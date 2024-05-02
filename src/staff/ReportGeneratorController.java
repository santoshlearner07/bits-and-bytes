package staff;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class ReportGeneratorController {

    @FXML void generateReport() {
        // Fetch data from the database
        String mostPopularItem = fetchDataFromDatabase("SELECT item_name FROM orders GROUP BY item_name ORDER BY COUNT(*) DESC LIMIT 1");
        String busiestPeriod = fetchDataFromDatabase("SELECT CONCAT(tblBkDt, ' ', tblBkTime) AS busiest_period FROM tableBookingInfo GROUP BY tblBkDt, tblBkTime ORDER BY COUNT(*) DESC LIMIT 1");
        String mostActiveCustomer = fetchDataFromDatabase("SELECT custName FROM orders GROUP BY custName ORDER BY COUNT(*) DESC LIMIT 1");
        String staffWithHighestHoursWorked = fetchDataFromDatabase("SELECT CONCAT(FirstName, ' ', LastName) AS staff_with_highest_hours_worked FROM staff ORDER BY TotalHoursWorked DESC LIMIT 1");

        // Generate report data
        String reportContent = "Report\n\n"
                + "Most popular item: " + mostPopularItem + "\n"
                + "Busiest period in the restaurant: " + busiestPeriod + "\n"
                + "Most active customer: " + mostActiveCustomer + "\n"
                + "Staff member with highest total hours worked: " + staffWithHighestHoursWorked;

        // Save report to file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("report.txt");
        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(reportContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String fetchDataFromDatabase(String sqlQuery) {
        String result = "";
        String url = "jdbc:mysql://127.0.0.1:3306/cafe";
        String username = "root";
        String password = "san7@SQL";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                result = resultSet.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
