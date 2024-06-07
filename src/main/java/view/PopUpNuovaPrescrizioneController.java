package view;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import DB.Database;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

public class PopUpNuovaPrescrizioneController implements Initializable {	
	@FXML
	private Label LCF;

	@FXML
	private Label LFarmaco;
	
	@FXML
	private Label LDosiGG;
	
	@FXML
	private Label LQuantita;
	
	@FXML
	private Label LVentilazione;
	
	@FXML
	private ComboBox<String> CFp;
	
	@FXML
	private ComboBox<String> Farmaco;
	
	@FXML
	private TextField DosiGG;
	
	@FXML
	private TextField Quantita;
	
	@FXML
	private ToggleButton Ventilazione;
	
	@FXML
	private Button Inserisci;
	
	@FXML
	private Label Messaggio;
	
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
	 * Metodo che estrae dalla base di dati i nomi di tutti i farmaci.
	 * 
	 * @return ArraList<String>
	 * 
	 */
	public static ObservableList<String> getAllFarmaci() {
		// Creo la lista di stringhe Farmaco
		ObservableList<String> listaFarm = FXCollections.observableArrayList();
		
		// SELECT
		try {
			Connection c = Database.Connessione();  
	
			Statement stmt = c.createStatement();
			
			// Seleziono i nomi di tutti i farmaci
			ResultSet rs = stmt.executeQuery( "SELECT Nome FROM Farmaco ORDER BY Nome;" );
			while ( rs.next() ) {
					String Farmaco = rs.getString("Nome");
					
					listaFarm.add(Farmaco);
			}
			stmt.close();
	        c.close();
	        System.out.println("Connessione chiusa");
			}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }

		return listaFarm;
	}
	
	/**
	 * Metodo che inizializza la scena
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Estraggo i codici fiscali dei pazienti attualmente ricoverati e li metto nella ComboBox
		CFp.getItems().addAll(getAllCF());
		
		// Estraggo i nomi dei farmaci e li metto nella ComboBox
		Farmaco.getItems().addAll(getAllFarmaci());
		
		Inserisci.setOnAction(event -> {
			if (CFp.getValue() == null || Farmaco.getValue() == null || DosiGG.getText().equals("") || Quantita.getText().equals("")) {
				Messaggio.setText("Riempire tutti i campi!");
				Messaggio.setTextFill(Color.RED);
			}
			else {
				try {
					Connection c = Database.Connessione();
					PreparedStatement ps = c.prepareStatement("INSERT INTO Prescrizione(Paziente, Medico, Farmaco, Data, DosiGG, Quantita, Ventilazione) VALUES (?, ?, ?, ?, ?, ?, ?);");
					ps.setString(1, CFp.getValue());
					ps.setString(2, PrimaController.getUs());
					ps.setString(3, Farmaco.getValue());
					ps.setDate(4, new Date(Calendar.getInstance().getTimeInMillis()));
					ps.setInt(5, Integer.valueOf(DosiGG.getText()));
					ps.setDouble(6, Double.valueOf(Quantita.getText()));
					ps.setBoolean(7, Ventilazione.isSelected());
					ps.executeUpdate();
					
					Messaggio.setText("Inserita nuova prescrizione");
					Messaggio.setTextFill(Color.GREEN);
					ps.close();
			        c.close();
			        System.out.println("Connessione chiusa");
				} 
				catch ( Exception e ) {
					System.err.println( e.getClass().getName()+": "+ e.getMessage() );
					System.exit(0);
			    }
			}
		});
	}
}

