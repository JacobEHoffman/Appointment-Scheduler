package model;

public class Month_Totals {
    private String month;
    private int total;

    public Month_Totals(String month, int total) {
        this.month = month;
        this.total = total;
    }

    /**
     * returns the month
     * @return
     */
    public String getMonth() {
        return month;
    }

    /**
     * sets the month
     * @param month
     */
    public void setMonth(String month) {
        this.month = month;
    }

    /**
     * returns the total number of instances for each month
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * sets the total number of instances for each month
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
