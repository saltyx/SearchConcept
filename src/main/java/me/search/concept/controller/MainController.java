package me.search.concept.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Pair;
import me.search.concept.model.BaseConcept;
import me.search.concept.model.ConceptTableViewItem;
import me.search.concept.model.Concepts;
import me.search.concept.persistent.SQLite;
import me.search.concept.util.*;
import me.search.concept.util.http.ApiHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainController {

    @FXML
    private TextField command;

    @FXML
    private Button search;

    @FXML
    private TableView conceptsTable;

    private final ObservableList<ConceptTableViewItem> data = FXCollections.observableArrayList();

    private String token = null;

    @FXML
    public void initialize() throws Exception {
        readyConfig();
        readyDatabase();
        readyObservableData();
    }

    @FXML
    public void onSearch() throws Exception {
        LogUtil.info("执行命令 " + command.getText());
        search.setText("请稍等...");
        search.setDisable(true);
        executeCommand(command.getText());
        restoreStatus();
    }

    private void readyObservableData() {
        ObservableList<TableColumn> observableList = conceptsTable.getColumns();
        observableList.get(0).setCellValueFactory(new PropertyValueFactory("updatedTime"));
        observableList.get(1).setCellValueFactory(new PropertyValueFactory("conceptName"));

        conceptsTable.setItems(data);

    }

    private void readyConfig() throws Exception {
        InputStream inputStream = getClass().getResourceAsStream("/config/token.token");
        if (inputStream == null) {
            AlertUtil.createErrorAlert("token文件不存在");
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        int c;
        while ((c = inputStream.read()) != -1) {
            if ((char) c != '\r') {
                stringBuilder.append((char) c);
            }
        }

        this.token = stringBuilder.toString();
        if (this.token.isEmpty()) {
           tryLogin();
        }
    }

    private void readyDatabase() {
        InputStream inputStream = getClass().getResourceAsStream("/database/concepts.db");
        if (inputStream == null) {
            AlertUtil.createErrorAlert("concepts.db不存在");
            ApplicationUtil.exit();
        }
    }

    private void tryLogin() {
        AlertUtil.createLoginDialog(new AlertUtil.DialogCallable<Pair<String, String>>() {
            @Override
            public void onOk(Pair<String, String> value) {
                try {
                    String mobile = value.getKey();
                    String pwd = value.getValue();

                    ApiHttpClient.getInstance().getTokenRequest(mobile, pwd, new ApiHttpClient.ApiCallable<String>() {
                        @Override
                        public void onSuccess(String value) {
                            try {
                                FileUtil.writeTokenFile(value);
                                token = value;
                            } catch (Exception e) {
                                e.printStackTrace();
                                AlertUtil.createErrorAlert(e.getMessage());
                            }
                        }

                        @Override
                        public void onInvalid(String value) {
                            AlertUtil.createErrorAlert("认证失败");
                            tryLogin();
                        }

                        @Override
                        public void onFail(String value) {
                            AlertUtil.createErrorAlert(value);
                            ApplicationUtil.exit();
                        }
                    });
                } catch (Exception e) {
                    AlertUtil.createErrorAlert(e.getMessage());
                }
            }

            @Override
            public void onCancel() {
                AlertUtil.createErrorAlert("登录失败");
                ApplicationUtil.exit();
            }
        });
    }

    private void executeCommand(String command) throws Exception {
        if (StringUtils.isEmpty(command)) {
            return;
        }

        switch (command) {
            case "ALL":
                all();
                break;
            case "REFRESH":
                refresh();
                break;
            case "CLEAR":
                clear();
                break;
            default:
                search(command);
                break;
        }
    }

    private void all() {
        SQLite.getInstance().queryAllConcepts(new SQLite.QueryCallable<List<ConceptTableViewItem>>() {
            @Override
            public void onDataAvailable(List<ConceptTableViewItem> value) {
                // TODO 绑定到TableView 上
            }

            @Override
            public void onFail(String error, Exception e) {
                AlertUtil.createErrorAlert(error);
            }
        });
    }

    private void refresh() throws IOException {
        final List<Concepts> concepts = new ArrayList<>();
        ApiHttpClient.getInstance().getConcepts(token, new ApiHttpClient.ApiCallable<List<BaseConcept>>() {
            @Override
            public void onSuccess(List<BaseConcept> value) {
                for (BaseConcept baseConcept : value) {
                    try {
                        ApiHttpClient.getInstance().getConceptStocks(token, baseConcept.getConceptCode(),
                            new ApiHttpClient.ApiCallable<List<String>>() {
                                @Override
                                public void onSuccess(List<String> value) {
                                    for (String stockCode : value) {
                                        Concepts temp = new Concepts();
                                        temp.setUpdatedTime(baseConcept.getUpdatedTime());
                                        temp.setStockCode(stockCode);
                                        temp.setConceptName(baseConcept.getConceptName());
                                        temp.setConceptCode(baseConcept.getConceptCode());
                                        concepts.add(temp);
                                    }
                                }

                                @Override
                                public void onInvalid(List<String> value) {
                                    tryLogin();
                                }

                                @Override
                                public void onFail(List<String> value) {
                                    AlertUtil.createErrorAlert("获取股票概念失败");
                                }
                            });
                    } catch (Exception e) {
                        AlertUtil.createErrorAlert(e.getMessage());
                        break;
                    }
                }
            }

            @Override
            public void onInvalid(List<BaseConcept> value) {
                tryLogin();
            }

            @Override
            public void onFail(List<BaseConcept> value) {
                AlertUtil.createErrorAlert("获取所有概念失败");
            }
        });

        SQLite.getInstance().insertConcepts(concepts, new SQLite.ExecuteCallable<String>() {
            @Override
            public void onSuccess(String value) {
                LogUtil.info("概念刷新成功");
                //TODO 绑定到tableView
            }

            @Override
            public void onFail(String value) {
                AlertUtil.createErrorAlert(value);
            }


        });
    }

    private void search(String command) {
        if (command.startsWith("0")) {
            command += ".XSHE";
        } else if (command.startsWith("6")) {
            command += ".XSHG";
        } else if (command.startsWith("3")) {
            command += ".XSHE";
        } else {
            AlertUtil.createErrorAlert("代码不合规");
            return;
        }
         SQLite.getInstance().queryConceptsByStockCode(command, new SQLite.QueryCallable<List<ConceptTableViewItem>>() {
            @Override
            public void onDataAvailable(List<ConceptTableViewItem> value) {
                data.clear();
                data.addAll(value);
                LogUtil.info("查询完毕");
            }

            @Override
            public void onFail(String error, Exception e) {
                LogUtil.info(error);
                AlertUtil.createErrorAlert(error);
            }
        });
    }

    private void clear() {
        SQLite.getInstance().clearConcepts(new SQLite.ExecuteCallable<String>() {
            @Override
            public void onSuccess(String value) {
                LogUtil.info("表已经清空");
                AlertUtil.createInfoAlert("表已清空");
            }

            @Override
            public void onFail(String value) {
                AlertUtil.createErrorAlert(value);
            }
        });
    }

    private void restoreStatus() {
        search.setDisable(false);
        search.setText("查询");

    }
}
