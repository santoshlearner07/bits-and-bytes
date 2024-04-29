package mainPage;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainPage {

    @FXML
    private StackPane displayMultipleEvent;

    @FXML
    private Label userDataLabel;

    // Method to receive user data from Login controller
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        userDataBuilder.append("User Data:\n");
        userDataBuilder.append("First Name: ").append(userData.getString("firstName")).append("\n");
        userDataBuilder.append("Last Name: ").append(userData.getString("lastName")).append("\n");
        userDataBuilder.append("Email: ").append(userData.getString("address")).append("\n");
        userDataBuilder.append("Role: ").append(userData.getString("roleBox")).append("\n");
        userDataLabel.setText(userDataBuilder.toString());
    }

    @FXML
    private void goToTable() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../tableBooking/TableBooking.fxml"));
            Parent mainPageRoot = loader.load();
            displayMultipleEvent.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
