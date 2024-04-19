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
        aUser.setText("Hello User");
        String checkUserName = userName.getText();
        try {
            String userNameCheckQuery = "SELECT COUNT(*) AS email_count FROM signup WHERE userName = ?";
            preparedStatement = connection.prepareStatement(userNameCheckQuery);
            preparedStatement.setString(1, checkUserName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int emailCount = resultSet.getInt("email_count");
                if (emailCount > 0) {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../mainPage/MainPage.fxml"));
                    Parent mainRoot = loader.load();
                    goToMain.getChildren().setAll(mainRoot);
                } else {
                    aUser.setText("User name does not exist");
                    System.out.println("Email does not exist in the database.");
                    userName.clear();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void goToSignUp() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../signUp/SignUp.fxml"));
            Parent signUpRoot = loader.load();
            goToMain.getChildren().setAll(signUpRoot);
            // contentPane.setPickOnBounds(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
