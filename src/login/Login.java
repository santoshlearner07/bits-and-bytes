package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import customer.Customer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import staff.ChefController;
import staff.DeliveryDriverController;
import staff.Waiter;

public class Login {
    @FXML
    private Label aUser;

    @FXML
    private TextField userName;

    @FXML
    private StackPane goToPage;

    private final String databaseURL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, username, password);
    }

    /**
     * handleLogin() fetching data from signup table based on userName
     * and based on there role there UI renders on screen for example, Manager,
     * Customer, Waiter
     * if userName does not match on UI it will display as userName does not exist
     * 
     * @throws SQLException
     */
    @FXML
    private void handleLogin() throws SQLException {
        connect();
        String checkUserName = userName.getText();
        // String checkUserName = userName.getText();
        try {
            // Check in the signup table
            String customerQuery = "SELECT * FROM signup WHERE userName = ?";
            preparedStatement = connection.prepareStatement(customerQuery);
            preparedStatement.setString(1, checkUserName);
            ResultSet customerResult = preparedStatement.executeQuery();

            // Check in the staff table
            String staffQuery = "SELECT * FROM staff WHERE firstName = ?";
            preparedStatement = connection.prepareStatement(staffQuery);
            preparedStatement.setString(1, checkUserName);
            ResultSet staffResult = preparedStatement.executeQuery();

            if (customerResult.next()) {
            // Fetch user role from the signup table
            String userRole = customerResult.getString("roleBox");
            FXMLLoader loader;
            if ("Customer".equals(userRole)) {
                // Redirect to customer page
                loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
                Parent mainRoot = loader.load();
                Customer custController = loader.getController();
                custController.setUser(customerResult);
                goToPage.getChildren().setAll(mainRoot);
            } 
        } 
            if (staffResult.next()) {
                // Fetch user role from the staff table
                String userRole = staffResult.getString("Role");
                FXMLLoader loader;
                if ("Manager".equalsIgnoreCase(userRole)) {
                // Redirect to manager page
                loader = new FXMLLoader(getClass().getResource("../staff/StaffManagement.fxml"));
                Parent mainRoot = loader.load();
                //StaffManagementController managerController = loader.getController();
                //managerController.setUser(staffResult);
                goToPage.getChildren().setAll(mainRoot);
                } 
                if ("Chef".equalsIgnoreCase(userRole)) {
                    // Redirect to chef page
                    loader = new FXMLLoader(getClass().getResource("../staff/Chef.fxml"));
                    Parent mainRoot = loader.load();
                    ChefController controller = loader.getController();
                    controller.setUser(staffResult);
                    goToPage.getChildren().setAll(mainRoot);
                }
                if ("Delivery Driver".equalsIgnoreCase(userRole)) {
                    // Redirect to chef page
                    loader = new FXMLLoader(getClass().getResource("../staff/DeliveryDriver.fxml"));
                    Parent mainRoot = loader.load();
                    DeliveryDriverController deliverycontroller = loader.getController();
                    deliverycontroller.setUser(staffResult);
                    goToPage.getChildren().setAll(mainRoot);
                }
                if ("Waiter".equalsIgnoreCase(userRole)) {
                    // Redirect to chef page
                    loader = new FXMLLoader(getClass().getResource("../staff/Waiter.fxml"));
                    Parent mainRoot = loader.load();
                    Waiter waitercontroller = loader.getController();
                    waitercontroller.setUser(staffResult);
                    goToPage.getChildren().setAll(mainRoot);
                }
            } else {
                aUser.setText("User name does not exist");
                System.out.println("Username does not exist in the database.");
                userName.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onClick of create your account it will redirect you to SignUp Page
     */
    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../signUp/SignUp.fxml"));
            Parent loginRoot = loader.load();
            goToPage.getChildren().setAll(loginRoot);
            // contentPane.setPickOnBounds(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
