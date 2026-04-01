package model;

public class Customer extends Person {

    private String phone;

    public Customer() {}

    public Customer(int id, String name, String phone) {
        super(id, name);   // calling parent constructor
        this.phone = phone;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}