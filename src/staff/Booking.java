package staff;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
/**
     * Constructs a new Booking object with the specified details.
     *
     * @param name         the name of the customer making the booking
     * @param tableSeat    the number of seats booked
     * @param bookingDate  the date of the booking
     * @param bookingTime  the time of the booking
     * @param status       the status of the booking
     */
public class Booking {

    private final SimpleStringProperty name;
    private final SimpleIntegerProperty tableSeat;
    private final SimpleStringProperty bookingDate;
    private final SimpleStringProperty bookingTime;
    private final SimpleStringProperty status;

    public Booking(String name, int tableSeat, String bookingDate, String bookingTime, String status) {
        this.name = new SimpleStringProperty(name);
        this.tableSeat = new SimpleIntegerProperty(tableSeat);
        this.bookingDate = new SimpleStringProperty(bookingDate);
        this.bookingTime = new SimpleStringProperty(bookingTime);
        this.status = new SimpleStringProperty(status);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public int getTableSeat() {
        return tableSeat.get();
    }

    public SimpleIntegerProperty tableSeatProperty() {
        return tableSeat;
    }

    public String getBookingDate() {
        return bookingDate.get();
    }

    public SimpleStringProperty bookingDateProperty() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime.get();
    }

    public SimpleStringProperty bookingTimeProperty() {
        return bookingTime;
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }
}
