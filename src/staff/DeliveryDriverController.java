package staff;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DeliveryDriverController {

    private final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private TableView<ObservableList<String>> ordersTableView;

    @FXML
    private TableColumn<ObservableList<String>, String> orderIdColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> itemNameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> custNameColumn;

    @FXML
    private TableColumn<ObservableList<String>, String> custAddrColumn;

    @FXML
    private Button markAsDeliveredButton;

    @FXML
    private void initialize() {
        // Initialize table columns
        orderIdColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));
        itemNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        custNameColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        custAddrColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));

        // Load orders data
        fetchOrdersData();
    }

    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        // this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        // userDataLogin.setText(userDataBuilder.toString());
    }

    @FXML
    private void handleMarkAsDelivered() {
        LocalTime currentTime = LocalTime.now();
        String formattedTime = currentTime.format(timeFormatter);
        ObservableList<String> selectedOrder = ordersTableView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            // Mark the selected order as delivered
            markOrderAsDelivered(selectedOrder);
        } else {
            showErrorAlert("No Order Selected", "Please select an order to mark as delivered.");
        }
    }

    private void fetchOrdersData() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String query = "SELECT orders.order_id, orders.item_name, orders.custName, signup.address " +
                    "FROM orders " +
                    "INNER JOIN signup ON orders.custName = signup.firstName " +
                    "WHERE orders.services = 'Delivery' AND orders.status = 'pending'";
            try (PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet resultSet = statement.executeQuery()) {
                ordersTableView.getItems().clear();
                while (resultSet.next()) {
                    ObservableList<String> orderData = FXCollections.observableArrayList(
                            resultSet.getString("order_id"),
                            resultSet.getString("item_name"),
                            resultSet.getString("custName"),
                            resultSet.getString("address"));
                    ordersTableView.getItems().add(orderData);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while fetching orders data.");
        }
    }

    private void markOrderAsDelivered(ObservableList<String> order) {
        int orderId = Integer.parseInt(order.get(0)); // Assuming order ID is stored in the first column
        System.out.println(orderId + "line 110");
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String updateQuery = "UPDATE orders SET status = 'Complete' WHERE order_id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setInt(1, orderId);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Refresh orders data after update
                    fetchOrdersData();
                    showAlert(Alert.AlertType.INFORMATION, "Order Status Updated",
                            "The order status has been updated to Complete.");
                } else {
                    showErrorAlert("Update Failed", "Failed to update the order status.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorAlert("Database Error", "An error occurred while updating the order status.");
        }
    }

    private void showErrorAlert(String title, String content) {
        showAlert(Alert.AlertType.ERROR, title, content);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

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
