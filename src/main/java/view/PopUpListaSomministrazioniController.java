package view;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import DB.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Paziente;
import model.SBP;
import model.Somministrazione;
import model.T;

public class PopUpListaSomministrazioniController implements Initializable {
	@FXML
	private TableView<Paziente> TabellaPazienti;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCognome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaNome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCFp;
	
	@FXML
	private TableView<Somministrazione> TabellaSomm;
	
	@FXML
	private TableColumn<Somministrazione, String> ColonnaInf;
	
	@FXML
	private TableColumn<Somministrazione, String> ColonnaFarm;
	
	@FXML
	private TableColumn<Somministrazione, String> ColonnaData;
	
	@FXML
	private TableColumn<Somministrazione, Time> ColonnaOra;
	
	@FXML
	private TableColumn<Somministrazione, String> ColonnaDose;
	
	@FXML
	private TableColumn<Somministrazione, String> ColonnaNote;
	
	/**
	 * Metodo che estrae dalla BD tutti i pazienti e li mette in una lista
	 * 
	 * @return ObservableList<Paziente> ListaPazienti
	 */
	public static ObservableList<Paziente> getAllPazienti() {
    	ObservableList<Paziente> ListaPazienti = FXCollections.observableArrayList();
    	try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
			while ( rs.next() ) {
					String CodSanitario = rs.getString("CodSanitario");
					String  Cognome = rs.getString("Cognome");
					String  Nome = rs.getString("Nome");
					Calendar DataN  = Calendar.getInstance();
					DataN.setTime(rs.getDate("DataN"));
					String  LuogoN = rs.getString("LuogoN");
					
					Paziente p = new Paziente(CodSanitario, Cognome, Nome, DataN, LuogoN);
					ListaPazienti.add(p);
			}
			rs.close();
			stmt.close();
	        c.close();
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return ListaPazienti;    
    }
	
	/**
	 * Metodo che estrae dalla BD tutte le somministrazioni di un paziente e le mette in una lista
	 * 
	 * @return ObservableList<Somministrazione> ListaSomministrazioni
	 */
	public static ObservableList<Somministrazione> getAllSomministrazioni(String CF) {
    	ObservableList<Somministrazione> ListaSomministrazioni = FXCollections.observableArrayList();
    	try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			// TODO Inserire nuove somminstrazioni nel db
			PreparedStatement stmt = c.prepareStatement("SELECT * FROM Somministrazione WHERE paziente = ? AND (CURRENT_DATE - Data) < 3;");
			stmt.setString(1, CF);
			
			ResultSet rs = stmt.executeQuery();
			while ( rs.next() ) {
					String Paziente = rs.getString("Paziente");
					String  Infermiere = rs.getString("Infermiere");
					String  Farmaco = rs.getString("Farmaco");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String Data = rs.getDate("Data").toString();
					Time Ora = rs.getTime("Ora");
					String Dose = Integer.toString(rs.getInt("Dose"));
					String  Note = rs.getString("Note");
					
					Somministrazione s = new Somministrazione(Paziente, Infermiere, Farmaco, Data, Ora, Dose, Note);
					ListaSomministrazioni.add(s);
			}
			rs.close();
			stmt.close();
	        c.close();
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return ListaSomministrazioni;    
    }
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		/**
		 * Inizializzazione della tabella dei pazienti
		 */
		ColonnaCognome.setCellValueFactory(cellData -> cellData.getValue().getCognome());
		ColonnaNome.setCellValueFactory(cellData -> cellData.getValue().getNome());
		ColonnaCFp.setCellValueFactory(cellData -> cellData.getValue().getCodSanitario());
		ObservableList<Paziente> lista = getAllPazienti();
		TabellaPazienti.setItems(lista);
		
		ColonnaInf.setCellValueFactory(cellData -> cellData.getValue().getInfermiere());
		ColonnaFarm.setCellValueFactory(cellData -> cellData.getValue().getFarmaco());
		ColonnaData.setCellValueFactory(cellData -> cellData.getValue().getData());
		ColonnaOra.setCellValueFactory(cellData -> cellData.getValue().getOra());
		ColonnaDose.setCellValueFactory(cellData -> cellData.getValue().getDose());
		ColonnaNote.setCellValueFactory(cellData -> cellData.getValue().getNote());
		
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaSomm.setItems(getAllSomministrazioni(newValue.getCodSanitario().get())));
	}
	
}
