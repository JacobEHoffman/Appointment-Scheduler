package controller;

import helper.UserQuery;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.ZoneId;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoginMenuController implements Initializable {


    Stage stage;
    Parent scene;

    ResourceBundle rb1;
    @FXML
    private Label userLabel;
    @FXML
    private Label passLabel;
    @FXML
    private Label languageLabel;
    @FXML
    private Label languageID;
    @FXML
    private Label tzLabel;
    @FXML
    private TextField userText;
    @FXML
    private TextField passText;
    @FXML
    private Button loginBtn;
    @FXML
    private MenuButton languageBtn;
    @FXML
    private Label timeZoneLabel;
    @FXML
    private Button exitBtn;

    /**
     * When clicked, queries the database and checks that username and password are correct.
     * If correct, directs user to main menu, otherwise notifies of incorrect username/password combo.
     * Also logs all login attempts, whether successful or unsuccessful.
     * @param actionEvent
     * @throws SQLException
     */

    public void onActionLogin(ActionEvent actionEvent) throws SQLException {
        Logger logger = Logger.getLogger("login_activity.txt");
        try {
            FileHandler fileHandler = new FileHandler("login_activity.txt", true);
            SimpleFormatter simpleFormatter = new SimpleFormatter();
            fileHandler.setFormatter(simpleFormatter);
            logger.addHandler(fileHandler);


            String userName = userText.getText();
            String password = passText.getText();
            if (UserQuery.loginAttempt(userName, password)) {
                logger.setLevel(Level.INFO);
                logger.log(Level.INFO, "Successful Login by User " + UserQuery.userIDQuery(userName) + " at Time: " + LocalDateTime.now());
                stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
            else{
                logger.setLevel(Level.INFO);
                logger.log(Level.INFO, "Unsuccessful Login by User " + UserQuery.userIDQuery(userName) + " at Time: " + LocalDateTime.now());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(rb1.getString("alertHeader"));
                alert.setContentText(rb1.getString("alertContent"));
                alert.showAndWait();
            }
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }


    }

    /**
     * When clicked, exits the program
     * @param actionEvent
     */

    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    /**
     * Initializes the login menu, depending on the system's default language, changes the displayed text
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb1 = ResourceBundle.getBundle("Languages", Locale.getDefault());
        userLabel.setText(rb1.getString("username"));
        passLabel.setText(rb1.getString("password"));
        languageLabel.setText(rb1.getString("language"));
        languageID.setText(rb1.getString("languageID"));
        tzLabel.setText(rb1.getString("timeZone"));
        timeZoneLabel.setText(ZoneId.systemDefault().toString());
        loginBtn.setText(rb1.getString("login"));
        exitBtn.setText(rb1.getString("exit"));



    }

}