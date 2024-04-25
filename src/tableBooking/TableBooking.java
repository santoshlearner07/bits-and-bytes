package tableBooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class TableBooking {
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String AVAILABLE_TABLE_COUNT = "SELECT COUNT(*) AS available_tables_count FROM restaurant_tables WHERE seats = ? AND is_available = 1";
    private final String TABLE_BOOKED = "UPDATE restaurant_tables SET is_available = 0 WHERE seats = ? AND is_available = 1 LIMIT 1";

    @FXML
    private ComboBox<String> seatsComboBox;

    @FXML
    private Label availabilityLabel;

    @FXML
    private Button bookTable;

    @FXML
    public void initialize() {
        seatsComboBox.setItems(FXCollections.observableArrayList(
                "2 seats", "4 seats", "8 seats", "10 seats", "11 seats"));
    }

    @FXML
    public void checkingSeats() {
        String selectedSeats = seatsComboBox.getValue();
        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            int selectedSeatsCount = Integer.parseInt(selectedSeats.split(" ")[0]);
            int availableTablesCount = countAvailableTables(selectedSeatsCount);
            availabilityLabel
                    .setText("Available tables for " + selectedSeats + ": " + availableTablesCount);

            bookTable.setVisible(availableTablesCount > 0);
        } else {
            availabilityLabel.setText("Please select the number of seats.");
        }
    }

    private int countAvailableTables(int seats) {
        int count = 0;
        try (Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = AVAILABLE_TABLE_COUNT;
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, seats);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        count = rs.getInt("available_tables_count");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @FXML
    public void bookATable() {
        String selectedSeats = seatsComboBox.getValue();
        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            int selectedSeatsCount = Integer.parseInt(selectedSeats.split(" ")[0]);
            try (Connection conn = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
                String sql = TABLE_BOOKED;
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setInt(1, selectedSeatsCount);
                    int rowsUpdated = stmt.executeUpdate();
                    availabilityLabel
                            .setText("Table Booked");
                    if (rowsUpdated > 0) {
                        System.out.println("Table booked successfully.");
                    } else {
                        System.out.println("Failed to book the table.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
