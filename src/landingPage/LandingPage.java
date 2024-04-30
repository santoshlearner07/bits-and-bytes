package landingPage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Label;

/**
 * Landing Page has two rendering pages Signup and Login based on user click specific page will get rendered
 */
public class LandingPage {
    @FXML
    private Label label;

    @FXML
    private StackPane contentPane;
/**
 * rendering signUp.fxml
 */
    @FXML
    private void signUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../signUp/SignUp.fxml"));
            Parent signUpRoot = loader.load();
            contentPane.getChildren().setAll(signUpRoot);
            // contentPane.setPickOnBounds(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/**
 * rendering Login.fxml
 */
    @FXML
    private void loginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent loginRoot = loader.load();
            contentPane.getChildren().setAll(loginRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
