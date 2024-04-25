package signUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUp {

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

    @FXML
    private StackPane signUpPane;

    private final String databaseURL = "jdbc:mysql://localhost:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    Alert alert = new Alert(AlertType.ERROR);

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
        if (checkIfUserExists(userName, userEmail)) {
            showAlert("Error", "Username or email already exists.");
            return;
        }
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

    public void initialize() {
        label.setText("Welcome! Create your account.");
    }

    @FXML
    private void handleInput() {
        String inputFirstName = firstName.getText();
        String inputLastName = lastName.getText();
        String inputUserName = userName.getText();
        String inputUserEmail = userEmail.getText();
        String selectedRole = roleBox.getValue();

        try {
            if (inputFirstName.length() >= 3 && inputLastName.length() >= 1 && inputUserName.length() >= 3
                    && inputUserEmail.length() >= 7 && roleBox.getValue() != null) {
                insertSignUpData(inputFirstName, inputLastName, inputUserName, inputUserEmail, selectedRole);
                showAlert("Added", "User Added");
                goToLogin();
            } else {
                showAlert("Error", "Please fill in all the fields.");
            }
        } catch (SQLException e) {
            showAlert("Error", "An error occurred while inserting data: " + e.getMessage());
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

    private void showAlert(String title, String message) {
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean checkIfUserExists(String userName, String userEmail) throws SQLException {
        connect();
        String query = "SELECT COUNT(*) AS count FROM signup WHERE userName = ? OR userEmail = ?";
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, userEmail);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // If count > 0, user exists
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }
}
