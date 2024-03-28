package signUp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;

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

    public void initialize() {
        label.setText("Welcome! Create your account.");
    }

    private void handleRole() {
        String selectedRole = roleBox.getValue();
        System.out.println("Selected Role: " + selectedRole);
    }

    @FXML
    private void handleInput() {
        String inputFirstName = firstName.getText();
        String inputLastName = lastName.getText();
        String inputUserName = userName.getText();
        String inputUserEmail = userEmail.getText();
        System.out.println("First Name: " + inputFirstName);
        System.out.println("Last Name: " + inputLastName);
        System.out.println("User Name: " + inputUserName);
        System.out.println("Email: " + inputUserEmail);
        handleRole();

        firstName.clear();
        lastName.clear();
        userName.clear();
        userEmail.clear();
        roleBox.setValue(null);
    }
}
