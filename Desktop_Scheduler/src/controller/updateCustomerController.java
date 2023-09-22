package controller;

import helper.Country_Division_Query;
import helper.CustomerQuery;
import helper.JDBC;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Customers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;



public class updateCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField updateCustomerID;
    @FXML
    private TextField updateCName;
    @FXML
    private TextField updateCAddress;
    @FXML
    private TextField updateCPostal;
    @FXML
    private TextField updateCPhone;
    @FXML
    private ComboBox<String> updateCCountry;
    @FXML
    private ComboBox<String> updateCState;
    @FXML
    private Button updateCConfirm;
    @FXML
    private Button updateCExit;

    /**
     *
     * @param actionEvent
     * @throws IOException If confirm is clicked, check for input errors and if correct updates customer and returns to main menu
     */


    public void onActionConfirm(ActionEvent actionEvent) throws IOException {

        try {
            int customer_ID = Integer.parseInt(updateCustomerID.getText());
            String customer_Name = updateCName.getText();
            String address = updateCAddress.getText();
            String postal_Code = updateCPostal.getText();
            String phone = updateCPhone.getText();
            String country = updateCCountry.getValue();
            String division = updateCState.getValue();

            CustomerQuery.update(customer_Name, address, postal_Code, phone, Country_Division_Query.getDivisionID(division), customer_ID);

            stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

        } catch(NumberFormatException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialogue");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }
        catch(SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     *
     * @param actionEvent check with user, then cancel customer update and return to main menu
     * @throws IOException
     */

    public void onActionExit(ActionEvent actionEvent) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to cancel?");
        alert.setContentText("Your changes will not be saved");
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
     * @param customer send customer information to be updated
     * @throws SQLException
     *
     *
     */

    public void sendCustomer(Customers customer) throws SQLException {
        updateCustomerID.setText(String.valueOf(customer.getCustomer_ID()));
        updateCName.setText(customer.getCustomer_Name());
        updateCAddress.setText(customer.getAddress());
        updateCPostal.setText(customer.getPostal_Code());
        updateCPhone.setText(customer.getPhone());
        updateCCountry.setItems(Country_Division_Query.initializeCountry());
        updateCCountry.getSelectionModel().select(customer.getCountry());
        updateCState.setItems(Country_Division_Query.initializeDivision(updateCCountry.getValue()));
        updateCState.getSelectionModel().select(customer.getDivision_Name());
    }

    /**
     *
     * @param actionEvent When user selects country, initializes appropriate list in division combobox
     * @throws SQLException
     */

    public void onActionSelectionMade(ActionEvent actionEvent) throws SQLException {
        updateCState.setDisable(false);
        updateCState.setItems(Country_Division_Query.initializeDivision(updateCCountry.getValue()));
    }

    /**
     *
     * @param url
     * @param resourceBundle Initialize updateCustomerController
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        JDBC.openConnection();

    }
}
