package metLibrary.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import metLibrary.model.StudentMember;
import metLibrary.model.User;

/**
 *Main entry point of the MetLibrary application
 *<p>
 *This class is responsible for:
 *<ul>
 *<li>Launching the JavaFX application</li>
 *<li>Managing screen navigation</li>
 *<li>Switching views based on user role</li>
 *</ul>
 *All GUI views communicate through this class to ensure
 *centralized and consistent navigation logic
 */

public class MainApp extends Application {

    private Scene scene;

    @Override
    public void start(Stage stage) {
        scene = new Scene(new LoginView(this).getView(), 900, 500);
        stage.setTitle("MetLibrary");
        stage.setScene(scene);
        stage.show();
    }

    public void showMainMenu(User user) {
        scene.setRoot(new MainMenuView(this, user).getView());
    }

    public void showCatalog(User user) {
        scene.setRoot(new CatalogView(this, user).getView());
    }

    public void showLoans(User user) {
        scene.setRoot(new LoanView(this, user).getView());
    }

    public void showStudentView(StudentMember student) {
        scene.setRoot(new StudentView(this, student).getView());
    }

    //Overdue report screen
    public void showOverdueReport(User user) {
        scene.setRoot(new OverdueView(this, user).getView());
    }

    public void showUserManagement(User user) {
        scene.setRoot(new UserManagementView(this, user).getView());
    }

    public void logout() {
        scene.setRoot(new LoginView(this).getView());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
