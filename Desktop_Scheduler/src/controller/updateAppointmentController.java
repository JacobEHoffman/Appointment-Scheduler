package controller;

import helper.AppointmentQuery;
import helper.Country_Division_Query;
import helper.JDBC;
import helper.UserQuery;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controller controls the update appointment menu scene
 */

public class updateAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private DatePicker updateADate;
    @FXML
    private TextField updateAAppointmentID;
    @FXML
    private TextField updateATitle;
    @FXML
    private TextField updateADescription;
    @FXML
    private TextField updateALocation;
    @FXML
    private TextField updateAType;
    @FXML
    private ComboBox<String> updateAContact;
    @FXML
    private Button updateAConfirm;
    @FXML
    private Button updateAExit;
    @FXML
    private ComboBox<String> updateACustomer;
    @FXML
    private ComboBox<String> updateAUser;
    @FXML
    private ComboBox<String> updateStartH;
    @FXML
    private ComboBox<String> updateEndH;
    @FXML
    private ComboBox<String> updateEndS;
    @FXML
    private ComboBox<String> updateEndM;
    @FXML
    private ComboBox<String> updateStartS;
    @FXML
    private ComboBox<String> updateStartM;

    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    ObservableList<String> seconds = FXCollections.observableArrayList();

    /**
     * If confirm is clicked, check for input errors as well as company standards and if correct
     * notifies user of local and UTC time zones then updates appointment and return to main menu.
     * Lambda Expression informs user of successful appointment update.
     * @param actionEvent
     */

    public void onActionConfirm(ActionEvent actionEvent) {

        try {
            int customer_ID = Integer.parseInt(updateACustomer.getValue());
            int user_ID = Integer.parseInt(updateAUser.getValue());
            int appointment_ID = Integer.parseInt(updateAAppointmentID.getText());
            String title = updateATitle.getText();
            String description = updateADescription.getText();
            String location = updateALocation.getText();
            String contact = updateAContact.getValue();
            String type = updateAType.getText();


            LocalDate date = updateADate.getValue();
            String hourS = updateStartH.getValue();
            String minuteS = updateStartM.getValue();
            String secondS = updateStartS.getValue();

            String hourE = updateEndH.getValue();
            String minuteE = updateEndM.getValue();
            String secondE = updateEndS.getValue();

            LocalDateTime startInput = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(hourS), Integer.parseInt(minuteS), Integer.parseInt(secondS));
            ZonedDateTime locZdtStart = ZonedDateTime.of(startInput, ZoneId.systemDefault());

            ZonedDateTime startInEST = locZdtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalDateTime startESTLocalDT = startInEST.toLocalDateTime();

            ZonedDateTime utcZdtStart = locZdtStart.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime utcLDTStart = utcZdtStart.toLocalDateTime();

            LocalDateTime endInput = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(hourE), Integer.parseInt(minuteE), Integer.parseInt(secondE));
            ZonedDateTime locZdtEnd = ZonedDateTime.of(endInput, ZoneId.systemDefault());

            ZonedDateTime endInEST = locZdtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalDateTime endESTLocalDT = endInEST.toLocalDateTime();

            ZonedDateTime utcZdtEnd = locZdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
            LocalDateTime utcLDTEnd = utcZdtEnd.toLocalDateTime();




            LocalDateTime startOfBusinessHours = LocalDateTime.of(updateADate.getValue(), LocalTime.of(8,0,0));
            LocalDateTime endOfBusinessHours = LocalDateTime.of(updateADate.getValue(), LocalTime.of(22,0,0));

            if(updateADate.getValue() == null || updateStartH.getValue() == null || updateStartM.getValue() == null || updateStartS.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment start date and time not filled in");
                alert.setContentText("Please use the calendar date picker to select a start date AND use the Hour, Minute, and Second dropdown to set hours, minutes, and seconds before confirming");
                alert.showAndWait();
                return;
            }
            else if(updateADate.getValue() == null || updateEndH.getValue() == null || updateEndM.getValue() == null || updateEndS.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment end date and time not filled in");
                alert.setContentText("Please use the calendar date picker to select an end date AND use the Hour, Minute, and Second dropdown to set hours, minutes, and seconds before confirming");
                alert.showAndWait();
                return;
            } else if (startESTLocalDT.isBefore(startOfBusinessHours) || startESTLocalDT.isAfter(endOfBusinessHours) || endESTLocalDT.isBefore(startOfBusinessHours) || endESTLocalDT.isAfter(endOfBusinessHours))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment time is outside of regular business hours");
                alert.setContentText("Please select a proper date and time that are during business hours (M-F 8:00-22:00 EST)");
                alert.showAndWait();
                return;
            }
            else if (locZdtStart.isAfter(locZdtEnd))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment start time cannot be after appointment end time");
                alert.setContentText("Please select a proper date and time that are during business hours (8:00-22:00 EST)");
                alert.showAndWait();
                return;
            }
            else if(locZdtStart.isEqual(locZdtEnd))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment start time same as appointment end time");
                alert.setContentText("Please select a proper date and time that are during business hours (8:00-22:00 EST)");
                alert.showAndWait();
                return;
            }
            else if(date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment date is outside of regular business hours");
                alert.setContentText("Please select a proper date and time that are during business hours (M-F 8:00-22:00 EST)");
                alert.showAndWait();
                return;
            }
            else if(AppointmentQuery.checkAppointmentOverlap(customer_ID, startInput, endInput))
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment Time Overlaps with another existing appointment");
                alert.setContentText("Please select a proper date and time that are during business hours (M-F 8:00-22:00 EST)");
                alert.showAndWait();
                return;
            }
            else {


                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                DateTimeFormatter customFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                alert.setTitle("Time and Date");
                alert.setHeaderText(null);
                alert.setContentText(

                        "Locale.getDefault().toString:" + Locale.getDefault().toString()
                                + "\n\n"
                                + "ZoneOffset.systemDefault:" + ZoneOffset.systemDefault()
                                + "\n\n"
                                + "Start Local Date and Time:" + customFormat.format(locZdtStart)
                                + "\n\n"
                                + "Start UTC Date and Time:" + customFormat.format(utcZdtStart)
                                + "\n\n"
                                + "End Local Date and Time:" + customFormat.format(locZdtEnd)
                                + "\n\n"
                                + "End UTC Date and Time:" + customFormat.format(utcZdtEnd)

                         /*
                        "System default" +  ZoneOffset.systemDefault()
                                + "\n start input " + startInput
                                + "\n end input " + endInput
                                + "\n start local " + locZdtStart
                                +   "\n end local " + locZdtEnd
                                + "\n utc zoned start " + utcZdtStart
                                + "\n utc zoned end " + utcZdtEnd
                                + "\n utc ldt start " + utcLDTStart
                                + "\n utc ldt end " + utcLDTEnd
                                + "\n est start time " + startESTLocalDT
                                + "\n est end time " + endESTLocalDT

                          */
                );

                alert.showAndWait().ifPresent((response -> {
                            if (response ==ButtonType.OK) {
                                System.out.println("Appointment Updated and Scheduled Successfully");
                                try {
                                    AppointmentQuery.update(title, description, location, type, Timestamp.valueOf(startInput), Timestamp.valueOf(endInput), customer_ID, user_ID, AppointmentQuery.getContactID(contact), appointment_ID);
                                    stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                                    scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                                    stage.setScene(new Scene(scene));
                                    stage.show();
                                } catch (IOException | SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        })
                );

            }


        } catch(NumberFormatException e)
        {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }

        catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent check with user, then cancel appointment update and return to main menu
     * @throws IOException
     */

    public void onActionExit(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Your changes will not be saved, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK)
        {
            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     *
     * @param appointment send appointment information to be updated
     * @throws SQLException
     *
     *
     */

    public void sendAppointment(Appointments appointment) throws SQLException {
        hours.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23");
        minutes.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
                "54", "55", "56", "57", "58", "59");
        seconds.addAll("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11",
                "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25",
                "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39",
                "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
                "54", "55", "56", "57", "58", "59");


        updateAAppointmentID.setText(String.valueOf(appointment.getAppointment_ID()));
        updateAContact.setItems(AppointmentQuery.initializeContact());
        updateAContact.getSelectionModel().select(appointment.getContact_Name());
        updateACustomer.setItems(AppointmentQuery.initializeCustomer());
        updateACustomer.setValue(String.valueOf(appointment.getCustomer_ID()));
        updateAUser.setItems(UserQuery.initializeUser());
        updateAUser.setValue(String.valueOf(appointment.getUser_ID()));
        updateALocation.setText(appointment.getLocation());
        updateATitle.setText(appointment.getTitle());
        updateADescription.setText(appointment.getDescription());
        updateAType.setText(appointment.getType());
        updateADate.setValue(appointment.getStart().toLocalDateTime().toLocalDate());
        updateStartH.setItems(hours);
        updateStartH.setValue(appointment.getStart().toString().substring(11,13));
        updateStartM.setItems(minutes);
        updateStartM.setValue(appointment.getStart().toString().substring(14,16));
        updateStartS.setItems(seconds);
        updateStartS.setValue(appointment.getStart().toString().substring(17,19));
        updateEndH.setItems(hours);
        updateEndH.setValue(appointment.getEnd().toString().substring(11,13));
        updateEndM.setItems(minutes);
        updateEndM.setValue(appointment.getEnd().toString().substring(14,16));
        updateEndS.setItems(seconds);
        updateEndS.setValue(appointment.getEnd().toString().substring(17,19));
    }

    /**
     *
     * @param url
     * @param resourceBundle Initialize updateAppointmentController
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        JDBC.openConnection();
    }
}
