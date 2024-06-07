package view;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;

import DB.Database;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Paziente;

public class PopUpDimissionePazienteController implements Initializable {
	@FXML
	private ComboBox<String> CS;
	
	@FXML
	private Label Messaggio;
	
	@FXML 
	private Button Stampa;
	
	@FXML 
	private Button Dimetti;
	
	@FXML
	private TextArea LetteraDimissione;
	
	// Stringa che memorizza il CodSanitario di un paziente dimesso
	private static String CFd;
	
	/** 
	 * Metodo get di CFd
	 * 
	 * @return String CF paziente dimesso
	 */
	public static String getCFd() {
		return CFd;
	}
	
	/** 
	 * Metodo set di CFd
	 * 
	 */
	public static void setCFd(String CF) {
		CFd = new String(CF);
	}
	
	/**
	 * Metodo che estrae dalla base di dati i codici fiscali di tutti i pazienti attualmente ricoverati.
	 * 
	 * @return ArraList<String>
	 * 
	 */
	public static ObservableList<String> getAllCF() {
		// Creo la lista di stringhe CF
		ObservableList<String> listaCF = FXCollections.observableArrayList();
		
		// SELECT
		try {
			Connection c = Database.Connessione();  
	
			Statement stmt = c.createStatement();
			
			// Seleziono i CF di tutti i pazienti
			ResultSet rs = stmt.executeQuery( "SELECT CodSanitario FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
			while ( rs.next() ) {
					String CodSanitario = rs.getString("CodSanitario");
					
					listaCF.add(CodSanitario);
			}
			stmt.close();
	        c.close();
	        System.out.println("Connessione chiusa");
			}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }

		return listaCF;
	}
	
	/**
	 * Metodo che salva la lettera di dimissione nel file specificato.
	 * 
	 * @param String lettera
	 * @param File file
	 */
	private void salvaTestoInFile(String contenuto, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println(contenuto);
            writer.close();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Estreggo i codici fiscali dei pazienti attualmente ricoverati e li metto nella ComboBox
		CS.getItems().addAll(getAllCF());
		
		// Aggiungo il listener alla ComboBox --> salvo il valore selezionato
		CS.valueProperty().addListener((observable, oldValue, newValue) -> {
				setCFd(newValue.toString());
		});
		
		// Aggiungo il listener al pulsante Dimetti --> aggiorno l'interfaccia Prima e setto a false l'attributo ricovero in Paziente
		Dimetti.setOnAction(eventD -> {
			if (getCFd() == null) {
				Messaggio.setText("Selezionare un paziente!");
				Messaggio.setTextFill(Color.RED);
			}
			else {
				try {
					Connection c = Database.Connessione();
					PreparedStatement stmt = c.prepareStatement("UPDATE Paziente SET ricovero = false WHERE CodSanitario = ?;");
					stmt.setString(1, getCFd());
					
					stmt.executeUpdate();
					
					stmt.close();
			        c.close();
			        System.out.println("Connessione chiusa");
					}
			    catch ( Exception e ) {
					System.err.println( e.getClass().getName()+": "+ e.getMessage() );
					System.exit(0);
			    }
				PrimaController.setCFD(getCFd());
			}
		});
		
		// Aggiungo il listener al pulsante Stampa --> apre la finestra di sistema per salvare la lettera di dimissione
		Stampa.setOnAction(eventS -> {
			if (getCFd() == null) {
				Messaggio.setText("Selezionare un paziente!");
				Messaggio.setTextFill(Color.RED);
			}
			else {
				FileChooser fc = new FileChooser();
				fc.setInitialFileName("LetteraDimissione_"+getCFd()+".txt"); //nome iniziale del file
				
				File fileLettera = fc.showSaveDialog(((Stage)(((Button)eventS.getSource()).getScene().getWindow())));
				
				if (fileLettera != null) {
					salvaTestoInFile(LetteraDimissione.getText(), fileLettera);
				}
			}
		});
	}
}