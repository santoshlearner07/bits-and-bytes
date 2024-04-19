package signUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SignUp {

    private final String databaseURL = "jdbc:mysql://localhost:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, username, password);
    }

    private void disconnect() throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    private void insertSignUpData(String firstName, String lastName, String userName, String userEmail, String roleBox)
            throws SQLException {
        connect();
        String insertQuery = "INSERT INTO signup(firstName,lastName,userName,userEmail,roleBox) VALUES (?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, userName);
            preparedStatement.setString(4, userEmail);
            preparedStatement.setString(5, roleBox);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data inserted");
            } else {
                System.out.println("Inserting data Failed");
            }
        } catch (SQLException e) {
            System.out.println("Error:- " + e.getMessage());
        } finally {
            disconnect();
        }
    }

    @FXML
    private Label label;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField userName;

    @FXML
    private TextField userEmail;

    @FXML
    private ComboBox<String> roleBox;

    public void initialize() {
        label.setText("Welcome! Create your account.");
    }

    @FXML
    private StackPane signUpPane;

    @FXML
    private void handleInput() {
        String inputFirstName = firstName.getText();
        String inputLastName = lastName.getText();
        String inputUserName = userName.getText();
        String inputUserEmail = userEmail.getText();
        String selectedRole = roleBox.getValue();
        System.out.println("First Name: " + inputFirstName);
        System.out.println("Last Name: " + inputLastName);
        System.out.println("User Name: " + inputUserName);
        System.out.println("Email: " + inputUserEmail);
        System.out.println("Selected Role: " + selectedRole);

        try {
            insertSignUpData(inputFirstName, inputLastName, inputUserName, inputUserEmail, selectedRole);
            System.out.println("Data inserted");
        } catch (SQLException e) {
            System.out.println("Error, inserting data:- " + e.getMessage());
        }

        firstName.clear();
        lastName.clear();
        userName.clear();
        userEmail.clear();
        roleBox.setValue(null);
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent loginRoot = loader.load();
            signUpPane.getChildren().setAll(loginRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
