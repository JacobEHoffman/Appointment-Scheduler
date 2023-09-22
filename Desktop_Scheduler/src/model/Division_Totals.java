package model;

public class Division_Totals {
    private String division;
    private int total;

    public Division_Totals(String division, int total) {
        this.division = division;
        this.total = total;
    }

    /**
     * returns the division
     * @return
     */
    public String getDivision() {
        return division;
    }

    /**
     * sets the division
     * @param division
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * returns the total number of instances of each division
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * sets the total number of instances of each division
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
