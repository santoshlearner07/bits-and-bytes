package staff;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;

import java.sql.*;

public class StaffController {

    @FXML
    private Label statusLabel;

    @FXML
    private TableView<StaffMember> staffTableView;

    @FXML
    private TableColumn<StaffMember, Integer> idColumn;

    @FXML
    private TableColumn<StaffMember, String> firstNameColumn;

    @FXML
    private TableColumn<StaffMember, String> lastNameColumn;

    @FXML
    private TableColumn<StaffMember, String> roleColumn;

    @FXML
    private TableColumn<StaffMember, Integer> hoursToWorkColumn;

    @FXML
    private TableColumn<StaffMember, Integer> totalHoursWorkedColumn;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private TextField hoursToWorkField;

    @FXML
    private TextField totalHoursWorkedField;

    private final String databaseURL = "jdbc:mysql://127.0.0.1:3306/cafe";
    private final String username = "root";
    private final String password = "san7@SQL";

    @FXML
    private void initialize() {
        roleBox.getItems().addAll("Manager", "Delivery Driver", "Chef", "Waiter");

        // Binding cell value factories to the appropriate properties of StaffMember
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        roleColumn.setCellValueFactory(cellData -> cellData.getValue().roleProperty());
        hoursToWorkColumn.setCellValueFactory(cellData -> cellData.getValue().hoursToWorkProperty().asObject());
        totalHoursWorkedColumn.setCellValueFactory(cellData -> cellData.getValue().totalHoursWorkedProperty().asObject());

        
        staffTableView.setEditable(true);
        firstNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        lastNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        roleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        hoursToWorkColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        totalHoursWorkedColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        // Initially hide the TableView
        staffTableView.setVisible(false);

        // Load staff data
        loadStaffDataFromDatabase();

        //TableView selection changes
        staffTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                handleTableViewSelection();
            }
        });
    }

    public void setUser(ResultSet userData) throws SQLException {
        StringBuilder userDataBuilder = new StringBuilder();
        // this.userData = userData;
        userDataBuilder.append("Hello, ").append(userData.getString("firstName")).append(" ");
        userDataBuilder.append(userData.getString("lastName")).append("\n");
        // userDataLogin.setText(userDataBuilder.toString());
    }

    @FXML
private void handleAddStaff() {
    try {
        // Check if the staff member already exists
        if (staffExists(firstNameField.getText(), lastNameField.getText())) {
            statusLabel.setText("Staff member already exists.");
            return;
        }
        
        // Add new staff member to the database
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String insertQuery = "INSERT INTO Staff (FirstName, LastName, Role, HoursToWork, TotalHoursWorked) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
            preparedStatement.setString(1, firstNameField.getText());
            preparedStatement.setString(2, lastNameField.getText());
            preparedStatement.setString(3, roleBox.getValue());
            preparedStatement.setInt(4, Integer.parseInt(hoursToWorkField.getText()));
            preparedStatement.setInt(5, Integer.parseInt(totalHoursWorkedField.getText()));

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                statusLabel.setText("Staff member added successfully.");
                // Reload staff data after adding
                loadStaffDataFromDatabase();

            } else {
                statusLabel.setText("Failed to add staff member.");
            }
        }
    } catch (SQLException e) {
        statusLabel.setText("Error: " + e.getMessage());
    } catch (NumberFormatException e) {
        statusLabel.setText("Please enter staff details");
    }
}

private boolean staffExists(String firstName, String lastName) throws SQLException {
    try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
        String selectQuery = "SELECT COUNT(*) AS count FROM Staff WHERE FirstName=? AND LastName=?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
        preparedStatement.setString(1, firstName);
        preparedStatement.setString(2, lastName);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        int count = resultSet.getInt("count");
        return count > 0;
    }
}


    @FXML
    private void handleModifyStaff() {
        // Check if a staff member is selected in the TableView
        StaffMember selectedStaff = staffTableView.getSelectionModel().getSelectedItem();
        if (selectedStaff == null) {
            statusLabel.setText("Please select a staff member to modify.");
            return;
        }

        // Get the selected staff member's ID
        int selectedStaffId = selectedStaff.getId();

        // Get the modified details from the input fields
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String role = roleBox.getValue();

        // Validate input for hoursToWorkField and totalHoursWorkedField
        int hoursToWork;
        try {
            hoursToWork = Integer.parseInt(hoursToWorkField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid integer values for Hours to Work.");
            return;
        }

        int totalHoursWorked;
        try {
            totalHoursWorked = Integer.parseInt(totalHoursWorkedField.getText());
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter valid integer values for Total Hours Worked.");
            return;
        }

        try {
            // Update the staff member's details in the database
            try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
                String updateQuery = "UPDATE Staff SET FirstName=?, LastName=?, Role=?, HoursToWork=?, TotalHoursWorked=? WHERE ID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
                preparedStatement.setString(1, firstName);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, role);
                preparedStatement.setInt(4, hoursToWork);
                preparedStatement.setInt(5, totalHoursWorked);
                preparedStatement.setInt(6, selectedStaffId);

                // Execute the update
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    statusLabel.setText("Staff member modified successfully.");
                    // Reload staff data after modification
                    loadStaffDataFromDatabase();
                } else {
                    statusLabel.setText("Failed to modify staff member.");
                }
            }
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteStaff() {
        // Check if a staff member is selected in the TableView
        StaffMember selectedStaff = staffTableView.getSelectionModel().getSelectedItem();
        if (selectedStaff == null) {
            statusLabel.setText("Please select a staff member to delete.");
            return;
        }

        // Get the selected staff member's ID
        int selectedStaffId = selectedStaff.getId();

        try {
            // Delete the staff member from the database
            try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
                String deleteQuery = "DELETE FROM Staff WHERE ID=?";
                PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery);
                preparedStatement.setInt(1, selectedStaffId);

                // Execute the deletion
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    statusLabel.setText("Staff member deleted successfully.");
                    // Reload staff data after deletion
                    loadStaffDataFromDatabase();
                } else {
                    statusLabel.setText("Failed to delete staff member.");
                }
            }
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }

    @FXML
    private void handleViewStaff() {
        // Toggle visibility of TableView
        staffTableView.setVisible(!staffTableView.isVisible());

        // If TableView is visible, load staff data from the database
        if (staffTableView.isVisible()) {
            loadStaffDataFromDatabase();
        }
    }

    private void handleTableViewSelection() {
        // Get the selected staff member
        StaffMember selectedStaff = staffTableView.getSelectionModel().getSelectedItem();
        if (selectedStaff != null) {
            // Populate input fields with selected staff member's data
            firstNameField.setText(selectedStaff.getFirstName());
            lastNameField.setText(selectedStaff.getLastName());
            roleBox.setValue(selectedStaff.getRole());
            hoursToWorkField.setText(String.valueOf(selectedStaff.getHoursToWork()));
            totalHoursWorkedField.setText(String.valueOf(selectedStaff.getTotalHoursWorked()));
        }
    }

    private void loadStaffDataFromDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseURL, username, password)) {
            String selectQuery = "SELECT * FROM Staff";
            PreparedStatement preparedStatement = connection.prepareStatement(selectQuery);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<StaffMember> staffList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String firstName = resultSet.getString("FirstName");
                String lastName = resultSet.getString("LastName");
                String role = resultSet.getString("Role");
                int hoursToWork = resultSet.getInt("HoursToWork");
                int totalHoursWorked = resultSet.getInt("TotalHoursWorked");
                StaffMember staffMember = new StaffMember(id, firstName, lastName, role, hoursToWork, totalHoursWorked);
                staffList.add(staffMember);
            }
            staffTableView.setItems(staffList);
        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
        }
    }
}
