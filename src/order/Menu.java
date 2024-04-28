package order;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Menu implements Initializable {

    private final String databaseURL = "jdbc:mysql://localhost:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    private String firstName;
    @FXML
    private VBox menuItemsContainer;

    // @SuppressWarnings("rawtypes")
    @FXML
    private ListView orderListView;

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

    private void addItem(String name, String price) {
        // Create a new menu item label
        Label menuItemLabel = new Label(name + " - " + price);

        // Create a button to add the item to the order list
        Button addButton = new Button("Add to Basket");
        addButton.setOnAction(event -> {
            addToOrder(name, price);
        });

        // Add the label and button to the menuItemsContainer
        HBox itemContainer = new HBox(menuItemLabel, addButton);
        itemContainer.setSpacing(5);
        menuItemsContainer.getChildren().add(itemContainer);
    }

    // Method to receive user data from Login controller
    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        this.firstName = userData.getString("firstName");
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
    }

    private void addToOrder(String name, String price) {
        // Add the selected item to the order list
        orderList.add(name + " - " + price);
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String sql = "INSERT INTO orders (custName, item_name, item_price) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, firstName);
                statement.setString(2, name);
                statement.setString(3, price);
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

    private void fetchOrderListFromDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
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
}