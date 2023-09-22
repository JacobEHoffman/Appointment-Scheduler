package model;

public class Countries {
    private int country_ID;
    private String country;

    public Countries(int country_ID, String country) {
        this.country_ID = country_ID;
        this.country = country;
    }

    /**
     * returns the country id
     * @return
     */
    public int getCountry_ID() {
        return country_ID;
    }

    /**
     * returns the country name
     * @return
     */
    public String getCountry() {
        return country;
    }
}
