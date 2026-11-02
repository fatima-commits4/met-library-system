package metLibrary.gui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import metLibrary.model.Loan;
import metLibrary.model.User;
import metLibrary.service.LoanService;

import java.util.List;

/**
 *Displays a list of overdue loans
 *<p>
 *This view is accessible to librarians only and retrieves
 *overdue loan data using background-safe service calls
 */

public class OverdueView {

    private final MainApp app;
    private final User user;
    private final LoanService loanService = new LoanService();

    public OverdueView(MainApp app, User user) {
        this.app = app;
        this.user = user;
    }

    public Pane getView() {

        Label title = new Label("Overdue Loans Report");
        Label status = new Label("Loading...");

        TableView<Loan> table = new TableView<>();

        TableColumn<Loan, String> itemCol = new TableColumn<>("Item");
        itemCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getItem().getTitle())
        );

        TableColumn<Loan, String> studentCol = new TableColumn<>("Student");
        studentCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getMember().getName())
        );

        TableColumn<Loan, String> dueCol = new TableColumn<>("Due Date");
        dueCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(d.getValue().getDueDate().toString())
        );

        table.getColumns().addAll(itemCol, studentCol, dueCol);

        Button refreshBtn = new Button("Refresh");
        Button backBtn = new Button("Back");

        backBtn.setOnAction(e -> app.showMainMenu(user));
        refreshBtn.setOnAction(e -> loadOverdue(table, status));

        VBox top = new VBox(6, title, status);
        HBox bottom = new HBox(10, refreshBtn, backBtn);

        BorderPane root = new BorderPane();
        root.setTop(top);
        root.setCenter(table);
        root.setBottom(bottom);
        root.setPrefSize(900, 500);

        // ✅ Concurrency: load in background thread
        loadOverdue(table, status);

        return root;
    }

    private void loadOverdue(TableView<Loan> table, Label status) {
        status.setText("Loading...");

        Task<List<Loan>> task = new Task<>() {
            @Override
            protected List<Loan> call() {
                // simulate heavy work (optional). Comment out if you want.
                // try { Thread.sleep(300); } catch (InterruptedException ignored) {}
                return loanService.getOverdueLoans();
            }
        };

        task.setOnSucceeded(e -> {
            List<Loan> overdue = task.getValue();
            table.setItems(FXCollections.observableArrayList(overdue));
            status.setText("Overdue loans: " + overdue.size());
        });

        task.setOnFailed(e -> status.setText("Failed to load overdue loans."));

        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
}
