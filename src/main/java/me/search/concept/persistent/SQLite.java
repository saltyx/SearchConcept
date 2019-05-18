package me.search.concept.persistent;

import me.search.concept.model.ConceptTableViewItem;
import me.search.concept.model.Concepts;
import me.search.concept.util.FileUtil;
import me.search.concept.util.LogUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public enum SQLite implements SQLiteInterface {

    INSTANCE {
        @Override
        public void queryAllConcepts(QueryCallable<List<ConceptTableViewItem>> callable) {
            List<ConceptTableViewItem> result = new ArrayList<>();
            try(Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        "select updated_time, concept_name from stock_concept;")) {
                while (resultSet.next()) {
                    String updatedTime = resultSet.getString("updated_time");
                    String conceptName = resultSet.getString("concept_name");
                    ConceptTableViewItem item = new ConceptTableViewItem(conceptName, updatedTime);
                    result.add(item);
                }

                callable.onDataAvailable(result);
            } catch (Exception e) {
                callable.onFail(e.getMessage(), e);
            }
        }

        @Override
        public void queryConceptsByStockCode(String stockCode, QueryCallable<List<ConceptTableViewItem>> callable) {
            List<ConceptTableViewItem> result = new ArrayList<>();
            try(Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(
                        String.format("select updated_time, concept_name from stock_concept where stock_code='%s';",stockCode))) {
                while (resultSet.next()) {
                    String updatedTime = resultSet.getString("updated_time");
                    String conceptName = resultSet.getString("concept_name");
                    ConceptTableViewItem item = new ConceptTableViewItem(conceptName, updatedTime);
                    result.add(item);
                }

                callable.onDataAvailable(result);
            } catch (Exception e) {
                callable.onFail(e.getMessage(), e);
            }
        }

        @Override
        public void insertConcepts(List<Concepts> conceptsList, ExecuteCallable<String> callable) {
            try(Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                for (Concepts concept : conceptsList) {
                    statement.execute(concept.toInsertSql());
                }
                connection.commit();
                callable.onSuccess(null);
            } catch (Exception e){
                callable.onFail(e.getMessage());
            }
        }

        @Override
        public void clearConcepts(ExecuteCallable<String> callable) {
            try(Connection connection = getConnection();
                Statement statement = connection.createStatement()) {
                statement.execute("delete from stock_concept");
                connection.commit();
                callable.onSuccess(null);
            } catch (Exception e) {
                callable.onFail(e.getMessage());
            }
        }
    };

    public static SQLite getInstance() {
        return INSTANCE;
    }

    private static Connection getConnection() throws Exception {
        Class.forName("org.sqlite.JDBC");
        Connection connection = DriverManager.getConnection("jdbc:sqlite:" + FileUtil.getDbPath());
        LogUtil.info(FileUtil.getDbPath());
        connection.setAutoCommit(false);
        return connection;
    }

    public interface ExecuteCallable<V> {
        void onSuccess(V value);
        void onFail(V value);
    }

    public interface QueryCallable<V> {
        void onDataAvailable(V value);
        void onFail(String error, Exception e);
    }
}
