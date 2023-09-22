package controller;

import helper.*;
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
import model.Customers;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.AppointmentQuery.getAllAppointments;

public class addAppointmentController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private DatePicker addADate;
    @FXML
    private ComboBox<String> addACustomer;
    @FXML
    private ComboBox<String> addAUser;
    @FXML
    private TextField addAAppointmentID;
    @FXML
    private TextField addATitle;
    @FXML
    private TextField addADescription;
    @FXML
    private TextField addALocation;
    @FXML
    private TextField addAType;
    @FXML
    private ComboBox<String> addStartH;
    @FXML
    private ComboBox<String> addEndH;
    @FXML
    private ComboBox<String> addEndS;
    @FXML
    private ComboBox<String> addEndM;
    @FXML
    private ComboBox<String> addStartS;
    @FXML
    private ComboBox<String> addStartM;
    @FXML
    private ComboBox<String> addAContact;
    @FXML
    private Button addAConfirm;
    @FXML
    private Button addAExit;

    ObservableList<String> hours = FXCollections.observableArrayList();
    ObservableList<String> minutes = FXCollections.observableArrayList();
    ObservableList<String> seconds = FXCollections.observableArrayList();

    /**
     * When clicked, validates inputs and checks with company standards. If correct, notifies user of local and UTC time
     * zones then adds appointment to database and returns to main menu.
     * Lambda informs user of successful appointment addition
     * @param actionEvent
     */

    public void onActionConfirm(ActionEvent actionEvent) {

        try {
            int customer_ID = Integer.parseInt(addACustomer.getValue());
            int user_ID = Integer.parseInt(addAUser.getValue());
            int appointment_ID = getAllAppointments().size()+1;
            String title = addATitle.getText();
            String description = addADescription.getText();
            String location = addALocation.getText();
            String contact = addAContact.getValue();
            String type = addAType.getText();


            LocalDate date = addADate.getValue();
            String hourS = addStartH.getValue();
            String minuteS = addStartM.getValue();
            String secondS = addStartS.getValue();

            String hourE = addEndH.getValue();
            String minuteE = addEndM.getValue();
            String secondE = addEndS.getValue();

            LocalDateTime startInput = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(hourS), Integer.parseInt(minuteS), Integer.parseInt(secondS));
            ZonedDateTime locZdtStart = ZonedDateTime.of(startInput, ZoneId.systemDefault());

            ZonedDateTime startInEST = locZdtStart.withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalDateTime startESTLocalDT = startInEST.toLocalDateTime();

            ZonedDateTime utcZdtStart = locZdtStart.withZoneSameInstant(ZoneId.of("UTC"));
            //LocalDateTime utcLDTStart = utcZdtStart.toLocalDateTime();

            LocalDateTime endInput = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(),
                    Integer.parseInt(hourE), Integer.parseInt(minuteE), Integer.parseInt(secondE));
            ZonedDateTime locZdtEnd = ZonedDateTime.of(endInput, ZoneId.systemDefault());

            ZonedDateTime endInEST = locZdtEnd.withZoneSameInstant(ZoneId.of("America/New_York"));
            LocalDateTime endESTLocalDT = endInEST.toLocalDateTime();

            ZonedDateTime utcZdtEnd = locZdtEnd.withZoneSameInstant(ZoneId.of("UTC"));
            //LocalDateTime utcLDTEnd = utcZdtEnd.toLocalDateTime();




            LocalDateTime startOfBusinessHours = LocalDateTime.of(addADate.getValue(), LocalTime.of(8,0,0));
            LocalDateTime endOfBusinessHours = LocalDateTime.of(addADate.getValue(), LocalTime.of(22,0,0));

            if(addADate.getValue() == null || addStartH.getValue() == null || addStartM.getValue() == null || addStartS.getValue() == null)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Appointment start date and time not filled in");
                alert.setContentText("Please use the calendar date picker to select a start date AND use the Hour, Minute, and Second dropdown to set hours, minutes, and seconds before confirming");
                alert.showAndWait();
                return;
            }
            else if(addADate.getValue() == null || addEndH.getValue() == null || addEndM.getValue() == null || addEndS.getValue() == null)
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

                        "ZoneOffset System Default: " + ZoneOffset.systemDefault()
                                + "\n\n"
                                + "Start Local Date and Time: " + customFormat.format(locZdtStart)
                                + "\n\n"
                                + "Start UTC Date and Time: " + customFormat.format(utcZdtStart)
                                + "\n\n"
                                + "End Local Date and Time: " + customFormat.format(locZdtEnd)
                                + "\n\n"
                                + "End UTC Date and Time: " + customFormat.format(utcZdtEnd)

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
                        System.out.println("Appointment Scheduled Successfully");
                        try {
                            AppointmentQuery.insert(title, description, location, type, Timestamp.valueOf(startInput), Timestamp.valueOf(endInput), customer_ID, user_ID, AppointmentQuery.getContactID(contact));
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
     * When clicked, warns user of loss of inputs. If user selects ok, returns to main menu, otherwise allows user to complete customer addition
     * @param actionEvent
     * @throws IOException
     */

    public void onActionExit(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values, do you want to continue?");
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
     * Send customer ID to populate in appointment form
     * @param selectedItem
     */
    public void sendCustomerInfo(Customers selectedItem) {
        addACustomer.setValue(String.valueOf(selectedItem.getCustomer_ID()));
    }


    /**
     * Initializes the add appointment menu, populating the hour, minute, and second comboboxes for appointment start and end times
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();
        try{
            addAContact.setItems(AppointmentQuery.initializeContact());
            addACustomer.setItems(AppointmentQuery.initializeCustomer());
            addAUser.setItems(UserQuery.initializeUser());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
        addStartH.setItems(hours);
        addStartM.setItems(minutes);
        addStartS.setItems(seconds);
        addEndH.setItems(hours);
        addEndM.setItems(minutes);
        addEndS.setItems(seconds);
    }


}
