package customer;

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
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import order.Menu;
import tableBooking.TableBooking;

public class Customer {
    /**
     * DATABASE_URL,USERNAME,PASSWORD used to connect to my localhost database
     */
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String TABLE_BOOKING_INFO = "SELECT * FROM tableBookingInfo WHERE name = ?";

    private String firstName;

    @FXML
    private Label userDataLogin;

    @FXML
    private Label tableBKStatus;

    @FXML
    private VBox displayTable;

    @FXML
    private StackPane orderPane;

    private ResultSet userData;

    /**
     * Method to receive user data from Login controller
     * 
     * @param userData whole userData is getting fetched on input term userData
     * @throws SQLException
     */
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.firstName = userData.getString("firstName");
        this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        userDataLogin.setText(userDataBuilder.toString());
        fetchTableBookingStatus();
    }

    /**
     * fetching Table Status based on name
     */
    private void fetchTableBookingStatus() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = TABLE_BOOKING_INFO;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String tableStatus = resultSet.getString("tableStatus");
                        switch (tableStatus) {
                            case "pending":
                                tableBKStatus.setText("Pending");
                                break;
                            case "Completed":
                                tableBKStatus.setText("Table Booked");
                                break;
                            default:
                                tableBKStatus.setText("");
                                break;
                        }
                        System.out.println("Order list updated from the database.");
                    } else {
                        System.out.println("No order found for the customer.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * once the user clicks on Menu image it will redirect user to Menu.fxml UI
     * where he can see all the menu list to order
     * 
     * @throws SQLException
     */
    @FXML
    private void goToMenu() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../order/Menu.fxml"));
            Parent mainPageRoot = loader.load();
            Menu tableBook = loader.getController();
            tableBook.setUser(userData);
            orderPane.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * once the user clicks on Table image it will redirect user to Table.fxml UI
     * where he can book table of his choice
     * 
     * @throws SQLException
     */
    @FXML
    private void goToTable() throws SQLException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../tableBooking/TableBooking.fxml"));
            Parent mainPageRoot = loader.load();
            TableBooking tableBook = loader.getController();
            tableBook.setUser(userData);
            orderPane.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * once the user clicks on Logout button it will redirect user to Login.fxml UI
     * 
     * @throws SQLException
     */
    @FXML
    private void logout() throws SQLException {
        System.out.println("Logged out " + userData.getString("firstName"));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent mainPageRoot = loader.load();
            orderPane.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getOrders() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM orders WHERE custName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    StringBuilder orderDetails = new StringBuilder();
                    while (resultSet.next()) {
                        String itemName = resultSet.getString("item_name");
                        String itemPrice = resultSet.getString("item_price");
                        orderDetails.append(itemName).append(" - ").append(itemPrice).append("\n");
                    }
                    if (orderDetails.length() > 0) {
                        displayOrderPopup(orderDetails.toString());
                    } else {
                        // System.out.println("No orders found for the customer.");
                        displayOrderPopup("No orders found for the customer.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void displayOrderPopup(String orderDetails) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Details");
        alert.setHeaderText("Order items for " + firstName + ":");
        alert.setContentText(orderDetails);
        alert.showAndWait();
    }
}
