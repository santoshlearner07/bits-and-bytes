package staff;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StaffMember {

    private final IntegerProperty id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final StringProperty role;
    private final IntegerProperty hoursToWork;
    private final IntegerProperty totalHoursWorked;
    
    /**
     * Constructs a new StaffMember object with the specified attributes.
     * @param id The ID of the staff member.
     * @param firstName The first name of the staff member.
     * @param lastName The last name of the staff member.
     * @param role The role of the staff member.
     * @param hoursToWork The number of hours the staff member is scheduled to work.
     * @param totalHoursWorked The total number of hours the staff member has worked.
     */
    public StaffMember(int id, String firstName, String lastName, String role, int hoursToWork, int totalHoursWorked) {
        this.id = new SimpleIntegerProperty(id);
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.role = new SimpleStringProperty(role);
        this.hoursToWork = new SimpleIntegerProperty(hoursToWork);
        this.totalHoursWorked = new SimpleIntegerProperty(totalHoursWorked);
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getRole() {
        return role.get();
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public int getHoursToWork() {
        return hoursToWork.get();
    }

    public IntegerProperty hoursToWorkProperty() {
        return hoursToWork;
    }

    public void setHoursToWork(int hoursToWork) {
        this.hoursToWork.set(hoursToWork);
    }

    public int getTotalHoursWorked() {
        return totalHoursWorked.get();
    }

    public IntegerProperty totalHoursWorkedProperty() {
        return totalHoursWorked;
    }

    public void setTotalHoursWorked(int totalHoursWorked) {
        this.totalHoursWorked.set(totalHoursWorked);
    }
}
