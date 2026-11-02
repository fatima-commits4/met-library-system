package metLibrary.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import metLibrary.model.*;
import metLibrary.service.CatalogService;

/**
 *Displays the library catalog.
 *<p>
 *Behavior depends on the user role:
 *<ul>
 *<li>Students have read-only access</li>
 *<li>Librarians can add, update, and delete catalog items</li>
 *</ul>
 *The catalog supports both books and journals using polymorphism
 */

public class CatalogView {

    private final MainApp app;
    private final User user;

    private final CatalogService catalogService = new CatalogService();
    private final TableView<LibraryItem> table = new TableView<>();

    public CatalogView(MainApp app, User user) {
        this.app = app;
        this.user = user;
    }

    public Pane getView() {

        // ---- Table columns
        TableColumn<LibraryItem, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getItemId()));

        TableColumn<LibraryItem, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getTitle()));

        TableColumn<LibraryItem, String> creatorCol = new TableColumn<>("Author / Editor");
        creatorCol.setCellValueFactory(d -> {
            LibraryItem it = d.getValue();
            if (it instanceof Book b) return new javafx.beans.property.SimpleStringProperty(b.getAuthor());
            if (it instanceof Journal j) return new javafx.beans.property.SimpleStringProperty(j.getEditor());
            return new javafx.beans.property.SimpleStringProperty("");
        });

        TableColumn<LibraryItem, String> codeCol = new TableColumn<>("ISBN / ISSN");
        codeCol.setCellValueFactory(d -> {
            LibraryItem it = d.getValue();
            if (it instanceof Book b) return new javafx.beans.property.SimpleStringProperty(b.getIsbn());
            if (it instanceof Journal j) return new javafx.beans.property.SimpleStringProperty(j.getIssn());
            return new javafx.beans.property.SimpleStringProperty("");
        });

        table.getColumns().setAll(idCol, titleCol, creatorCol, codeCol);
        table.setItems(FXCollections.observableArrayList(catalogService.getAllItems()));

        // ---- Back
        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showMainMenu(user));

        // ---- Student: read-only catalog
        if (!user.isLibrarian()) {
            VBox left = new VBox(10, new Label("Catalog (Read-only)"), table, backBtn);
            left.setPrefSize(900, 500);
            return left;
        }

        // ---- Librarian controls (with Update)
        TextField idField = new TextField(); idField.setPromptText("ID (e.g., B001 / J001)");
        TextField titleField = new TextField(); titleField.setPromptText("Title");
        TextField creatorField = new TextField(); creatorField.setPromptText("Author / Editor");
        TextField codeField = new TextField(); codeField.setPromptText("ISBN / ISSN");

        ComboBox<String> typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Book", "Journal");
        typeBox.setValue("Book");

        Label msg = new Label();

        Button addBtn = new Button("Add");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear");

        // ---- Click row -> load into fields
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;

            idField.setText(sel.getItemId());
            titleField.setText(sel.getTitle());

            if (sel instanceof Book b) {
                typeBox.setValue("Book");
                creatorField.setText(b.getAuthor());
                codeField.setText(b.getIsbn());
            } else if (sel instanceof Journal j) {
                typeBox.setValue("Journal");
                creatorField.setText(j.getEditor());
                codeField.setText(j.getIssn());
            } else {
                creatorField.clear();
                codeField.clear();
            }

            // lock type while editing an existing record
            typeBox.setDisable(true);
            msg.setText("");
        });

        // ---- Add
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String title = titleField.getText().trim();
            String creator = creatorField.getText().trim();
            String code = codeField.getText().trim();

            if (id.isEmpty() || title.isEmpty() || creator.isEmpty() || code.isEmpty()) {
                msg.setText("Fill all fields to add an item.");
                return;
            }

            boolean exists = catalogService.getAllItems().stream()
                    .anyMatch(it -> it.getItemId().equalsIgnoreCase(id));
            if (exists) {
                msg.setText("That Item ID already exists. Choose a different ID.");
                return;
            }

            LibraryItem item = typeBox.getValue().equals("Book")
                    ? new Book(id, title, creator, code)
                    : new Journal(id, title, creator, code);

            catalogService.addItem(item);
            table.getItems().setAll(catalogService.getAllItems());

            clearForm(table, idField, titleField, creatorField, codeField, typeBox, msg);
            msg.setText("Item added.");
        });

        // ---- Update Selected
        updateBtn.setOnAction(e -> {
            LibraryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select an item to update.");
                return;
            }

            String newId = idField.getText().trim();
            String newTitle = titleField.getText().trim();
            String newCreator = creatorField.getText().trim();
            String newCode = codeField.getText().trim();

            if (newId.isEmpty() || newTitle.isEmpty() || newCreator.isEmpty() || newCode.isEmpty()) {
                msg.setText("Fields cannot be empty.");
                return;
            }

            boolean idChanged = !selected.getItemId().equalsIgnoreCase(newId);
            if (idChanged) {
                boolean exists = catalogService.getAllItems().stream()
                        .anyMatch(it -> it.getItemId().equalsIgnoreCase(newId));
                if (exists) {
                    msg.setText("That new Item ID already exists.");
                    return;
                }
            }

            // Keep original type (Book stays Book, Journal stays Journal)
            boolean wasAvailable = selected.isAvailable();

            catalogService.removeItem(selected);

            LibraryItem updated;
            if (selected instanceof Book) {
                updated = new Book(newId, newTitle, newCreator, newCode);
            } else {
                updated = new Journal(newId, newTitle, newCreator, newCode);
            }
            updated.setAvailable(wasAvailable);

            catalogService.addItem(updated);
            table.getItems().setAll(catalogService.getAllItems());

            clearForm(table, idField, titleField, creatorField, codeField, typeBox, msg);
            msg.setText("Item updated.");
        });

        // ---- Delete
        deleteBtn.setOnAction(e -> {
            LibraryItem selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select an item to delete.");
                return;
            }

            catalogService.removeItem(selected);
            table.getItems().setAll(catalogService.getAllItems());

            clearForm(table, idField, titleField, creatorField, codeField, typeBox, msg);
            msg.setText("Item deleted.");
        });

        // ---- Clear
        clearBtn.setOnAction(e -> clearForm(table, idField, titleField, creatorField, codeField, typeBox, msg));

        VBox form = new VBox(
                8,
                new Label("Librarian Catalog Management"),
                idField, titleField, creatorField, codeField, typeBox,
                new HBox(10, addBtn, updateBtn),
                new HBox(10, deleteBtn, clearBtn),
                msg,
                backBtn
        );

        HBox root = new HBox(15, table, form);
        root.setPrefSize(900, 500);
        return root;
    }

    private void clearForm(
            TableView<LibraryItem> table,
            TextField idField,
            TextField titleField,
            TextField creatorField,
            TextField codeField,
            ComboBox<String> typeBox,
            Label msg
    ) {
        table.getSelectionModel().clearSelection();
        idField.clear();
        titleField.clear();
        creatorField.clear();
        codeField.clear();
        typeBox.setValue("Book");
        typeBox.setDisable(false);
        msg.setText("");
    }
}


