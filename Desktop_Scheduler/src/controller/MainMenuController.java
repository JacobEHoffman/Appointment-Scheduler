package controller;

import helper.AppointmentQuery;
import helper.CustomerQuery;
import helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointments;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainMenuController implements Initializable {

    public RadioButton viewAllBtn;
    public RadioButton viewByMonth;
    public RadioButton viewByWeek;
    public Button reportsBtn;
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customers> customerTableView;
    @FXML
    private TableColumn<Customers, Integer> cIDCol;
    @FXML
    private TableColumn<Customers, String> cNameCol;
    @FXML
    private TableColumn<Customers, String> cAddressCol;
    @FXML
    private TableColumn<Customers, String> CPostalCol;
    @FXML
    private TableColumn<Customers, String> cPhoneCol;
    @FXML
    private TableColumn<Customers, Integer> cDivisionIDCol;
    @FXML
    private TableView<Appointments> appointmentsTableView;
    @FXML
    private TableColumn<Appointments, Integer> aIDCol;
    @FXML
    private TableColumn<Appointments, String> aTitleCol;
    @FXML
    private TableColumn<Appointments, String> aDescriptionCol;
    @FXML
    private TableColumn<Appointments, String> aLocationCol;
    @FXML
    private TableColumn<Appointments, String> aTypeCol;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> aStartCol;
    @FXML
    private TableColumn<Appointments, ZonedDateTime> aEndCol;
    @FXML
    private TableColumn<Appointments, Integer> aCustomerIDCol;
    @FXML
    private TableColumn<Appointments, String> aContactCol;
    @FXML
    private TableColumn<Appointments, String> aUserIDCol;
    @FXML
    private Button logoutBtn;
    @FXML
    private Button addC;
    @FXML
    private Button updateC;
    @FXML
    private Button deleteC;
    @FXML
    private Button addA;
    @FXML
    private Button updateA;
    @FXML
    private Button deleteA;

    /**
     *
     * @param actionEvent On button click, exits program
     */

    public void onActionLogout(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.exit(0);
    }


    /**
     *
     * @param actionEvent when clicked opens the add customer menu
     * @throws IOException
     */

    public void onActionAddC(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     *
     * @param actionEvent when clicked,opens the update customer menu
     */
    public void onActionUpdateC(ActionEvent actionEvent) {

        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/updateCustomerMenu.fxml"));
            loader.load();

            updateCustomerController  ucController = loader.getController();
            ucController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException | SQLException | IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Customer Selected");
            alert.setContentText("Please select a customer to update");
            alert.showAndWait();
        }

    }

    /**
     *
     * @param actionEvent Must select a customer to delete, when clicked, deletes selected customer
     * @throws SQLException
     * @throws IOException
     */

    public void onActionDeleteC(ActionEvent actionEvent) throws SQLException, IOException {

        Customers customerToDelete = customerTableView.getSelectionModel().getSelectedItem();
        if(customerTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR: No Customer Selected");
            alert.setContentText("Please select a customer to delete");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to delete customer: " + customerToDelete.getCustomer_Name() + "?");
            alert.setContentText("Click Ok to confirm");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK)
            {
                AppointmentQuery.cDelete(customerToDelete.getCustomer_ID());
                CustomerQuery.delete(customerToDelete.getCustomer_ID());
            }
        }
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Must select a customer, when clicked opens the add appointment menu for the given customer
     * @param actionEvent
     * @throws IOException
     */

    public void onActionAddA(ActionEvent actionEvent) throws IOException {

        customerTableView.getSelectionModel().getSelectedItem();
        if(customerTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR: No Customer Selected for Appointment");
            alert.setContentText("Please select a customer to make an appointment");
            alert.showAndWait();
        }
        else
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/addAppointmentMenu.fxml"));
            loader.load();

            addAppointmentController  aaController = loader.getController();
            aaController.sendCustomerInfo(customerTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * Must select an appointment, when clicked opens the update appointment menu for the given customer
     * @param actionEvent
     * @throws IOException
     */

    public void onActionUpdateA(ActionEvent actionEvent) {

        try
        {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/updateAppointmentMenu.fxml"));
            loader.load();

            updateAppointmentController  uaController = loader.getController();
            uaController.sendAppointment(appointmentsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        catch(NullPointerException | SQLException | IOException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No Appointment Selected");
            alert.setContentText("Please select an appointment to update");
            alert.showAndWait();
        }
    }

    /**
     * Must select an appointment, when clicked deletes the selected appointment
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */

    public void onActionDeleteA(ActionEvent actionEvent) throws SQLException, IOException {


        Appointments appointmentToDelete = appointmentsTableView.getSelectionModel().getSelectedItem();
        if(appointmentsTableView.getSelectionModel().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR: No Appointment Selected");
            alert.setContentText("Please select an appointment to delete");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to cancel appointment " + "ID: " + appointmentToDelete.getAppointment_ID() + "   Type: " + appointmentToDelete.getType() + "?");
            alert.setContentText("Click Ok to confirm");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK)
            {
                AppointmentQuery.delete(appointmentToDelete.getAppointment_ID());
            }
        }
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * When view all selected, see all appointments in database
     * @param actionEvent
     * @throws SQLException
     */

    public void onActionViewAll(ActionEvent actionEvent) throws SQLException {

        viewByMonth.setSelected(false);
        viewByWeek.setSelected(false);

        appointmentsTableView.setItems(AppointmentQuery.getAllAppointments());
        aIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        aTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        aDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        aLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        aTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        aStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        aEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        aCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
        aContactCol.setCellValueFactory(new PropertyValueFactory<>("contact_Name"));
        aUserIDCol.setCellValueFactory(new PropertyValueFactory<>("user_ID"));
        appointmentsTableView.getSortOrder().add(aIDCol);
    }

    /**
     * When view month selected, view appointments in the coming month.
     * Lambda Function to streamline addition of filtered appointments to month table
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */

    public void onActionMonth(ActionEvent actionEvent) throws SQLException, IOException {

        try {
            viewAllBtn.setSelected(false);
            viewByWeek.setSelected(false);

            ObservableList<Appointments> allAppointments = AppointmentQuery.getAllAppointments();
            ObservableList<Appointments> byMonth = FXCollections.observableArrayList();

            ZonedDateTime filteredStartLocal = ZonedDateTime.now(ZoneId.systemDefault());
            ZonedDateTime filteredEndLocal = filteredStartLocal.plusMonths(1);

            ZonedDateTime filteredStartUTC = filteredStartLocal.withZoneSameInstant(ZoneOffset.UTC);
            ZonedDateTime filteredEndUTC = filteredEndLocal.withZoneSameInstant(ZoneOffset.UTC);

            allAppointments.forEach(
                    appointments -> {
                        if (appointments.getStart().toLocalDateTime().atZone(ZoneOffset.UTC).isAfter(filteredStartUTC.minusDays(1)) &&
                                appointments.getStart().toLocalDateTime().atZone(ZoneOffset.UTC).isBefore(filteredEndUTC))
                        {
                            byMonth.add(appointments);
                        }

                        appointmentsTableView.setItems(byMonth);
                        appointmentsTableView.getSortOrder().add(aIDCol);
                    }

            );


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    /**
     * When view week selected, view appointments in the coming week.
     * Lambda Function to streamline addition of filtered appointments to week table
     * @param actionEvent
     * @throws SQLException
     */

    public void onActionWeek(ActionEvent actionEvent) throws SQLException {

        viewAllBtn.setSelected(false);
        viewByMonth.setSelected(false);

        appointmentsTableView.setItems(null);

        ObservableList<Appointments> allAppointments = AppointmentQuery.getAllAppointments();
        ObservableList<Appointments> byWeek = FXCollections.observableArrayList();

        ZonedDateTime filteredStartLocal = ZonedDateTime.now();
        ZonedDateTime filteredEndLocal = filteredStartLocal.plusWeeks(1);

        ZonedDateTime filteredStartUTC = filteredStartLocal.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime filteredEndUTC = filteredEndLocal.withZoneSameInstant(ZoneOffset.UTC);

        allAppointments.forEach(
                appointments -> {
                    if (appointments.getStart().toLocalDateTime().atZone(ZoneOffset.UTC).isAfter(filteredStartUTC.minusDays(1)) &&
                            appointments.getStart().toLocalDateTime().atZone(ZoneOffset.UTC).isBefore(filteredEndUTC))
                    {
                        byWeek.add(appointments);
                    }

                }
        );

        appointmentsTableView.setItems(byWeek);
        appointmentsTableView.getSortOrder().add(aIDCol);
    }

    /**
     * When clicked, opens the reports menu
     * @param actionEvent
     * @throws IOException
     */

    public void onActionReports(ActionEvent actionEvent) throws IOException {

        stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/Reports.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Initializes the main menu, if there is an appointment within the next 15 minutes, alerts user, otherwise says there are no appointments within 15 minutes.
     * Lambda to search through appointments and to see if any are scheduled within 15 minutes of when user logs in.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



        try {
            ObservableList<Appointments> allAppointments = AppointmentQuery.getAllAppointments();
            AtomicBoolean upcomingAppointment = new AtomicBoolean(false);
            JDBC.openConnection();

            customerTableView.setItems(CustomerQuery.getAllCustomers());
            cIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
            cNameCol.setCellValueFactory((new PropertyValueFactory<>("customer_Name")));
            cAddressCol.setCellValueFactory((new PropertyValueFactory<>("address")));
            CPostalCol.setCellValueFactory((new PropertyValueFactory<>("postal_Code")));
            cPhoneCol.setCellValueFactory((new PropertyValueFactory<>("phone")));
            cDivisionIDCol.setCellValueFactory((new PropertyValueFactory<>("division_ID")));

            appointmentsTableView.setItems(allAppointments);
            aIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
            aTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            aDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            aLocationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            aTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            aStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            aEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            aCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
            aContactCol.setCellValueFactory(new PropertyValueFactory<>("contact_Name"));
            aUserIDCol.setCellValueFactory(new PropertyValueFactory<>("user_ID"));
            appointmentsTableView.getSortOrder().add(aIDCol);

            viewAllBtn.setSelected(true);



            allAppointments.forEach(
                    appointments -> {
                        if(appointments.getStart().toLocalDateTime().isAfter(LocalDateTime.now()) && appointments.getStart().toLocalDateTime().isBefore(LocalDateTime.now().plusMinutes(15))){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText("There is an appointment scheduled in the next 15 minutes");
                            alert.setContentText("Appointment ID: " + appointments.getAppointment_ID() + "\nStart Date and Time: " + appointments.getStart());
                            alert.showAndWait();
                            upcomingAppointment.set(true);
                        }
                    }
            );
            if(!upcomingAppointment.get())
            {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("There are no appointments scheduled in the next 15 minutes");
                alert.showAndWait();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }


}
