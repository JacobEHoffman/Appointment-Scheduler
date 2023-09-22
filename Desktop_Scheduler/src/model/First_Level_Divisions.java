package model;

public class First_Level_Divisions {
    private int division_ID;
    private String division;
    private int country_ID;



    public First_Level_Divisions(int division_ID, String division, int country_ID) {
        this.division_ID = division_ID;
        this.division = division;
        this.country_ID = country_ID;
    }

    /**
     * returns the division id
     * @return
     */
    public int getDivision_ID() {
        return division_ID;
    }

    /**
     * returns the division name
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     * returns the country id
     * @return
     */
    public int getCountry_ID() {
        return country_ID;
    }


}


