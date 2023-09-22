package model;

public class Users {
    private static int user_ID;
    private static String user_Name;
    private String password;

    public Users(int user_ID, String user_Name, String password) {
        this.user_ID = user_ID;
        this.user_Name = user_Name;
        this.password = password;
    }

    /**
     * return the user id
     * @return
     */
    public static int getUser_ID() {
        return user_ID;
    }

    /**
     * return the username
     * @return
     */
    public static String getUser_Name() {
        return user_Name;
    }

    /**
     * return the user password
     * @return
     */
    public String getPassword() {
        return password;
    }
}
