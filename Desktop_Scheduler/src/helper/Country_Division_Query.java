package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Countries;
import model.Customers;
import model.Division_Totals;
import model.First_Level_Divisions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Country_Division_Query {

    /**
     * Observable list to store every division for a specific country
     * @param country
     * @return
     * @throws SQLException
     */
    public static ObservableList<String> initializeDivision(String country) throws SQLException {

        ObservableList<String> allFilteredDivisions = FXCollections.observableArrayList();
        String sql = "SELECT fld.Division, fld.Division_ID, c.Country, C.Country_ID FROM countries as c INNER JOIN " +
                " first_level_divisions AS fld ON c.Country_ID = fld.Country_ID WHERE c.Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            allFilteredDivisions.add(rs.getString("Division"));
        }

        return allFilteredDivisions;

    }

    /**
     * Observable list to store all divisions
     * @return
     * @throws SQLException
     */

    public static ObservableList<First_Level_Divisions> getAllDivisions() throws SQLException {
        ObservableList<First_Level_Divisions> allDivisions = FXCollections.observableArrayList();
        String sql = "SELECT Division_ID, Division, Country_ID, COUNT(*) FROM first_level_divisions GROUP BY Division";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int divisionID = rs.getInt("Division_ID");
            String division = rs.getString("Division");
            int countryID = rs.getInt("Country_ID");

            First_Level_Divisions first_level_division = new First_Level_Divisions(divisionID, division, countryID);

            allDivisions.add(first_level_division);
        }

        return allDivisions;
    }

    /**
     * Observable list to store distinct divisions and the total occurrences of each division
     * @return
     * @throws SQLException
     */

    public static ObservableList<Division_Totals> getDivisionTotals() throws SQLException {
        ObservableList<Division_Totals> divisionTotals = FXCollections.observableArrayList();
        String sql = "SELECT Division, COUNT(customers.Division_ID) as Total FROM first_level_divisions JOIN " +
                "Customers ON first_level_divisions.Division_ID = customers.Division_ID GROUP BY first_level_divisions.Division_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String division = rs.getString("Division");
            int total = rs.getInt("Total");

            Division_Totals division_Totals = new Division_Totals(division, total);

            divisionTotals.add(division_Totals);
        }

        return divisionTotals;
    }

    /**
     * Observable list to store all countries
     * @return
     * @throws SQLException
     */

    public static ObservableList<String> initializeCountry() throws SQLException {
        ObservableList<String> allCountries = FXCollections.observableArrayList();
        String sql = "SELECT Country FROM countries";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            allCountries.add(rs.getString("Country"));
        }

        return allCountries;
    }

    /**
     * Observable list to store division IDs from a given division name
     * @param division
     * @return
     * @throws SQLException
     */

    public static int getDivisionID(String division) throws SQLException {
        int divisionID = 0;
        String sql = "SELECT fld.Division, fld.Division_ID FROM first_level_divisions AS fld WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            divisionID = rs.getInt("Division_ID");
        }
        return divisionID;

    }

    /**
     * Observable list to store country IDs from a given country name
     * @param country
     * @return
     * @throws SQLException
     */

    public static int getCountryID(String country) throws SQLException {
        int countryID = 0;
        String sql = "SELECT c.Country, c.Country_ID FROM countries AS c WHERE Country = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, country);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            countryID = rs.getInt("Country_ID");
        }
        return countryID;
    }
}
