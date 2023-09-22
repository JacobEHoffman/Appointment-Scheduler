package helper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

/**
 * Helper class for appointment queries
 */
public abstract class AppointmentQuery {

    /**
     * Inserts appointment into the appointment table
     * @param Title
     * @param Description
     * @param Location
     * @param Type
     * @param Start
     * @param End
     * @param customerID
     * @param userID
     * @param ContactID
     * @return
     * @throws SQLException
     */
    public static void insert(String Title, String Description, String Location, String Type, Timestamp Start, Timestamp End, int customerID, int userID, int ContactID) throws SQLException {

        //ZonedDateTime utcZdtStart = Start.withZoneSameInstant(ZoneOffset.UTC);
        //ZonedDateTime utcZdtEnd = End.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");




        String sql = "INSERT INTO APPOINTMENTS(Title, Description, Location, Type, Start, End, Create_Date, Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Title);
        ps.setString(2, Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, Start);
        ps.setTimestamp(6, End);
        ps.setString(7, ZonedDateTime.now(ZoneOffset.UTC).format(customFormat));
        ps.setString(8, Users.getUser_Name());
        ps.setString(9, ZonedDateTime.now(ZoneOffset.UTC).format(customFormat));
        ps.setString(10, Users.getUser_Name());
        ps.setInt(11, customerID);
        ps.setInt(12, userID);
        ps.setInt(13, ContactID);
        ps.executeUpdate();
    }

    /**
     * Updates an appointment in the appointment table
     * @param Title
     * @param Description
     * @param Location
     * @param Type
     * @param Start
     * @param End
     * @param Customer_ID
     * @param User_ID
     * @param Contact
     * @param Appointment_ID
     * @return
     * @throws SQLException
     */
    public static void update(String Title, String Description, String Location, String Type, Timestamp Start, Timestamp End, int Customer_ID, int User_ID, int Contact, int Appointment_ID) throws SQLException {

        //ZonedDateTime utcZdtStart = Start.withZoneSameInstant(ZoneOffset.UTC);
        //ZonedDateTime utcZdtEnd = End.withZoneSameInstant(ZoneOffset.UTC);
        DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String sql = "UPDATE APPOINTMENTS SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?, Contact_ID = ? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, Title);
        ps.setString(2, Description);
        ps.setString(3, Location);
        ps.setString(4, Type);
        ps.setTimestamp(5, Start);
        ps.setTimestamp(6, End);
        ps.setTimestamp(7, Timestamp.valueOf(LocalDateTime.now()));
        ps.setString(8, Users.getUser_Name());
        ps.setInt(9, Customer_ID);
        ps.setInt(10, User_ID);
        ps.setInt(11, Contact);
        ps.setInt(12, Appointment_ID);
        ps.executeUpdate();

    }


    /**
     * Deletes an appointment from the appointments table using the Appointment ID
     * @param Appointment_ID
     * @return
     * @throws SQLException
     */
    public static void delete(int Appointment_ID) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Appointment_ID);
        ps.executeUpdate();

    }

    /**
     * Deletes an appointment from the appointments table using the Customer ID
     * @param Customer_ID
     * @throws SQLException
     */
    public static void cDelete(int Customer_ID) throws SQLException {
        String sql = "DELETE FROM APPOINTMENTS WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, Customer_ID);
        ps.executeUpdate();
    }

    /**
     * The observable list of all appointments
     * @return
     * @throws SQLException
     */

    public static ObservableList<Appointments> getAllAppointments() throws SQLException {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT * FROM appointments AS a LEFT JOIN contacts as c ON a.Contact_ID = c.Contact_ID";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");

            Appointments appointment = new Appointments(appointmentID, title, description, location, type, Timestamp.valueOf(start), Timestamp.valueOf(end), customerID, userID, contactID, contactName);

            allAppointments.add(appointment);
        }
        ps.close();
        return allAppointments;
    }


    /**
     * Observable list of all contact names
     * @return
     * @throws SQLException
     */

    public static ObservableList<String> initializeContact() throws SQLException {
        ObservableList<String> allContacts = FXCollections.observableArrayList();
        String sql = "SELECT Contact_Name FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            allContacts.add(rs.getString("Contact_Name"));
        }

        return allContacts;
    }

    /**
     * Observable list of all customer IDs
     * @return
     * @throws SQLException
     */

    public static ObservableList<String> initializeCustomer() throws SQLException {
        ObservableList<String> allCustomers = FXCollections.observableArrayList();
        String sql = "SELECT Customer_ID FROM customers";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            allCustomers.add(rs.getString("Customer_ID"));
        }

        return allCustomers;
    }

    /**
     * Return contact ID of a given contact name
     * @param contact
     * @return
     * @throws SQLException
     */

    public static int getContactID(String contact) throws SQLException {
        int contactID = 0;
        String sql = "SELECT Contact_Name, Contact_ID FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contact);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            contactID = rs.getInt("Contact_ID");
        }
        return contactID;

    }

    /**
     * Checks whether a given appointment overlaps with another existing appointment
     * @param customer_ID
     * @param ldtStart
     * @param ldtEnd
     * @return
     * @throws SQLException
     */

    public static boolean checkAppointmentOverlap(int customer_ID, LocalDateTime ldtStart, LocalDateTime ldtEnd) throws SQLException {
        for(Appointments appointments : getAllAppointments())
        {
            if (appointments.getCustomer_ID() == customer_ID && ldtStart.isAfter(appointments.getStart().toLocalDateTime())
                    && ldtStart.isBefore(appointments.getEnd().toLocalDateTime()))
            {
                return true;
            }
            else if(appointments.getCustomer_ID() == customer_ID && ldtEnd.isAfter(appointments.getStart().toLocalDateTime())
                    && ldtEnd.isBefore(appointments.getEnd().toLocalDateTime()))
            {
                return true;
            }
            else if (appointments.getCustomer_ID() == customer_ID && ldtEnd.isAfter(appointments.getStart().toLocalDateTime())
                    && ldtEnd.isBefore(appointments.getEnd().toLocalDateTime()))
            {
                return true;
            }
            else if (appointments.getCustomer_ID() == customer_ID && ldtStart.isBefore(appointments.getStart().toLocalDateTime())
                    && ldtEnd.isAfter(appointments.getEnd().toLocalDateTime()))
            {
                return true;
            }
            else if (appointments.getCustomer_ID() == customer_ID && (ldtStart.isEqual(appointments.getStart().toLocalDateTime())
                    || ldtEnd.isEqual(appointments.getEnd().toLocalDateTime())))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Observable list to store distinct types and the total occurrences of each type
     * @return
     * @throws SQLException
     */

    public static ObservableList<Type_Totals> getTypeTotals() throws SQLException {
        ObservableList<Type_Totals> typeTotals = FXCollections.observableArrayList();
        String sql = "SELECT Type, COUNT(Type) as Total FROM appointments GROUP BY Type";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            String type = rs.getString("Type");
            int total = rs.getInt("Total");

            Type_Totals type_Totals = new Type_Totals(type, total);

            typeTotals.add(type_Totals);
        }

        return typeTotals;
    }

    /**
     * Observable list to store distinct months and the total occurrences of each month. Also takes the integer that the month
     * is stored as and converts it to the string of month's name
     * @return
     * @throws SQLException
     */

    public static ObservableList<Month_Totals> getMonthTotals() throws SQLException {
        ObservableList<Month_Totals> monthTotals = FXCollections.observableArrayList();
        String sql = "SELECT MONTH(Start) as Month, COUNT(MONTH(Start)) as Total FROM appointments GROUP BY MONTH(Start)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        String monthName = null;

        while(rs.next()){
            String month = rs.getString("Month");
            int total = rs.getInt("Total");

            if(month.equals("1"))
            {
                monthName = "January";
            }
            else if(month.equals("2"))
            {
                monthName = "February";
            }
            else if(month.equals("3"))
            {
                monthName = "March";
            }
            else if(month.equals("4"))
            {
                monthName = "April";
            }
            else if(month.equals("5"))
            {
                monthName = "May";
            }
            else if(month.equals("6"))
            {
                monthName = "June";
            }
            else if(month.equals("7"))
            {
                monthName = "July";
            }
            else if(month.equals("8"))
            {
                monthName = "August";
            }
            else if(month.equals("9"))
            {
                monthName = "September";
            }
            else if(month.equals("10"))
            {
                monthName = "October";
            }
            else if(month.equals("11"))
            {
                monthName = "November";
            }
            else if(month.equals("12"))
            {
                monthName = "December";
            }

            Month_Totals month_Totals = new Month_Totals(monthName, total);

            monthTotals.add(month_Totals);
        }

        return monthTotals;
    }

    /**
     * Observable list of all appointments for a given contact
     * @param contactName
     * @return
     * @throws SQLException
     */

    public static ObservableList<Appointments> getAppointmentsByContact(String contactName) throws SQLException {
        ObservableList<Appointments> allAppointments = FXCollections.observableArrayList();
        String sql = "SELECT appt.Appointment_ID, appt.Title, appt.Description, appt.Location, appt.Type, appt.Start, appt.End, appt.Customer_ID, appt.User_ID, appt.Contact_ID, " +
                "cont.Contact_Name FROM appointments AS appt INNER JOIN contacts AS cont ON appt.Contact_ID = cont.Contact_ID WHERE cont.Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, contactName);
        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            int appointmentID = rs.getInt("Appointment_ID");
            String title = rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            LocalDateTime start = rs.getTimestamp("Start").toLocalDateTime();
            LocalDateTime end = rs.getTimestamp("End").toLocalDateTime();
            int customerID = rs.getInt("Customer_ID");
            int userID = rs.getInt("User_ID");
            int contactID = rs.getInt("Contact_ID");



            ZonedDateTime utcStart = ZonedDateTime.of(start, ZoneOffset.UTC);
            ZonedDateTime localzdtStart = utcStart.withZoneSameInstant(ZoneId.systemDefault());


            ZonedDateTime utcEnd = ZonedDateTime.of(end, ZoneOffset.UTC);
            ZonedDateTime localzdtEnd = utcEnd.withZoneSameInstant(ZoneId.systemDefault());

            Appointments appointment = new Appointments(appointmentID, title, description, location, type, Timestamp.valueOf(localzdtStart.toLocalDateTime()), Timestamp.valueOf(localzdtEnd.toLocalDateTime()), customerID, userID, contactID, contactName);

            allAppointments.add(appointment);
        }
        return allAppointments;
    }

}
