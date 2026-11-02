package metLibrary.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import metLibrary.model.*;
import metLibrary.service.*;

/**
 *Displays loan information and loan management functionality
 *<p>
 *Librarians can issue and return loans for students
 *Students can view their own loan history in read-only mode
 *Overdue loans are visually identified
 */

public class LoanView {

    private final MainApp app;
    private final User user;

    private final LoanService loanService = new LoanService();
    private final CatalogService catalogService = new CatalogService();
    private final UserService userService = new UserService();

    private final TableView<Loan> table = new TableView<>();

    public LoanView(MainApp app, User user) {
        this.app = app;
        this.user = user;
    }

    public Pane getView() {

        TableColumn<Loan, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getItem().getTitle()));

        TableColumn<Loan, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getMember().getName()));

        TableColumn<Loan, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getDueDate().toString()));

        TableColumn<Loan, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().isOverdue() ? "OVERDUE" : "OK"));

        table.getColumns().setAll(itemCol, studentCol, dueCol, statusCol);

        // Load loans
        var allLoans = loanService.getAllLoans();

        if (user instanceof StudentMember student) {
            table.setItems(FXCollections.observableArrayList(
                    allLoans.stream().filter(l -> l.getMember().getId().equals(student.getId())).toList()
            ));
        } else {
            table.setItems(FXCollections.observableArrayList(allLoans));
        }

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> app.showMainMenu(user));

        // Student: read-only
        if (!user.isLibrarian()) {
            VBox root = new VBox(10, new Label("My Loans (Read-only)"), table, backBtn);
            root.setPrefSize(900, 500);
            return root;
        }

        // Librarian issue/return controls
        ComboBox<LibraryItem> itemBox = new ComboBox<>();
        itemBox.setItems(FXCollections.observableArrayList(
                catalogService.getAllItems().stream().filter(LibraryItem::isAvailable).toList()
        ));
        itemBox.setPromptText("Select Item");

        ComboBox<StudentMember> studentBox = new ComboBox<>();
        studentBox.setItems(FXCollections.observableArrayList(
                userService.getAllUsers().stream()
                        .filter(u -> u instanceof StudentMember)
                        .map(u -> (StudentMember) u)
                        .toList()
        ));
        studentBox.setPromptText("Select Student");

        Button borrowBtn = new Button("Borrow");
        Button returnBtn = new Button("Return Selected");

        borrowBtn.setOnAction(e -> {
            LibraryItem item = itemBox.getValue();
            StudentMember member = studentBox.getValue();
            if (item == null || member == null) return;

            Loan loan = new Loan(item, member);
            loanService.issueLoan(loan);

            item.setAvailable(false);

            table.getItems().setAll(loanService.getAllLoans());
            itemBox.getItems().remove(item);
        });

        returnBtn.setOnAction(e -> {
            Loan selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) return;

            selected.getItem().setAvailable(true);
            loanService.returnLoan(selected);

            table.getItems().setAll(loanService.getAllLoans());

            // refresh available items
            itemBox.setItems(FXCollections.observableArrayList(
                    catalogService.getAllItems().stream().filter(LibraryItem::isAvailable).toList()
            ));
        });

        VBox controls = new VBox(10,
                new Label("Librarian Loan Management"),
                itemBox, studentBox, borrowBtn, returnBtn, backBtn
        );

        HBox root = new HBox(15, table, controls);
        root.setPrefSize(900, 500);
        return root;
    }
}
