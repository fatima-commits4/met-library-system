package metLibrary.gui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import metLibrary.model.Librarian;
import metLibrary.model.StudentMember;
import metLibrary.model.User;

/**
 *Represents the main menu displayed after a successful login
 *<p>
 *The available options are determined by the user's role:
 *<ul>
 *<li>Librarians can manage catalog items, loans, users, and overdue reports</li>
 *<li>Students can browse the catalog and view their own loans</li>
 *</ul>
 */

public class MainMenuView {

    private final MainApp app;
    private final User user;
    private final VBox root = new VBox(12);

    public MainMenuView(MainApp app, User user) {
        this.app = app;
        this.user = user;

        root.setPadding(new Insets(20));

        Label welcome = new Label("Welcome, " + user.getName());

        // Everyone
        Button catalogBtn = new Button("Catalog");
        catalogBtn.setOnAction(e -> app.showCatalog(user));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setOnAction(e -> app.logout());

        root.getChildren().addAll(welcome, catalogBtn);

        // Librarian-only
        if (user instanceof Librarian) {
            Button loansBtn = new Button("Manage Loans");
            loansBtn.setOnAction(e -> app.showLoans(user));

            Button usersBtn = new Button("Manage Users");
            usersBtn.setOnAction(e -> app.showUserManagement(user));

            Button overdueBtn = new Button("Overdue Report");
            overdueBtn.setOnAction(e -> app.showOverdueReport(user));

            root.getChildren().addAll(loansBtn, usersBtn, overdueBtn);
        }

        // Student-only
        if (user instanceof StudentMember student) {
            Button myLoansBtn = new Button("My Loans");
            myLoansBtn.setOnAction(e -> app.showLoans(student));

            Button studentBtn = new Button("Student Area");
            studentBtn.setOnAction(e -> app.showStudentView(student));

            root.getChildren().addAll(myLoansBtn, studentBtn);
        }

        root.getChildren().add(logoutBtn);
        root.setPrefSize(400, 300);
    }

    public Parent getView() {
        return root;
    }
}

