package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This represents an address owned by a customer.
 * @author Sam James *
 */

public class Address {
    private final int id;
    private final String firstLine;
    private final String city;
    private final String postCode;

    private Address(int id, String firstLine, String city, String postCode){
        this.id = id;
        this.firstLine = firstLine;
        this.city = city;
        this.postCode = postCode;
    }

    /**
     * Get the address ID
     * @return The address ID
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the first line of the address
     * @return The first line of the address
     */
    public String getFirstLine() {
        return this.firstLine;
    }

    /**
     * Get the city of the address
     * @return The city
     */
    public String getCity() {
        return this.city;
    }

    /**
     * Get the post code of the address
     * @return The post code of the address
     */
    public String getPostCode() {
        return this.postCode;
    }

    /**
     * Creates an address entry in the database and returns the created entry as an object.
     * @param conn The database connection
     * @param firstLine The first line of the address
     * @param city The address city
     * @param postCode The address postcode
     * @return The created Address object
     * @throws SQLException
     */
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

    /**
     * Gets an address object by the address ID.
     * @param conn The database connection
     * @param id The Address ID
     * @return An address object that corresponds to the particular Id
     * @throws SQLException
     */
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

    /**
     * Updates an address entry in the database.
     * @param conn The database connection
     * @param id The ID of the address.
     * @param firstLine The first line of the address
     * @param city The address city
     * @param postCode The address post code
     * @throws SQLException
     */
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