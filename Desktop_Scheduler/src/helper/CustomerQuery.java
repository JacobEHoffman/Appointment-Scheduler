package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customers;
import model.Users;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public abstract class CustomerQuery {


    /**
     * Inserts customer into the customer table
     * @param Customer_Name
     * @param Address
     * @param Postal_Code
     * @param Phone
     * @param Division_ID
     * @return
     * @throws SQLException
     */
    public static int insert(String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID) throws SQLException {

        String sql = "INSERT INTO CUSTOMERS(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Customer_Name);
        ps.setString(2, Address);
        ps.setString(3, Postal_Code);
        ps.setString(4, Phone);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, Users.getUser_Name());
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, Users.getUser_Name());
        ps.setInt(9, Division_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Updates customer in the customer table
     * @param Customer_Name
     * @param Address
     * @param Postal_Code
     * @param Phone
     * @param Division_ID
     * @param Customer_ID
     * @return
     * @throws SQLException
     */

    public static int update(String Customer_Name, String Address, String Postal_Code, String Phone, int Division_ID, int Customer_ID) throws SQLException {

        String sql = "UPDATE CUSTOMERS SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Customer_Name);
        ps.setString(2, Address);
        ps.setString(3, Postal_Code);
        ps.setString(4, Phone);
        ps.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(6, Users.getUser_Name());
        ps.setInt(7, Division_ID);
        ps.setInt(8, Customer_ID);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Deletes a customer from the customer table
     * @param Customer_ID
     * @return
     * @throws SQLException
     */

    public static void delete(int Customer_ID) throws SQLException {
        String sql = "DELETE FROM CUSTOMERS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Customer_ID);

        try{
            ps.executeUpdate();
            ps.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    /**
     * Select all customers from the customer table
     * @throws SQLException
     */

    public static void select() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            int customer_ID = rs.getInt("Customer_ID");
            String customer_Name = rs.getString("Customer_Name");
            System.out.print(customer_ID + " | ");
            System.out.print(customer_Name + "\n");
        }

    }

    /**
     * The observable list of all customers
     * @return
     * @throws SQLException
     */

    public static ObservableList<Customers> getAllCustomers() throws SQLException {

        ObservableList<Customers> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT cust.Customer_ID, cust.Customer_Name, cust.Address, cust.Postal_Code, cust.Phone, " +
                "cust.Division_ID, fld.Division, fld.Country_ID, co.Country FROM customers AS cust INNER JOIN " +
                "first_level_divisions AS fld ON cust.Division_ID = fld.Division_ID INNER JOIN countries AS co ON fld.Country_ID = co.Country_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int customerID = rs.getInt("Customer_ID");
            String customerName = rs.getString("Customer_Name");
            String address= rs.getString("Address");
            String postal_Code= rs.getString("Postal_Code");
            String phone= rs.getString("Phone");
            int division_ID = rs.getInt("Division_ID");
            String division_Name = rs.getString("Division");
            String country = rs.getString("Country");

            Customers customer = new Customers(customerID, customerName, address, postal_Code, phone, division_ID, division_Name, country);

            allCustomers.add(customer);
        }
        return allCustomers;
    }

}
