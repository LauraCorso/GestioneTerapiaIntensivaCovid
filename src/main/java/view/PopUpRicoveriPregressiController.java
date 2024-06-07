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
import model.Ricovero;

public class PopUpRicoveriPregressiController implements Initializable {
	@FXML
	private TableView<Paziente> TabellaPazienti;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCognome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaNome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCFp;
	
	@FXML
	private TableView<Ricovero> TabellaRicoveri;
	
	@FXML
	private TableColumn<Ricovero, String> ColonnaOspedale;
	
	@FXML
	private TableColumn<Ricovero, String> ColonnaReparto;
	
	@FXML
	private TableColumn<Ricovero, String> ColonnaData;
	
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
	 * Metodo che estrae dalla BD tutti i ricoveri pregressi di un paziente e le mette in una lista
	 * 
	 * @return ObservableList<Ricovero> ListaRicoveri
	 */
	public static ObservableList<Ricovero> getAllRicoveri(String CF) {
    	ObservableList<Ricovero> ListaRicoveri = FXCollections.observableArrayList();
    	try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			PreparedStatement stmt = c.prepareStatement("SELECT * FROM RicoveriPP WHERE paziente = ?;");
			stmt.setString(1, CF);
			
			ResultSet rs = stmt.executeQuery();
			while ( rs.next() ) {
					String Paziente = rs.getString("Paziente");
					String  Ospedale = rs.getString("Ospedale");
					String  Reparto = rs.getString("NomeRep");
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String Data = rs.getDate("Data").toString();
					
					Ricovero r = new Ricovero(Paziente, Ospedale, Reparto, Data);
					ListaRicoveri.add(r);
			}
			rs.close();
			stmt.close();
	        c.close();
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return ListaRicoveri;    
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
		
		ColonnaOspedale.setCellValueFactory(cellData -> cellData.getValue().getOspedale());
		ColonnaReparto.setCellValueFactory(cellData -> cellData.getValue().getReparto());
		ColonnaData.setCellValueFactory(cellData -> cellData.getValue().getData());
				
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaRicoveri.setItems(getAllRicoveri(newValue.getCodSanitario().get())));
	}
	
}
