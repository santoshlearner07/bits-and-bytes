package staff;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 * Menu Class handles the display menu, placing an order, going back to customer
 * page and logging out
 */
public class WaiterOrder implements Initializable {

    private final String DATABASE_URL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String USERNAME = "root";
    private final String PASSWORD = "san7@SQL";
    private final String PENDING = "pending";
    private final String INSERT_ORDER = "INSERT INTO orders (custName, item_name, item_price, status, foodPrepStatus, services, time,orderDate, tableNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private String firstName;
    @FXML
    private VBox menuItemsContainer;

    @FXML
    private StackPane goToPage;

    private ResultSet userData;

    @FXML
    private ListView<String> orderListView;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<String> orderList = new ArrayList();

    /**
     * preDefined menu item and price
     */
    private WaiterOrderItem[] menuItems = {
            new WaiterOrderItem("Pizza", "£10"),
            new WaiterOrderItem("Burger", "£8"),
            new WaiterOrderItem("Salad", "£6"),
            new WaiterOrderItem("Pork", "£12"),
            new WaiterOrderItem("Fish", "£5"),
            new WaiterOrderItem("Ham Fish", "£20"),
            new WaiterOrderItem("Vegan Pizza", "£3"),
            new WaiterOrderItem("Veg Burger", "£4"),
            new WaiterOrderItem("Milk", "£2"),
            new WaiterOrderItem("Coffee", "£3"),
            new WaiterOrderItem("Apple Juice", "£6"),
            new WaiterOrderItem("Coke", "£1")
    };

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (WaiterOrderItem menuItem : menuItems) {
            addItem(menuItem.getName(), menuItem.getPrice());
        }

        WaiterOrderItem todaysSpecial = getTodaysSpecial();
        if (todaysSpecial != null) {
            addItem(todaysSpecial.getName(), todaysSpecial.getPrice());
        }
    }

    /**
     * today's special based on the day of the week
     * 
     * @return
     */
    private WaiterOrderItem getTodaysSpecial() {

        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        switch (dayOfWeek) {
            case MONDAY:
                return new WaiterOrderItem("Today's special: Pongal", "£10");
            case TUESDAY:
                return new WaiterOrderItem("Today's special: Dosa", "£9");
            case WEDNESDAY:
                return new WaiterOrderItem("Today's special: Butter Chicken", "£8");
            case THURSDAY:
                return new WaiterOrderItem("Today's special: Bread halwa", "£7");
            case FRIDAY:
                return new WaiterOrderItem("Today's special: Gulab jamun", "£12");
            case SATURDAY:
                return new WaiterOrderItem("Today's special: Fish fry", "£11");
            case SUNDAY:
                return new WaiterOrderItem("Today's special: Vegetable rice", "£15");
            default:
                return null;
        }
    }

    /**
     * Adding Item to Basket with itemName and itemPrice
     * 
     * @param name
     * @param price
     */
    @FXML
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
     * Fetching all the user data based on UserName and data is coming from login
     * page while routing happens it fetches the data and pass it down to other
     * components Method to receive user data from Login controller
     * 
     * @param userData
     * @throws SQLException
     */
    // public void setUser(ResultSet userData) throws SQLException {
    //     this.firstName = userData.getString("firstName");
    //     this.userData = userData;
    // }

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

    /**
     * once order added to Basket updateting the basket
     */
    private void updateOrderListView() {
        ObservableList<String> items = FXCollections.observableArrayList(orderList);
        orderListView.setItems(items);
    }

    // /**
    //  * alert box for showing any alert or message
    //  * 
    //  * @param title
    //  * @param content
    //  */
    // private void showErrorAlert(String title, String content) {
    //     Alert alert = new Alert(AlertType.ERROR);
    //     alert.setTitle(title);
    //     alert.setHeaderText(null);
    //     alert.setContentText(content);
    //     alert.showAndWait();
    // }

    /**
     * if basket is empty user cannot place a place an order
     * and if basket is not empty after placing an order it will redirect to table
     * UI where he has to select table.
     */
    @FXML
    private void placeOrder() {
        if (orderList.size() == 0) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("No Order");
            alert.setHeaderText(null);
            alert.setContentText("Please Place an Order.");
            alert.showAndWait();
        } else {
            // Show a choice dialog for selecting the table
            ChoiceDialog<Integer> tableDialog = new ChoiceDialog<>();
            tableDialog.setTitle("Select Table");
            tableDialog.setHeaderText(null);
            tableDialog.setContentText("Select table:");

            // Populate the choice dialog with table numbers from 1 to 11
            ObservableList<Integer> tableOptions = FXCollections.observableArrayList();
            for (int i = 1; i <= 11; i++) {
                tableOptions.add(i);
            }
            tableDialog.getItems().addAll(tableOptions);

            // Show the dialog and wait for user input
            tableDialog.showAndWait().ifPresent(tableNumber -> {
                // Proceed with inserting the order into the database
                LocalTime currentTime = LocalTime.now();
                String timeText = String.format("%02d:%02d:%02d", currentTime.getHour(), currentTime.getMinute(),
                        currentTime.getSecond());

                String orderDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

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
                            statement.setString(8, orderDate);
                            statement.setInt(9, tableNumber); // Insert the selected table number
                            statement.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Order Confirmation");
                alert.setHeaderText(null);
                alert.setContentText("Order placed. Table " + tableNumber + " selected.");
                alert.showAndWait();

                // Redirect the user back to the table booking page or any other appropriate
                // page
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../staff/Waiter.fxml"));
                    Parent customerRoot = loader.load();
                    // TableBooking customerController = loader.getController();
                    // customerController.setUser(userData);
                    goToPage.getChildren().setAll(customerRoot);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
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
            goToPage.getChildren().setAll(mainPageRoot);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../staff/Waiter.fxml"));
            Parent customerRoot = loader.load();
            // Waiter customerController = loader.getController();
            // customerController.setUser(userData); // Pass user data to the customer controller if needed
            goToPage.getChildren().setAll(customerRoot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}