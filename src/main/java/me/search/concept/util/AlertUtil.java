package me.search.concept.util;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

public final class AlertUtil {

    public static void createErrorAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void createInfoAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(text);
        alert.showAndWait();
    }

    public static void createLoginDialog(DialogCallable<Pair<String, String>> callable) {
        Dialog<Pair<String, String>> loginDialog = new Dialog<>();
        loginDialog.setTitle("需要登录");
        ButtonType loginButtonType = new ButtonType("登录", ButtonBar.ButtonData.OK_DONE);
        loginDialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("手机号");
        PasswordField password = new PasswordField();
        password.setPromptText("密码");

        grid.add(new Label("手机号:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("密码:"), 0, 1);
        grid.add(password, 1, 1);

        Node loginButton = loginDialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        username.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        loginDialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> username.requestFocus());

        loginDialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = loginDialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            callable.onOk(new Pair<>(usernamePassword.getKey(), usernamePassword.getValue()));
        });

        result.orElseGet(() -> {
           callable.onCancel();
           return null;
        });

    }

    public interface DialogCallable<V> {
        void onOk(V value);
        void onCancel();
    }

}
