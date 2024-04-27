package hellofx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../landingPage/LandingPage.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("../customer/Customer.fxml"));
        primaryStage.setTitle("Bits and Bytes");
        primaryStage.setScene(new Scene(root, 1000, 720)); // width,height
        primaryStage.show();
        primaryStage.getScene().getRoot().requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
