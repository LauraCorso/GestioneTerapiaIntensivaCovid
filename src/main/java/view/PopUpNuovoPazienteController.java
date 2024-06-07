package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import DB.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import model.Paziente;

public class PopUpNuovoPazienteController implements Initializable {
	@FXML
	private Label CF;
	
	@FXML
	private Label Nome;
	
	@FXML
	private Label Cognome;
	
	@FXML
	private Label DataN;
	
	@FXML
	private Label LuogoN;
	
	@FXML
	private Label Messaggio;
	
	@FXML
	private Button Inserisci;
	
	@FXML
	private TextField cfI;
	
	@FXML
	private TextField nomeI;
	
	@FXML
	private TextField cognomeI;
	
	@FXML
	private DatePicker dataI;
	
	@FXML
	private TextField luogoI;
	
	// Variabile che salva il valore della data selezionata (DatePicker)
	private Date varData = new Date(0);
	
	/** Metodo get per varData
	 * 
	 * @return varData
	 */
	public Date getVarData() {
		return varData;
	}
	
	/** Metodo set per varData. 
	 * Prende in ingresso una variabile LocalDate e la converte in Date
	 * 
	 * @param LocalDate dataLD
	 * @throws ParseException 
	 */
	public void setVarData(LocalDate dataLD) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		varData = new Date(sdf.parse(dataLD.toString()).getTime());
	}
	
	
	/** Metodo che conta il numero di pazienti correntemente ricoverati
	 * 
	 * @return numero pazienti
	 */
	public static int numeroPazienti() {
		int n = 0;
		try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT COUNT(*) AS NumeroP FROM Paziente WHERE ricovero = true;");
			rs.next();
			n = rs.getInt("NumeroP");
	
			rs.close();
			stmt.close();
	        c.close();
	        System.out.println("Connessione chiusa");
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return n; 
	}
	
	/**
	 * Metodo che controlla se un paziente esiste già
	 * 
	 * @return boolean
	 */
	public static boolean pazientePresente(String CF) {
		// Creo la lista di stringhe
		List<String> listaP = new ArrayList();
		
		// SELECT lista codici fiscali
		try {
			Connection c = Database.Connessione();  
	
			Statement stmt = c.createStatement();
			
			ResultSet rs = stmt.executeQuery( "SELECT CodSanitario FROM Paziente ORDER BY CodSanitario;" );
			while ( rs.next() ) {
					String CodSanitario = rs.getString("CodSanitario");
					listaP.add(CodSanitario);
			}
			stmt.close();
	        c.close();
	        System.out.println("Connessione chiusa");
			}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		int i = 0;
		boolean trovato = false;
		while (i < listaP.size() && !trovato) {
			if(listaP.get(i).equals(CF)) {
				trovato = true;
				break;
			}
			i++;
		}
		return trovato;
	}
	
	/**
	 * Metodo che inizializza la scena
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Inserisci.setOnAction(event -> {
			if (numeroPazienti() == 10) {
				Messaggio.setText("Numero massimo di pazienti raggiunto!");
				Messaggio.setTextFill(Color.RED);
			}
			else {
				if (cfI.getText().toString().equals("") || cognomeI.getText().toString().equals("") || nomeI.getText().toString().equals("") || luogoI.getText().toString().equals("") || getVarData().toString().equals("0")) {
					Messaggio.setText("Riempire tutti i campi!");
					Messaggio.setTextFill(Color.RED);
				}
				else if (pazientePresente(cfI.getText().toString())) {
					Messaggio.setText("Il paziente è già presente!");
					Messaggio.setTextFill(Color.RED);
				}
				else {
					try {
						Connection c = Database.Connessione();
						PreparedStatement ps = c.prepareStatement("INSERT INTO paziente(CodSanitario, Cognome, Nome, DataN, LuogoN) VALUES (?, ?, ?, ?, ?);");
						ps.setString(1, cfI.getText().toString());
						ps.setString(2, cognomeI.getText().toString());
						ps.setString(3, nomeI.getText().toString());
						ps.setString(5, luogoI.getText().toString());
						setVarData(dataI.getValue());
						ps.setDate(4, getVarData());
						int r = ps.executeUpdate();
						Messaggio.setText("Inserita "+r+" riga");
						Messaggio.setTextFill(Color.GREEN);
						
						ps.close();
				        c.close();
				        System.out.println("Connessione chiusa");
						}
				    catch ( Exception e ) {
						System.err.println( e.getClass().getName()+": "+ e.getMessage() );
						System.exit(0);
				    }

				view.PrimaController.setNuovoP(cfI.getText().toString());
				view.PrimaController.setModificaNP(true);
			}
			}
		});
	}
}
