package order;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import customer.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * Menu Class handels the display menu, placing an order, going back to customer page and logging out  
 */
public class Menu implements Initializable {

    private final String DATABASE_URL = "jdbc:mysql://localhost:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String PENDING = "pending";

    private String firstName;
    @FXML
    private VBox menuItemsContainer;

    @FXML
    private StackPane orderPane;

    private ResultSet userData;

    @FXML
    private ListView orderListView;

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
     * 
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Load menu items from the menuItems array
        for (MenuItem menuItem : menuItems) {
            addItem(menuItem.getName(), menuItem.getPrice());
        }
    }

    @FXML
    private void addItem() {
        // Method to add a new item to the menu
        addItem("New Item", "$0");
    }

    /**
     * on click of button the item name and price will go to basket
     * @param name
     * @param price
     */
    private void addItem(String name, String price) {
        // Create a new menu item label
        Label menuItemLabel = new Label(name + " - " + price);
        menuItemLabel.setStyle("-fx-font-size: 16px;");
        menuItemLabel.setPrefWidth(250);
        // Create a button to add the item to the order list
        Button addButton = new Button("Add to Basket");
        addButton.setStyle(
                "-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 15px;-fx-cursor: hand;");
        addButton.setOnAction(event -> {
            addToBasket(name, price);
        });

        // Add the label and button to the menuItemsContainer
        HBox itemContainer = new HBox(menuItemLabel, addButton);
        itemContainer.setSpacing(5);
        menuItemsContainer.getChildren().add(itemContainer);
    }

    /**
     * Fetching all the user data based on UserName and data is coming from login page 
     * while routing happens it fetches the data and pass it down to other components
     * Method to receive user data from Login controller
     * @param userData
     * @throws SQLException
     */
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.firstName = userData.getString("firstName");
        this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
    }

    /**
     * the item that gets clicked will be added to the basket 
     * @param name
     * @param price
     */
    private void addToBasket(String name, String price) {
        // Add the selected item to the order list
        orderList.add(name + " - " + price);
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = "INSERT INTO orders (custName, item_name, item_price, status, foodPrepStatus) VALUES (?, ?, ?,?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, name);
                statement.setString(3, price);
                statement.setString(4, PENDING);
                statement.setString(5, PENDING);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Order item inserted into database.");
                    // Fetch updated order list from the database
                    fetchOrderListFromDatabase();
                } else {
                    System.out.println("Failed to insert order item into the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * fetching all the order from database based on customer Name/first Name and 
     * displaying it on the screen
     */
    private void fetchOrderListFromDatabase() {
        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USERNAME, PASSWORD)) {
            String sql = "SELECT * FROM orders WHERE custName = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                try (ResultSet resultSet = statement.executeQuery()) {
                    ObservableList<String> items = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        String itemName = resultSet.getString("item_name");
                        String itemPrice = resultSet.getString("item_price");
                        items.add(itemName + " - " + itemPrice);
                    }
                    orderListView.setItems(items);
                    System.out.println("Order list updated from the database.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * popup appears as the button is clicked and all the data is inserted in database 
     */
    @FXML
    private void placeOrder() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Order Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Order placed");
        alert.showAndWait();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../customer/Customer.fxml"));
            Parent mainPageRoot = loader.load();
            Customer custController = loader.getController();
            custController.setUser(userData);
            orderPane.getChildren().setAll(mainPageRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * if user wants to logout he can just click on a button and it will redirect user to the Login Page
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
        // try (Connection connection = DriverManager.getConnection(DATABASE_URL,
        // USERNAME, PASSWORD)) {
        // String sql = "DELETE FROM orders WHERE custName = ?";
        // try (PreparedStatement statement = connection.prepareStatement(sql)) {
        // statement.setString(1, firstName);
        // int rowsDeleted = statement.executeUpdate();
        // if (rowsDeleted >= 0) {
        // System.out.println("All orders deleted for customer: " + firstName);
        // // Clear the order list in the UI
        // orderListView.getItems().clear();
        // try {
        // FXMLLoader loader = new
        // FXMLLoader(getClass().getResource("../login/Login.fxml"));
        // Parent mainPageRoot = loader.load();
        // orderPane.getChildren().setAll(mainPageRoot);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // } else {
        // System.out.println("No orders found for customer: " + firstName);
        // }
        // }
        // } catch (SQLException e) {
        // e.printStackTrace();
        // }
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