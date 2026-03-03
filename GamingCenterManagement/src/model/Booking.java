package model;

public class Booking {

    private int bookingId;
    private int customerId;
    private int gameId;
    private int hours;

    public Booking(int bookingId, int customerId, int gameId, int hours) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.gameId = gameId;
        this.hours = hours;
    }

    public int getBookingId() { return bookingId; }
    public int getCustomerId() { return customerId; }
    public int getGameId() { return gameId; }
    public int getHours() { return hours; }
}