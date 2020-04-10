package models;

import java.lang.UnsupportedOperationException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


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

    public static void registerAddress(Connection conn, String firstLine, String city, String postCode) {
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO Address (FirstLine,City,PostCode) VALUES (?,?,?)");
            st.setString(1, firstLine);
            st.setString(2, city);
            st.setString(3, postCode);
            st.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
            System.out.println(se);
        }
    }

    public static int getAddressId(Connection conn, String firstLine, String city, String postCode) {
        try{
            PreparedStatement st = conn.prepareStatement("Select * from Address where FirstLine = ? && City = ? && PostCode = ? ");
            st.setString(1, firstLine);
            st.setString(2, city);
            st.setString(3, postCode);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                return rs.getInt(1);
            }
            else return 0;
        } catch (SQLException se){
            se.printStackTrace();
            System.out.println(se);
            return 0;
        }
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