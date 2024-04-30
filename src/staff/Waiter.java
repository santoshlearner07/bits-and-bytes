package staff;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.layout.StackPane;

public class Waiter {

    private final String databaseURL = "jdbc:mysql://localhost:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private String firstName;

    @FXML
    private StackPane goToPage;

    @FXML
    private ListView<String> orderListView;

    public void setUser(ResultSet userData) throws SQLException {
        // Extract first name from the user data and store it
        this.firstName = userData.getString("firstName");
    }

    @FXML
    private void initialize() {
        // Fetch orders from the database and populate the orderListView
        fetchAllOrders();
    }

    private void fetchAllOrders() {
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String sql = "SELECT * FROM orders WHERE status = 'pending' AND foodPrepStatus = 'Complete'";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = statement.executeQuery()) {
                    ObservableList<String> items = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        String itemName = resultSet.getString("item_name");
                        String custName = resultSet.getString("custName");
                        String status = resultSet.getString("status");
                        items.add("Customer Name:- " + custName + " -> Order - " + itemName + " - " + " Status: "
                                + status);
                    }
                    orderListView.setItems(items);
                    System.out.println("Order list updated from the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void completeOrder() {
        String selectedOrder = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrder != null) {
            String[] parts = selectedOrder.split(" -> ");
            String[] orderInfo = parts[1].split(" - ");
            String itemName = orderInfo[1];
            String customerNameWithPrefix = parts[0];
            String customerName = customerNameWithPrefix.substring(customerNameWithPrefix.indexOf(":-") + 3).trim();

            try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
                String sql = "UPDATE orders SET status = 'Completed' WHERE custName = ? AND item_name = ?";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, customerName);
                    statement.setString(2, itemName);
                    int rowsUpdated = statement.executeUpdate();
                    if (rowsUpdated > 0) {
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Order Completed");
                        alert.setHeaderText(null);
                        alert.setContentText("Order Completed. Delivered it to Table");
                        alert.showAndWait();
                        fetchAllOrders(); // Refresh order list
                    } else {
                        System.out.println("Failed to update order status.");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent mainPageRoot = loader.load();
            goToPage.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
