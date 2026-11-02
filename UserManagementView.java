package metLibrary.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import metLibrary.model.StudentMember;
import metLibrary.model.User;
import metLibrary.service.UserService;

/**
 *Provides user management functionality for librarians
 *<p>
 *Allows librarians to:
 *<ul>
 *<li>Add new student accounts</li>
 *<li>Edit existing student details</li>
 *<li>Delete student accounts</li>
 *</ul>
 *Librarian accounts are protected from modification
 */
public class UserManagementView {

    private final MainApp app;
    private final User currentUser;
    private final UserService userService = new UserService();

    private final TableView<User> table = new TableView<>();

    public UserManagementView(MainApp app, User currentUser) {
        this.app = app;
        this.currentUser = currentUser;
    }

    public Pane getView() {

        // ---- Table columns
        TableColumn<User, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getId())
        );

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getName())
        );

        TableColumn<User, String> roleCol = new TableColumn<>("Role");
        roleCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().isLibrarian() ? "Librarian" : "Student"
                )
        );

        table.getColumns().setAll(idCol, nameCol, roleCol);
        table.setItems(FXCollections.observableArrayList(userService.getAllUsers()));

        // ---- Form fields
        TextField idField = new TextField();
        idField.setPromptText("Student ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        PasswordField pwField = new PasswordField();
        pwField.setPromptText("Password");

        Label msg = new Label();

        // ---- Buttons
        Button addBtn = new Button("Add Student");
        Button updateBtn = new Button("Update Selected");
        Button deleteBtn = new Button("Delete Selected");
        Button clearBtn = new Button("Clear");
        Button backBtn = new Button("Back");

        // ---- Click row -> load into form
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;

            idField.setText(sel.getId());
            nameField.setText(sel.getName());

            // Don’t auto-fill password for security; user must type a new one to change it
            pwField.clear();

            // Prevent editing librarian IDs (so you don't mess up admin)
            if (sel.isLibrarian()) {
                idField.setDisable(true);
                msg.setText("Librarian account cannot be edited/deleted.");
            } else {
                idField.setDisable(false);
                msg.setText("");
            }
        });

        // ---- Add
        addBtn.setOnAction(e -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            String pw = pwField.getText();

            if (id.isEmpty() || name.isEmpty() || pw.isEmpty()) {
                msg.setText("Fill all fields to add a student.");
                return;
            }

            // Simple duplicate ID check
            boolean exists = userService.getAllUsers().stream()
                    .anyMatch(u -> u.getId().equalsIgnoreCase(id));
            if (exists) {
                msg.setText("That ID already exists. Choose a different ID.");
                return;
            }

            userService.addUser(new StudentMember(id, name, pw));
            table.getItems().setAll(userService.getAllUsers());

            idField.clear(); nameField.clear(); pwField.clear();
            msg.setText("Student added.");
        });

        // ---- Update Selected (edit details)
        updateBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a student to update.");
                return;
            }
            if (selected.isLibrarian()) {
                msg.setText("Cannot update librarian.");
                return;
            }

            String newId = idField.getText().trim();
            String newName = nameField.getText().trim();
            String newPw = pwField.getText(); // optional: only changes if provided

            if (newId.isEmpty() || newName.isEmpty()) {
                msg.setText("ID and Name cannot be empty.");
                return;
            }

            // If ID changed, ensure it doesn't collide with another user
            boolean idChanged = !selected.getId().equalsIgnoreCase(newId);
            if (idChanged) {
                boolean exists = userService.getAllUsers().stream()
                        .anyMatch(u -> u.getId().equalsIgnoreCase(newId));
                if (exists) {
                    msg.setText("That new ID already exists.");
                    return;
                }
            }

            // Remove old + add updated (simple and reliable with your persistence)
            userService.removeUser(selected);

            String finalPw = (newPw == null || newPw.isBlank())
                    ? selected.getPassword() // keep old password if user didn't type new one
                    : newPw;

            userService.addUser(new StudentMember(newId, newName, finalPw));

            table.getItems().setAll(userService.getAllUsers());
            msg.setText("Student updated.");
        });

        // ---- Delete
        deleteBtn.setOnAction(e -> {
            User selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                msg.setText("Select a user to delete.");
                return;
            }
            if (selected.isLibrarian()) {
                msg.setText("Cannot delete librarian.");
                return;
            }

            userService.removeUser(selected);
            table.getItems().setAll(userService.getAllUsers());

            idField.clear(); nameField.clear(); pwField.clear();
            msg.setText("User deleted.");
        });

        // ---- Clear
        clearBtn.setOnAction(e -> {
            table.getSelectionModel().clearSelection();
            idField.setDisable(false);
            idField.clear();
            nameField.clear();
            pwField.clear();
            msg.setText("");
        });

        // ---- Back
        backBtn.setOnAction(e -> app.showMainMenu(currentUser));

        VBox form = new VBox(8,
                new Label("User Management (Librarian)"),
                idField, nameField, pwField,
                new HBox(10, addBtn, updateBtn),
                new HBox(10, deleteBtn, clearBtn),
                msg,
                backBtn
        );

        HBox root = new HBox(15, table, form);
        root.setPrefSize(900, 500);
        return root;
    }
}
