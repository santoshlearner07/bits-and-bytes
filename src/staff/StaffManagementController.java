package staff;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Controller class for the staff management interface.
 */
public class StaffManagementController {

    @FXML
    private Label nameLabel; 

    @FXML
    private Button logoutButton;
    /**
     * Handles the action to open the staff management UI.
     */
    @FXML
    private void handleStaffManagement() {
        openStaffManagementUI();
    }
    /**
     * Handles the action to open the manage bookings UI.
     */
    @FXML
    private void handleManageBookings() {
        openManageBookingsUI();
    }
    /**
     * Opens the staff management UI.
     */
    private void openStaffManagementUI() {
        try {
            // Load the FXML file of the staff management UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../staff/Manager.fxml"));
            Parent root = loader.load();

            // Create a new stage for the staff management UI
            Stage stage = new Stage();
            stage.setTitle("Staff Management");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Opens the manage bookings UI.
     */
    private void openManageBookingsUI() {
        try {
            // Load the FXML file of the manage bookings UI
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../staff/ManageBooking.fxml"));
            Parent root = loader.load();

            // Create a new stage for the manage bookings UI
            Stage stage = new Stage();
            stage.setTitle("Manage Bookings");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * Handles the action to generate a report.
     */
    @FXML
    private void handleGenerateReport() {
        // Call the generateReport method from the ReportGeneratorController
        new ReportGeneratorController().generateReport();
    }

    /**
     * Sets the user's name.
     *
     * @param resultSet ResultSet containing user information
     */
    public void setUser(ResultSet resultSet) {
        try {
            String name = resultSet.getString("firstName");
            nameLabel.setText("Welcome, " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
     /**
     * Handles the logout action.
     */
    @FXML
    private void logout() {
        try {
            // Load the Login.fxml file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent loginRoot = loader.load();

            // Get the stage from the logoutButton
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            
            // Set the login scene as the root of the stage
            stage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
