package controller;

import helper.AppointmentQuery;
import helper.Country_Division_Query;
import helper.CustomerQuery;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ReportsController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView appointmentsByTypeTableView;
    @FXML
    private TableColumn reportTypeCol;
    @FXML
    private TableColumn reportTypeTotalCol;
    @FXML
    private TableView appointmentsByMonthTableView;
    @FXML
    private TableColumn reportMonthCol;
    @FXML
    private TableColumn reportMonthTotalCol;
    @FXML
    private TableView rContactScheduleTableView;
    @FXML
    private TableColumn rAppointmentIDCol;
    @FXML
    private TableColumn rTitleCol;
    @FXML
    private TableColumn rTypeCol;
    @FXML
    private TableColumn rDescriptionCol;
    @FXML
    private TableColumn rStartCol;
    @FXML
    private TableColumn rEndCol;
    @FXML
    private TableColumn rCustomerIDCol;
    @FXML
    private ComboBox<String> rContactCombobox;
    @FXML
    private TableView customersByDistrictTableView;
    @FXML
    private TableColumn reportDistrictCol;
    @FXML
    private TableColumn reportDistrictTotalCol;


    /**
     * When clicked, user is returned to the main menu
     * @param actionEvent
     * @throws IOException
     */

    public void onActionMain(ActionEvent actionEvent) throws IOException {
        stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * When clicked, exits the program
     * @param actionEvent
     */

    public void onActionLogout(ActionEvent actionEvent) {
        JDBC.closeConnection();
        System.exit(0);
    }

    public void onActionSelectContact(ActionEvent actionEvent) throws SQLException {
        rContactScheduleTableView.setItems(AppointmentQuery.getAppointmentsByContact(rContactCombobox.getValue()));
        rAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
        rTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        rDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        rTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        rStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        rEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        rCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));
    }

    /**
     * Initializes the reports menu, setting desired tableviews
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            appointmentsByTypeTableView.setItems(AppointmentQuery.getTypeTotals());
            reportTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            reportTypeTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

            appointmentsByMonthTableView.setItems(AppointmentQuery.getMonthTotals());
            reportMonthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
            reportMonthTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

            rContactScheduleTableView.setItems(AppointmentQuery.getAllAppointments());
            rAppointmentIDCol.setCellValueFactory(new PropertyValueFactory<>("appointment_ID"));
            rTitleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            rDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            rTypeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            rStartCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            rEndCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            rCustomerIDCol.setCellValueFactory(new PropertyValueFactory<>("customer_ID"));

            customersByDistrictTableView.setItems(Country_Division_Query.getDivisionTotals());
            reportDistrictCol.setCellValueFactory(new PropertyValueFactory<>("division"));
            reportDistrictTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));

            rContactCombobox.setItems(AppointmentQuery.initializeContact());




        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
