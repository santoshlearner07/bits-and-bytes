package login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import mainPage.MainPage;

public class Login {
    @FXML
    private Label aUser;

    @FXML
    private TextField userName;

    @FXML
    private StackPane goToMain;

    private final String databaseURL = "jdbc:mysql://localhost:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;

    private void connect() throws SQLException {
        connection = DriverManager.getConnection(databaseURL, username, password);
    }

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
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/MainPage.fxml"));
                Parent mainRoot = loader.load();
                MainPage mainPageController = loader.getController();
                mainPageController.setUser(resultSet); // Pass user data to MainPage controller
                goToMain.getChildren().setAll(mainRoot);
            } else {
                aUser.setText("User name does not exist");
                System.out.println("Username does not exist in the database.");
                userName.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../signUp/SignUp.fxml"));
            Parent loginRoot = loader.load();
            goToMain.getChildren().setAll(loginRoot);
            // contentPane.setPickOnBounds(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
