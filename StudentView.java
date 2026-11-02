package metLibrary.gui;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import metLibrary.model.StudentMember;
import metLibrary.service.CatalogService;

/**
 *Represents the student-only interface
 *<p>
 *Allows student members to:
 *<ul>
 *<li>Search the library catalog</li>
 *<li>View borrowed items</li>
 *</ul>
 *All functionality is read-only
 */
public class StudentView {

    private final MainApp app;
    private final StudentMember student;
    private final CatalogService catalogService = new CatalogService();
    private final VBox root = new VBox(10);

    public StudentView(MainApp app, StudentMember student) {
        this.app = app;
        this.student = student;

        root.setPadding(new Insets(15));

        Label label = new Label("Student Catalog Search");
        TextField searchField = new TextField();
        searchField.setPromptText("Search by title, author/editor, ID, ISBN/ISSN");

        Button searchBtn = new Button("Search");
        TextArea results = new TextArea();
        results.setEditable(false);

        searchBtn.setOnAction(e -> {
            results.clear();
            catalogService.search(searchField.getText())
                    .forEach(item ->
                            results.appendText(item.getItemId() + " - " + item.getTitle() + "\n")
                    );
        });

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showMainMenu(student));

        root.getChildren().addAll(label, searchField, searchBtn, results, backBtn);
    }

    public Parent getView() {
        return root;
    }
}

