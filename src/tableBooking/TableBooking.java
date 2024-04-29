package tableBooking;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import customer.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class TableBooking {
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String CUSTOMER_TABLE_BOOKED = "INSERT INTO tableBookingInfo (name, tableSeat, tblBkDt,tblBkTime, tableStatus) VALUES (?, ?, ?, ?,?)";

    private String firstName;
    @FXML
    private ComboBox<String> seatsComboBox;

    @FXML
    private ComboBox<String> timeComboBox;

    @FXML
    private Label availabilityLabel;

    @FXML
    private Label bookingStatus;

    @FXML
    private Button bookTable;

    @FXML
    private StackPane tableBK;

    @FXML
    private DatePicker datePicker;

    private ResultSet userData;

    @FXML
    public void initialize() {
        seatsComboBox.setItems(FXCollections.observableArrayList(
                "2 seats", "4 seats", "8 seats", "10 seats"));

        timeComboBox.setItems(FXCollections.observableArrayList(
                "11:00 AM", "12:00 PM", "01:00 PM", "02:00 PM", "03:00 PM", "04:00 PM", "05:00 PM", "06:00 PM",
                "07:00 PM", "08:00 PM", "09:00 PM",
                "10:00 PM", "11:00 PM"));

        datePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate currentDate = LocalDate.now();
                LocalDate maxDate = currentDate.plusDays(15);
                setDisable(empty || date.isBefore(currentDate) || date.isAfter(maxDate));
            }
        });
    }

    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.userData = userData;
        this.firstName = userData.getString("firstName");
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
    }

    @FXML
    public void bookATable() throws SQLException {
        if (userData == null) {
            System.out.println("User data is null.");
            return;
        }

        String selectedSeats = seatsComboBox.getValue();
        String selectedTime = timeComboBox.getValue();
        LocalDate selectedDate = datePicker.getValue(); // Get selected date as LocalDate

        if (selectedSeats != null && !selectedSeats.isEmpty() &&
                selectedTime != null && !selectedTime.isEmpty() &&
                selectedDate != null) {
            int selectedSeatsCount = Integer.parseInt(selectedSeats.split(" ")[0]);
            String userName = userData.getString("firstName");

            try (Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
                try (PreparedStatement stmt = conn.prepareStatement(CUSTOMER_TABLE_BOOKED)) {
                    stmt.setString(1, userName);
                    stmt.setInt(2, selectedSeatsCount);
                    stmt.setDate(3, java.sql.Date.valueOf(selectedDate));
                    stmt.setString(4, selectedTime);
                    stmt.setString(5, "pending");
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("Table booked successfully.");
                        seatsComboBox.setValue(null);
                        timeComboBox.setValue(null);
                        datePicker.setValue(null);
                        bookingStatus.setText("Table booked, waiting for manager approval.");
                    } else {
                        System.out.println("Failed to book the table.");
                        bookingStatus.setText("Fill every field.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Please select all options.");
            bookingStatus.setText("Fill every field.");
        }
    }

    @FXML
    public void goBack() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
            Parent customerRoot = loader.load();
            Customer customerController = loader.getController();
            customerController.setUser(userData); // Pass user data to the customer controller if needed
            tableBK.getChildren().setAll(customerRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}