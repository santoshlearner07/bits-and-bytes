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
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
import javafx.scene.layout.VBox;
import order.Menu;
import tableBooking.TableBooking;

public class Customer {
    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";

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

    // Method to receive user data from Login controller
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.firstName = userData.getString("firstName");
        this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        userDataLogin.setText(userDataBuilder.toString());
        fetchOrderListFromDatabase();
    }

    private void fetchOrderListFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM tableBookingInfo WHERE name = ?";
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
    

    @FXML
    private void customerClick() {
        System.out.println("Clicked");
    }

    @FXML
    private void goToMenu() throws SQLException {
        String firstName = userData.getString("firstName"); // Get the firstName from userData
        System.out.println(firstName);
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

    @FXML
    private void goToTable() throws SQLException {
        String firstName = userData.getString("firstName"); // Get the firstName from userData
        System.out.println(firstName);
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

}
