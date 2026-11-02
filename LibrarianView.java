package metLibrary.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import metLibrary.model.Librarian;

/**
 *Represents the librarian dashboard of the MetLibrary application
 *<p>
 *This view provides privileged access to administrative functionality,
 *allowing librarians to:
 *<ul>
 *<li>Manage the library catalog</li>
 *<li>Manage loan transactions</li>
 *<li>Access other administrative views</li>
 *</ul>
 *Navigation actions are delegated to {@link MainApp}
 */

public class LibrarianView {

    private final MainApp app;
    private final Librarian librarian;

    public LibrarianView(MainApp app, Librarian librarian) {
        this.app = app;
        this.librarian = librarian;
    }

    public VBox getView() {
        Label welcome = new Label("Welcome Librarian: " + librarian.getName());

        Button catalogBtn = new Button("Manage Catalog");
        Button loanBtn = new Button("Manage Loans");
        Button logoutBtn = new Button("Logout");

        catalogBtn.setOnAction(e -> app.showCatalog(librarian));
        loanBtn.setOnAction(e -> app.showLoans(librarian));
        logoutBtn.setOnAction(e -> app.logout());

        VBox root = new VBox(15, welcome, catalogBtn, loanBtn, logoutBtn);
        root.setPrefSize(400, 300);
        return root;
    }
}
