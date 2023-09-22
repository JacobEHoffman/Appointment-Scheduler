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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static helper.CustomerQuery.getAllCustomers;

public class addCustomerController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TextField addCName;
    @FXML
    private TextField addCAddress;
    @FXML
    private TextField addCPostal;
    @FXML
    private TextField addCPhone;
    @FXML
    private ComboBox<String> addCCountry;
    @FXML
    private ComboBox<String> addCState;
    @FXML
    private Button addCConfirm;
    @FXML
    private Button addCExit;

    /**
     * When clicked, validates inputs and iff successful saves a new customer into database
     * @param actionEvent
     * @throws IOException
     */


    public void onActionConfirm(ActionEvent actionEvent) throws IOException {

        try {
            int customer_ID = getAllCustomers().size()+1;
            String customer_Name = addCName.getText();
            String address = addCAddress.getText();
            String postal_Code = addCPostal.getText();
            String phone = addCPhone.getText();
            String country = addCCountry.getValue();
            String division = addCState.getValue();

            CustomerQuery.insert(customer_Name, address, postal_Code, phone, Country_Division_Query.getDivisionID(division));


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

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all text field values, do you want to continue?");
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
     * When a country is selected from the combobox, populates the division combobox with the appropriate locations
     * @param actionEvent
     * @throws SQLException
     */

    public void onActionSelectionMade(ActionEvent actionEvent) throws SQLException {
        addCState.setDisable(false);
        addCState.setItems(Country_Division_Query.initializeDivision(addCCountry.getValue()));

    }

    /**
     * Initializes the add customer menu, populating the country combobox
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        JDBC.openConnection();
        try{
            addCCountry.setItems(Country_Division_Query.initializeCountry());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        addCState.setDisable(true);



}


}
