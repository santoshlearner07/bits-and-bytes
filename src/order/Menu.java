package order;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import tableBooking.TableBooking;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import customer.Customer;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;

/**
 * Menu Class handles the display menu, placing an order, going back to customer
 * page and logging out
 */
public class Menu implements Initializable {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String PENDING = "pending";
    private final String INSERT_ORDER = "INSERT INTO orders (custName, item_name, item_price, status, foodPrepStatus, services, time) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private String firstName;
    @FXML
    private VBox menuItemsContainer;

    @FXML
    private StackPane orderPane;

    private ResultSet userData;

    @FXML
    private ListView<String> orderListView;

    /**
     * preDefined menu item and price
     */
    private MenuItem[] menuItems = {
            new MenuItem("Pizza", "£10"),
            new MenuItem("Burger", "£8"),
            new MenuItem("Salad", "£6"),
            new MenuItem("Pork", "£12"),
            new MenuItem("Fish", "£5"),
            new MenuItem("Ham Fish", "£20"),
            new MenuItem("Vegan Pizza", "£3"),
            new MenuItem("Veg Burger", "£4"),
            new MenuItem("Milk", "£2"),
            new MenuItem("Coffee", "£3"),
            new MenuItem("Apple Juice", "£6"),
            new MenuItem("Coke", "£1")
    };

    private List<String> orderList = new ArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (MenuItem menuItem : menuItems) {
            addItem(menuItem.getName(), menuItem.getPrice());
        }
    }

    // @FXML
    // private void addItem() {
    // // Method to add a new item to the menu
    // addItem("New Item", "$0");
    // }

    /**
     * on click of button the item name and price will go to basket
     * 
     * @param name
     * @param price
     */
    private void addItem(String name, String price) {
        Label menuItemLabel = new Label(name + " - " + price);
        menuItemLabel.setStyle("-fx-font-size: 16px;");
        menuItemLabel.setPrefWidth(250);
        Button addButton = new Button("Add to Basket");
        addButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 15px;-fx-cursor: hand;");
        addButton.setOnAction(event -> {
            String[] parts = menuItemLabel.getText().split(" - ");
            String itemName = parts[0];
            String itemPrice = parts[1];
            addToBasket(itemName, itemPrice);
        });

        HBox itemContainer = new HBox(menuItemLabel, addButton);
        itemContainer.setSpacing(5);
        menuItemsContainer.getChildren().add(itemContainer);
    }

    /**
     * Fetching all the user data based on UserName and data is coming from login
     * page while routing happens it fetches the data and pass it down to other
     * components Method to receive user data from Login controller
     * 
     * @param userData
     * @throws SQLException
     */
    public void setUser(ResultSet userData) throws SQLException {
        this.firstName = userData.getString("firstName");
        this.userData = userData;
    }

    /**
     * the item that gets clicked will be added to the basket
     * 
     * @param name
     * @param price
     */
    private void addToBasket(String name, String price) {
        orderList.add(name + " - " + price);
        updateOrderListView();
    }

    private void updateOrderListView() {
        ObservableList<String> items = FXCollections.observableArrayList(orderList);
        orderListView.setItems(items);
    }

    @FXML
    private void takeAway() {
        ChoiceDialog<String> timeDialog = new ChoiceDialog<>();
        timeDialog.setTitle("Select Takeaway Time");
        timeDialog.setHeaderText(null);
        timeDialog.setContentText("Select pickup time:");
    
        ObservableList<String> timeOptions = FXCollections.observableArrayList();
        for (int hour = 11; hour <= 23; hour++) {
            timeOptions.add(String.format("%02d:00", hour));
        }
        timeDialog.getItems().addAll(timeOptions);
    
        timeDialog.showAndWait().ifPresent(time -> {
            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
                String insertOrder = INSERT_ORDER;
                try (PreparedStatement statement = connection.prepareStatement(insertOrder)) {
                    for (String orderItem : orderList) {
                        String[] parts = orderItem.split(" - ");
                        statement.setString(1, firstName);
                        statement.setString(2, parts[0]);
                        statement.setString(3, parts[1]);
                        statement.setString(4, PENDING);
                        statement.setString(5, PENDING);
                        statement.setString(6, "Takeaway"); // Assuming there's a column for order type
                        statement.setString(7, time); // Insert the selected pickup time
                        statement.executeUpdate();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            orderList.clear();
    
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Order Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Order placed for takeaway at " + time);
            alert.showAndWait();
    
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
                Parent customerRoot = loader.load();
                Customer customerController = loader.getController();
                customerController.setUser(userData); // Pass user data to the customer controller if needed
                orderPane.getChildren().setAll(customerRoot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    

    /**
     * popup appears as the button is clicked and all the data is inserted in
     * database
     */
    @FXML
    private void placeOrder() {
        LocalTime currentTime = LocalTime.now();
        String timeText = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
                currentTime.getSecond());

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String insertOrder = INSERT_ORDER;
            try (PreparedStatement statement = connection.prepareStatement(insertOrder)) {
                for (String orderItem : orderList) {
                    String[] parts = orderItem.split(" - ");
                    statement.setString(1, firstName);
                    statement.setString(2, parts[0]);
                    statement.setString(3, parts[1]);
                    statement.setString(4, PENDING);
                    statement.setString(5, PENDING);
                    statement.setString(6, "Dine-In");
                    statement.setString(7, timeText);
                    statement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Order placed. Please select Table");
        alert.showAndWait();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../tableBooking/TableBooking.fxml"));
            Parent customerRoot = loader.load();
            TableBooking customerController = loader.getController();
            customerController.setUser(userData); // Pass user data to the customer controller if needed
            orderPane.getChildren().setAll(customerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * if user wants to logout he can just click on a button and it will redirect
     * user to the Login Page
     */
    @FXML
    private void logout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../login/Login.fxml"));
            Parent mainPageRoot = loader.load();
            orderPane.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * go back to customer page
     */
    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
            Parent customerRoot = loader.load();
            Customer customerController = loader.getController();
            customerController.setUser(userData); // Pass user data to the customer controller if needed
            orderPane.getChildren().setAll(customerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
