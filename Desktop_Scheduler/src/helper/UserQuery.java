package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class UserQuery {

    /**
     * Verifies that the username and password input match records in the database
     * @param userNameInput
     * @param passwordInput
     * @return
     * @throws SQLException
     */

    public static boolean loginAttempt(String userNameInput, String passwordInput) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, userNameInput);
        ps.setString(2, passwordInput);
        ResultSet rs = ps.executeQuery();
        if (rs.next()){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Observable list to store user IDs
     * @return
     * @throws SQLException
     */

    public static ObservableList<String> initializeUser() throws SQLException {
        ObservableList<String> allUsers = FXCollections.observableArrayList();
        String sql = "SELECT User_ID FROM users";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            allUsers.add(rs.getString("User_ID"));
        }

        return allUsers;
    }

    public static int userIDQuery(String username) throws SQLException {
        int user_ID = 0;
        String sql = "SELECT User_ID FROM users WHERE User_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            user_ID = rs.getInt("User_ID");
        }

        return user_ID;
    }
}
