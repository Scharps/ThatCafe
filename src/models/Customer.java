package models;

public class Customer extends User {

    private Address address;
    private String username;

    private Customer(int id, String username, String firstName, String lastName, Address address) {
        super(id, firstName, lastName);
        this.address = address;
        this.username = username;
    }

    public Address getAddress() {
        return this.address;
    }

    public String getUsername() { return this.username;}

    public static Customer createCustomer(String firstName, String lastName, Address address) {
        throw new UnsupportedOperationException("createCustomer() is not yet implemented");
    }

    public static Customer getCustomer(int id) {
        throw new UnsupportedOperationException("getCustomer() is not yet implemented");
    }

    public static boolean deleteCustomer(int id) {
        throw new UnsupportedOperationException("deleteCustomer() is not yet implemented");
    }

    public static boolean updateCustomer(int id, String firstName, String lastName, Address address) {
        throw new UnsupportedOperationException("updateCustomer() is not yet implemented");
    }
}