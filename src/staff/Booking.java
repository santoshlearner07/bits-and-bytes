package staff;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Booking {

    private final SimpleStringProperty name;
    private final SimpleIntegerProperty tableSeat;
    private final SimpleStringProperty bookingDate;
    private final SimpleStringProperty bookingTime;
    private final SimpleStringProperty status;

    /**
     * Constructs a new Booking object.
     *
     * @param name         The name of the customer making the booking.
     * @param tableSeat    The number of table seats reserved for the booking.
     * @param bookingDate  The date of the booking.
     * @param bookingTime  The time of the booking.
     * @param status       The status of the booking.
     */

    public Booking(String name, int tableSeat, String bookingDate, String bookingTime, String status) {
        this.name = new SimpleStringProperty(name);
        this.tableSeat = new SimpleIntegerProperty(tableSeat);
        this.bookingDate = new SimpleStringProperty(bookingDate);
        this.bookingTime = new SimpleStringProperty(bookingTime);
        this.status = new SimpleStringProperty(status);
    }
    /**
     * Gets the name of the customer making the booking.
     *
     * @return The name of the customer.
     */
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
