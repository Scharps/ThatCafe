package models;

import java.lang.UnsupportedOperationException;


public class Address {
    private int id;
    private String firstLine;
    private String city;
    private String postCode;

    private Address(int id, String firstLine, String city, String postCode){
        this.id = id;
        this.firstLine = firstLine;
        this.city = city;
        this.postCode = postCode;
    };

    public int getId() {
        return this.id;
    }

    public String getFirstLine() {
        return this.firstLine;
    }

    public String getCity() {
        return this.city;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public static Address createAddress(int id, String firstLine, String city, String postCode) {
        Address address = new Address(id, firstLine, city, postCode);
        return address;
        //throw new UnsupportedOperationException("createAddress() is not yet implemented");
    }

    public static Address getAddress(int id) {
        throw new UnsupportedOperationException("getAddress() is not yet implemented");
    }

    public static boolean deleteAddress(int id) {
        throw new UnsupportedOperationException("deleteAddress() is not yet implemented");
    }

    public static boolean updateAddress(int id, String firstLine, String city, String postCode) {
        throw new UnsupportedOperationException("updateAddress() is not yet implemented");
    }
}