package model;

public class Type_Totals {
    private String type;
    private int total;

    public Type_Totals(String type, int total) {
        this.type = type;
        this.total = total;
    }

    /**
     * returns the type of appointment
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * sets the type of appointment
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * returns the total number of instances of each type
     * @return
     */
    public int getTotal() {
        return total;
    }

    /**
     * sets the total number of instances of each type
     * @param total
     */
    public void setTotal(int total) {
        this.total = total;
    }
}
