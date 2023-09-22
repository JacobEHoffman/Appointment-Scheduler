package model;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Appointments {

    private int appointment_ID;
    private String title;
    private String description;
    private String location;
    private String type;
    private Timestamp start;
    private Timestamp end;
    private int customer_ID;
    private int contact_ID;
    private String contact_Name;
    private int user_ID;


    public Appointments(int appointment_ID, String title, String description, String location, String type,
                        Timestamp start, Timestamp end, int customer_ID, int user_ID, int contact_ID, String contact_Name)
    {
        this.appointment_ID = appointment_ID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.start = start;
        this.end = end;
        this.customer_ID = customer_ID;
        this.contact_ID = contact_ID;
        this.contact_Name = contact_Name;
        this.user_ID = user_ID;

    }

    /**
     *  the appointment id
     * @return
     */
    public int getAppointment_ID() {
        return appointment_ID;
    }

    /**
     * sets the appointment id
     * @param appointment_ID
     */
    public void setAppointment_ID(int appointment_ID) {
        this.appointment_ID = appointment_ID;
    }

    /**
     * returns the appointment title
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * sets the appointment title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * returns the appointment description
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     * sets the appointment description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * returns the appointment location
     * @return
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the appointment location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * returns the appointment type
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * sets the appointment type
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * returns the appointment start time
     * @return
     */
    public Timestamp getStart() {
        return start;
    }

    /**
     * sets the appointment start time
     * @param start
     */
    public void setStart(Timestamp start) {
        this.start = start;
    }

    /**
     * returns the appointment end time
     * @return
     */
    public Timestamp getEnd() {
        return end;
    }

    /**
     * sets the appointment end time
     * @param end
     */
    public void setEnd(Timestamp end) {
        this.end = end;
    }

    /**
     * returns the customer id
     * @return
     */
    public int getCustomer_ID() {
        return customer_ID;
    }

    /**
     * sets the customer id
     * @param customer_ID
     */
    public void setCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }

    /**
     * returns the user id
     * @return
     */
    public int getUser_ID() { return user_ID; }

    /**
     * sets the user id
     * @param user_ID
     */
    public void setUser_ID(int user_ID) { this.user_ID = user_ID; }

    /**
     * returns the contact id
     * @return
     */
    public int getContact_ID() {
        return contact_ID;
    }

    /**
     * sets the contact id
     * @param contact_ID
     */
    public void setContact_ID(int contact_ID) {
        this.contact_ID = contact_ID;
    }

    /**
     * returns the contact name
     * @return
     */
    public String getContact_Name() { return contact_Name; }

    /**
     * sets the contact name
     * @param contact_Name
     */
    public void setContact_Name(String contact_Name) { this.contact_Name = contact_Name; }
}
