package staff;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.sql.*;
/**
 * Controller class for managing chef-related functionalities.
 */
public class ChefController {

    @FXML
    private TableView<Object[]> ordersTableView;

    @FXML
    private TableColumn<Object[], Integer> orderIdColumn;

    @FXML
    private TableColumn<Object[], String> itemNameColumn;

 

    private final String databaseURL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    @FXML
    private void initialize() {
        // Initialize TableView columns
        orderIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty((Integer) cellData.getValue()[0]).asObject());
        itemNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty((String) cellData.getValue()[1]));
       

        // Load orders into TableView
        loadOrders();
    }
    /**
     * Sets the user data.
     *
     * @param userData the result set containing user data
     * @throws SQLException if a database access error occurs
     */
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        // this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        // userDataLogin.setText(userDataBuilder.toString());
    }
    /**
     * Handles the action when an order is marked as complete.
     */
    @FXML
    private void handleMarkAsComplete() {
    Object[] selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
    if (selectedOrder == null) {
        showAlert("Please select an order to mark as complete.");
        return;
    }

    int orderId = (int) selectedOrder[0];

    try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
        String updateQuery = "UPDATE Orders SET foodPrepStatus = 'Complete' WHERE order_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setInt(1, orderId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Order marked as complete.");
                // Remove the completed order from the TableView
                ordersTableView.getItems().remove(selectedOrder);
            } else {
                showAlert("Failed to mark order as complete.");
            }
        }
    } catch (SQLException e) {
        showAlert("Error: " + e.getMessage());
    }
}

    /**
     * Loads pending orders into the TableView.
     */
    private void loadOrders() {
        ordersTableView.getItems().clear();
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String selectQuery = "SELECT order_id, item_name FROM Orders WHERE foodPrepStatus= 'Pending'";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int id = resultSet.getInt("order_id");
                        String itemName = resultSet.getString("item_name");
                        ordersTableView.getItems().add(new Object[]{id, itemName});
                    }
                }
            }
        } catch (SQLException e) {
            showAlert("Error loading orders: " + e.getMessage());
        }
    }

    /**
     * Displays an alert with the given message.
     *
     * @param message the message to display in the alert
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Handles the logout action.
     */
    @FXML
    private void logout() {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
        Parent loginRoot = loader.load();
        
        // Get the current scene from any node in the scene graph
        Scene scene = ordersTableView.getScene();
        
        // Set the login scene as the root of the current scene
        scene.setRoot(loginRoot);
    } catch (IOException e) {
        e.printStackTrace();
    }
}


    
}
