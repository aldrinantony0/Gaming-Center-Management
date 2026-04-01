package model;

public class Payment {

    private int paymentId;
    private int bookingId;
    private double amount;

    public Payment(int paymentId, int bookingId, double amount) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amount = amount;
    }

    public int getPaymentId() { return paymentId; }
    public int getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
}