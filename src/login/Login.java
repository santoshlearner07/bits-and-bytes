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
import mainPage.MainPage;
import staff.StaffController;
import staff.Waiter;
import tableBooking.TableBooking;

public class Login {
    @FXML
    private Label aUser;

    @FXML
    private TextField userName;

    @FXML
    private StackPane goToPage;

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD);
    }
/**
 * handleLogin() fetching data from signup table based on userName
 * and based on there role there UI renders on screen for example, Manager, Customer, Waiter
 * if userName does not match on UI it will display as userName does not exist
 * @throws SQLException
 */
    @FXML
    private void handleLogin() throws SQLException {
        connect();
        String checkUserName = userName.getText();
        try {
            String userDataQuery = "SELECT * FROM signup WHERE userName = ?";
            preparedStatement = connection.prepareStatement(userDataQuery);
            preparedStatement.setString(1, checkUserName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String userRole = resultSet.getString("roleBox"); // Assuming the role column name is "roleBox"
                FXMLLoader loader;
                if ("Customer".equals(userRole)) {
                    loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
                    Parent mainRoot = loader.load();
                    Customer custController = loader.getController();
                    custController.setUser(resultSet);
                    goToPage.getChildren().setAll(mainRoot);
                }  else if ("Manager".equals(userRole)) {
                    loader = new FXMLLoader(getClass().getResource("../staff/Manager.fxml"));
                    Parent mainRoot = loader.load();
                    StaffController managerController = loader.getController();
                    managerController.setUser(resultSet);
                    goToPage.getChildren().setAll(mainRoot);
                } 
                else if ("Waiter".equals(userRole)) {
                    loader = new FXMLLoader(getClass().getResource("../staff/Waiter.fxml"));
                    Parent mainRoot = loader.load();
                    Waiter managerController = loader.getController();
                    managerController.setUser(resultSet);
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
