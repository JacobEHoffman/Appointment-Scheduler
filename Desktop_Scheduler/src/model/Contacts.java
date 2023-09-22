package model;

public class Contacts {
    private int contact_ID;
    private String contact_Name;
    private String email;

    public Contacts(int contact_ID, String contact_Name, String email) {
        this.contact_ID = contact_ID;
        this.contact_Name = contact_Name;
        this.email = email;
    }

    /**
     * returns the contact id
     * @return
     */
    public int getContact_ID() {
        return contact_ID;
    }

    /**
     * returns the contact name
     * @return
     */
    public String getContact_Name() {
        return contact_Name;
    }

    /**
     * returns the contact email
     * @return
     */
    public String getEmail() {
        return email;
    }
}
