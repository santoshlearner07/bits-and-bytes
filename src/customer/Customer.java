package customer;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
// import javafx.scene.layout.VBox;
import javafx.scene.layout.VBox;

public class Customer {

    @FXML
    private Label userDataLogin;

    @FXML
    private VBox displayTable;

    @FXML
    private StackPane orderPane;

    private ResultSet userData;

    // Method to receive user data from Login controller
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.userData = userData;
        // userDataBuilder.append("User Data:\n");
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        // userDataBuilder.append("Email:
        // ").append(userData.getString("userEmail")).append("\n");
        // userDataBuilder.append("Role:
        // ").append(userData.getString("roleBox")).append("\n");
        userDataLogin.setText(userDataBuilder.toString());
    }

    @FXML
    private void customerClick() {
        System.out.println("Clicked");
    }

    @FXML
    private void goToTable() throws SQLException {
        System.out.println(userData.getString("firstName"));
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../tableBooking/TableBooking.fxml"));
            Parent mainPageRoot = loader.load();
            displayTable.getChildren().setAll(mainPageRoot);
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
