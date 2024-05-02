package staff;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import java.sql.*;

public class ManageBookingController {

    @FXML
    private TableView<Booking> bookingTableView;

    @FXML
    private TableColumn<Booking, String> nameColumn;

    @FXML
    private TableColumn<Booking, String> tableColumn; 

    @FXML
    private TableColumn<Booking, String> bookingDateColumn;

    @FXML
    private TableColumn<Booking, String> bookingTimeColumn;

    @FXML
     private TableColumn<Booking, String> statusColumn;


    @FXML
    private Label statusLabel;

    private final String databaseURL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    @FXML
    private void initialize() {
        initializeTableView();
    }

    private void initializeTableView() {
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getTableSeat())));
        bookingDateColumn.setCellValueFactory(cellData -> cellData.getValue().bookingDateProperty());
        bookingTimeColumn.setCellValueFactory(cellData -> cellData.getValue().bookingTimeProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());
    
        loadBookingDataFromDatabase();
    }
    /**
     * Handles the action when the user clicks the "Back" button.
     */
    @FXML
    private void goBack() {
        // Get the current stage and close it
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }
    /**
     * Loads booking data from the database into the TableView.
     */
    private void loadBookingDataFromDatabase() {
        ObservableList<Booking> bookingList = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String selectQuery = "SELECT * FROM tableBookingInfo where tableStatus='Pending'";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Booking booking = new Booking(
                        resultSet.getString("name"),
                        resultSet.getInt("tableSeat"),
                        resultSet.getString("tblBkDt"),
                        resultSet.getString("tblBkTime"),
                        resultSet.getString("tableStatus")
                );
                bookingList.add(booking);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bookingTableView.setItems(bookingList);
    }
    /**
     * Handles the action when the user clicks the "Approve" button.
     */
    @FXML
    private void handleApproveBooking() {
    Booking selectedBooking = bookingTableView.getSelectionModel().getSelectedItem();
    if (selectedBooking != null) {
        // Update the database to reflect the approved status
        updateBookingStatusInDatabase(selectedBooking);

        // Remove the selected booking from the TableView
        bookingTableView.getItems().remove(selectedBooking);

        // Update status label
        statusLabel.setText("Booking Approved: " + selectedBooking.getName());
    } else {
        statusLabel.setText("Please select a booking to approve.");
    }
}

    /**
     * Updates the status of the booking in the database.
     *
     * @param booking The booking to update.
     */
    private void updateBookingStatusInDatabase(Booking booking) {
    try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
        String updateQuery = "UPDATE tableBookingInfo SET tableStatus = ? WHERE name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
        preparedStatement.setString(1, "Approved"); // Set status to "Approved"
        preparedStatement.setString(2, booking.getName());
        preparedStatement.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    

}
