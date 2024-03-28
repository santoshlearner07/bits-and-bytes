package login;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
public class Login {
    @FXML
    private Label aUser;

    @FXML
    private TextField userName;

    // public void initialize() {
        // }
        

    @FXML  
    private void handleLogin(){
        aUser.setText("Hello User");
        String inputUserName = userName.getText(); 
        System.out.println("User Name: " + inputUserName);
    }
}
