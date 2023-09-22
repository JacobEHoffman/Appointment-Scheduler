package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Customers {
    private static ObservableList<Appointments> associatedAppointments = FXCollections.observableArrayList();
    private int customer_ID;
    private String customer_Name;
    private String address;
    private String postal_Code;
    private String phone;
    private int division_ID;
    private String division_Name;
    private String country;

    public Customers(int customer_ID, String customer_Name, String address, String postal_Code, String phone, int division_ID, String division_Name, String country){
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.address = address;
        this.postal_Code = postal_Code;
        this.phone = phone;
        this.division_ID = division_ID;
        this.division_Name = division_Name;
        this.country = country;
    }

    /**
     * returns the customer id
     * @return
     */
    public int getCustomer_ID() {
        return customer_ID;
    }

    /**
     * returns the customer name
     * @return
     */
    public String getCustomer_Name() {
        return customer_Name;
    }

    /**
     * returns the customer address
     * @return
     */
    public String getAddress() {
        return address;
    }

    /**
     * returns the customer postal code
     * @return
     */
    public String getPostal_Code() {
        return postal_Code;
    }

    /**
     * returns the customer phone number
     * @return
     */
    public String getPhone() {
        return phone;
    }

    /**
     * returns the customer division id
     * @return
     */
    public int getDivision_ID() {
        return division_ID;
    }

    /**
     * returns the customer division name
     * @return
     */
    public String getDivision_Name() {
        return division_Name;
    }

    /**
     * returns the customer country name
     * @return
     */
    public String getCountry() {
        return country;
    }

    /**
     * sets the customer id
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    /**
     * sets the customer name
     * @param customer_Name
     */
    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    /**
     * sets the customer address
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * sets the customer postal code
     * @param postal_Code
     */
    public void setPostal_Code(String postal_Code) {
        this.postal_Code = postal_Code;
    }

    /**
     * sets the customer phone number
     * @param phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * sets the customer division id
     * @param division_ID
     */
    public void setDivision_ID(int division_ID) {
        this.division_ID = division_ID;
    }

    /**
     * sets the customer division name
     * @param division_Name
     */
    public void setDivision_Name(String division_Name) {
        this.division_Name = division_Name;
    }

    /**
     * sets the customer country name
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }
}