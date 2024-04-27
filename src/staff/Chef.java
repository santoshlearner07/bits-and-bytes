package staff;

import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Chef {

    @FXML
    private Label userDataLogin;

    // Method to receive user data from Login controller
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        userDataBuilder.append("User Data:\n");
        userDataBuilder.append("First Name: ").append(userData.getString("firstName")).append("\n");
        userDataBuilder.append("Last Name: ").append(userData.getString("lastName")).append("\n");
        userDataBuilder.append("Email: ").append(userData.getString("userEmail")).append("\n");
        userDataBuilder.append("Role: ").append(userData.getString("roleBox")).append("\n");
        userDataLogin.setText(userDataBuilder.toString());
    }

    @FXML
    private void customerClick() {
        System.out.println("Clicked");
    }
}
