package metLibrary.gui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import metLibrary.model.User;
import metLibrary.service.UserService;

/**
 *Represents the login screen of the MetLibrary application
 *<p>
 *Allows users to authenticate using their user ID and password
 *Upon successful login, users are redirected to the main menu
 *appropriate to their role
 */

public class LoginView {

    private final MainApp app;
    private final UserService userService = new UserService();
    private final VBox root = new VBox(10);

    public LoginView(MainApp app) {
        this.app = app;

        root.setPadding(new Insets(20));

        Label title = new Label("MetLibrary Login");
        Label hint = new Label("Hint: admin / admin");

        TextField idField = new TextField();
        idField.setPromptText("User ID");

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Password");

        Label error = new Label();
        Button loginBtn = new Button("Login");

        loginBtn.setOnAction(e -> {
            User user = userService.login(idField.getText().trim(), pwField.getText());
            if (user != null) app.showMainMenu(user);
            else error.setText("Invalid ID or password");
        });

        root.getChildren().addAll(title, hint, idField, pwField, loginBtn, error);
    }

    public Parent getView() {
        return root;
    }
}
