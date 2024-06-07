package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.collections.*;

import model.Paziente;
import view.PrimaController;
import DB.Database;

public class Main extends Application {
	
    private static Stage primaryStage;
    private BorderPane rootLayout;
    
    /**
     * Questo metodo inizializza l'interfaccia del programma 
     * 
     * @param primaryStage lo stage di javafx
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Terapia Intensiva");

        initRoot();
        showPrima();
    }
    
    /**
     * Questo metodo inizializza la base dell'interfaccia primaria
     */
    public void initRoot() {
        try {
            // Caricamento file fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/Root.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Visualizzazione scene root layout
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Questo metodo inizializza l'interfaccia primaria
     */
    public void showPrima() {
    	try {
            // Caricamento file fxml
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/Prima.fxml"));
            AnchorPane Prima = (AnchorPane) loader.load();
            
            rootLayout.setCenter(Prima);
    	} catch (IOException e) {
            e.printStackTrace();
        }    
    }
    
	/**
	 * Ritorna lo stage principale
	 * 
	 * @return primaryStage
	 */
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
			launch(args);	//lancia l'interfaccia ed il programma
	}

}
