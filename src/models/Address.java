package models;

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
    }

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

    public static Address createAddress(Connection conn, String firstLine, String city, String postCode) throws SQLException{
        PreparedStatement st = conn.prepareStatement("INSERT INTO Address (FirstLine,City,PostCode) VALUES (?,?,?)");
        st.setString(1, firstLine);
        st.setString(2, city);
        st.setString(3, postCode);
        st.executeUpdate();

        st = conn.prepareStatement("SELECT * FROM Address\n" +
                "WHERE AddressId = (SELECT MAX(AddressId) FROM Address);");
        ResultSet rs = st.executeQuery();
        rs.next();
        Address address = new Address(
                rs.getInt("AddressId"),
                rs.getString("FirstLine"),
                rs.getString("City"),
                rs.getString("PostCode")
        );
        return address;
    }

    public static Address getAddress(Connection conn, int id) throws SQLException {
        PreparedStatement st =conn.prepareStatement("SELECT * FROM Address WHERE AddressId = ?");
        st.setInt(1, id);
        ResultSet rs = st.executeQuery();
        if(rs.next()) {
            return new Address(
                    rs.getInt("AddressId"),
                    rs.getString("FirstLine"),
                    rs.getString("City"),
                    rs.getString("PostCode")
            );
        }
        return null;
    }

    public static void updateAddress(Connection conn, int id, String firstLine, String city, String postCode) throws SQLException {
        PreparedStatement st = conn.prepareStatement("UPDATE Address " +
                "SET FirstLine = ?, City = ?, PostCode = ?" +
                "WHERE AddressId = ?");
        st.setString(1, firstLine);
        st.setString(2, city);
        st.setString(3, postCode);
        st.setInt(4, id);
        st.executeUpdate();
    }
}