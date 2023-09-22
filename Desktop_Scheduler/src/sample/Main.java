package sample;

import helper.CustomerQuery;
import helper.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;


/**
 * Javadoc files are included in the Javadocs file contained in the submission
 */
public class Main extends Application {



    @Override
    public void init() {System.out.println("Starting");}

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("../view/LoginMenu.fxml"));
        primaryStage.setTitle("Appointment Scheduling System");
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.show();

    }

    @Override
    public void stop() {System.out.println("Terminated"); }


    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch(args);
        JDBC.closeConnection();
    }
}
