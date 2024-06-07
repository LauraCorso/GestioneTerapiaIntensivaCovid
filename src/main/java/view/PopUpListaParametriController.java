package view;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;

import DB.Database;
import controller.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBP;
import model.FC;
import model.Infermiere;
import model.Paziente;
import model.S;
import model.SBP;
import model.T;

public class PopUpListaParametriController implements Initializable {
	@FXML
	private TableView<Paziente> TabellaPazienti;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCognome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaNome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCFp;
	
	@FXML
	private TableView<SBP> TabellaSBP;
	
	@FXML
	private TableColumn<SBP, Integer> ColonnaSBP;
	
	@FXML
	private TableView<DBP> TabellaDBP;
	
	@FXML
	private TableColumn<DBP, Integer> ColonnaDBP;
	
	@FXML
	private TableView<FC> TabellaFC;
	
	@FXML
	private TableColumn<FC, Integer> ColonnaFC;
	
	@FXML
	private TableView<S> TabellaS;
	
	@FXML
	private TableColumn<S, Integer> ColonnaS;
	
	@FXML
	private TableView<T> TabellaT;
	
	@FXML
	private TableColumn<T, Double> ColonnaT;
	
	
	
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
	 * Metodo che estrae dalla BD le ultime 60 righe di SBP del paziente selezionato
	 * 
	 * @return lista di SBP
	 */ 
	public static ObservableList<SBP> get60SBP(String CF) {
		ObservableList<SBP> ListaSBP = FXCollections.observableArrayList();
		int n_righe = 0;
		
		try {
			Connection c = Database.Connessione();  
			
			PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS righeTot FROM SBP WHERE paziente = ?;" );
			stmt.setString(1, CF);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			n_righe = rs.getInt("righeTot");
			
			stmt.close();
			c.close();
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		try {
			Connection c = Database.Connessione(); 
			if (n_righe <= 60) {
				PreparedStatement stmtP = c.prepareStatement("SELECT paziente, valreg FROM SBP WHERE paziente = ?;");
				stmtP.setString(1, CF);
				ResultSet rs = stmtP.executeQuery();
				
				while (rs.next()) {
					SBP obj = new SBP(rs.getInt("valreg"), rs.getString("paziente"));
					ListaSBP.add(obj);
				}
				
				stmtP.close();
			}
			else {
				PreparedStatement stmt = c.prepareStatement("SELECT paziente, valreg FROM SBP WHERE paziente = ? LIMIT 60 OFFSET ?;" );
				stmt.setString(1, CF);
				stmt.setInt(2, n_righe-60);
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					SBP obj = new SBP(rs.getInt("valreg"), rs.getString("paziente"));
					ListaSBP.add(obj);
				}
				stmt.close();
			}
			
	        c.close();
	        System.out.println("Connessione chiusa");
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		return ListaSBP;
	}
	
	/**
	 * Metodo che estrae dalla BD le ultime 60 righe di DBP del paziente selezionato
	 * 
	 * @return lista di DBP
	 */ 
	public static ObservableList<DBP> get60DBP(String CF) {
		ObservableList<DBP> ListaDBP = FXCollections.observableArrayList();
		int n_righe = 0;
		
		try {
			Connection c = Database.Connessione();  
			PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS righeTot FROM DBP WHERE paziente = ?;" );
			stmt.setString(1, CF);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			n_righe = rs.getInt("righeTot");
			
			stmt.close();
			c.close();
			
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		try {
			Connection c = Database.Connessione();  
	
			if (n_righe <= 60) {
				PreparedStatement stmtP = c.prepareStatement("SELECT paziente, valreg FROM DBP WHERE paziente = ?;");
				stmtP.setString(1, CF);
				ResultSet rs = stmtP.executeQuery();
				
				while (rs.next()) {
					DBP obj = new DBP(rs.getInt("valreg"), rs.getString("paziente"));
					ListaDBP.add(obj);
				}
				
				stmtP.close();
			}
			else {
				PreparedStatement stmt = c.prepareStatement("SELECT paziente, valreg FROM DBP WHERE paziente = ? LIMIT 60 OFFSET ?;" );
				stmt.setString(1, CF);
				stmt.setInt(2, n_righe-60);
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					DBP obj = new DBP(rs.getInt("valreg"), rs.getString("paziente"));
					ListaDBP.add(obj);
				}
				stmt.close();
			}
	        c.close();
	        System.out.println("Connessione chiusa");
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		return ListaDBP;
	}
	
	/**
	 * Metodo che estrae dalla BD le ultime 24 righe di S del paziente selezionato
	 * 
	 * @return lista di S
	 */ 
	public static ObservableList<S> get24S(String CF) {
		ObservableList<S> ListaS = FXCollections.observableArrayList();
		int n_righe = 0;
		
		try {
			Connection c = Database.Connessione();  
			
			PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS righeTot FROM Saturazione WHERE paziente = ?;" );
			stmt.setString(1, CF);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			n_righe = rs.getInt("righeTot");
			
			stmt.close();
			c.close();
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		try {
			Connection c = Database.Connessione(); 
			if (n_righe <= 24) {
				PreparedStatement stmtP = c.prepareStatement("SELECT paziente, valreg FROM Saturazione WHERE paziente = ?;");
				stmtP.setString(1, CF);
				ResultSet rs = stmtP.executeQuery();
				
				while (rs.next()) {
					S obj = new S(rs.getInt("valreg"), rs.getString("paziente"));
					ListaS.add(obj);
				}
				
				stmtP.close();
			}
			else {
				PreparedStatement stmt = c.prepareStatement("SELECT paziente, valreg FROM Saturazione WHERE paziente = ? LIMIT 60 OFFSET ?;" );
				stmt.setString(1, CF);
				stmt.setInt(2, n_righe-24);
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					S obj = new S(rs.getInt("valreg"), rs.getString("paziente"));
					ListaS.add(obj);
				}
				stmt.close();
			}
			
	        c.close();
	        System.out.println("Connessione chiusa");
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		return ListaS;
	}
	
	/**
	 * Metodo che estrae dalla BD le ultime 40 righe di T del paziente selezionato
	 * 
	 * @return lista di T
	 */ 
	public static ObservableList<T> get40T(String CF) {
		ObservableList<T> ListaT = FXCollections.observableArrayList();
		int n_righe = 0;
		
		try {
			Connection c = Database.Connessione();  
			
			PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS righeTot FROM Temperatura WHERE paziente = ?;" );
			stmt.setString(1, CF);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			n_righe = rs.getInt("righeTot");
			
			stmt.close();
			c.close();
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		try {
			Connection c = Database.Connessione(); 
			if (n_righe <= 40) {
				PreparedStatement stmtP = c.prepareStatement("SELECT paziente, valreg FROM Temperatura WHERE paziente = ?;");
				stmtP.setString(1, CF);
				ResultSet rs = stmtP.executeQuery();
				
				while (rs.next()) {
					T obj = new T(rs.getDouble("valreg"), rs.getString("paziente"));
					ListaT.add(obj);
				}
				
				stmtP.close();
			}
			else {
				PreparedStatement stmt = c.prepareStatement("SELECT paziente, valreg FROM Temperatura WHERE paziente = ? LIMIT 60 OFFSET ?;" );
				stmt.setString(1, CF);
				stmt.setDouble(2, n_righe-40);
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					T obj = new T(rs.getDouble("valreg"), rs.getString("paziente"));
					ListaT.add(obj);
				}
				stmt.close();
			}
			
	        c.close();
	        System.out.println("Connessione chiusa");
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		return ListaT;
	}
	
	/**
	 * Metodo che estrae dalla BD le ultime 120 righe di FC del paziente selezionato
	 * 
	 * @return lista di FC
	 */ 
	public static ObservableList<FC> get120FC(String CF) {
		ObservableList<FC> ListaFC = FXCollections.observableArrayList();
		int n_righe = 0;
		
		try {
			Connection c = Database.Connessione();  
			
			PreparedStatement stmt = c.prepareStatement("SELECT COUNT(*) AS righeTot FROM Frequenza WHERE paziente = ?;" );
			stmt.setString(1, CF);
			ResultSet rs = stmt.executeQuery();
			rs.next();
			n_righe = rs.getInt("righeTot");
			
			stmt.close();
			c.close();
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		try {
			Connection c = Database.Connessione(); 
			if (n_righe <= 120) {
				PreparedStatement stmtP = c.prepareStatement("SELECT paziente, valreg FROM Frequenza WHERE paziente = ?;");
				stmtP.setString(1, CF);
				ResultSet rs = stmtP.executeQuery();
				
				while (rs.next()) {
					FC obj = new FC(rs.getInt("valreg"), rs.getString("paziente"));
					ListaFC.add(obj);
				}
				
				stmtP.close();
			}
			else {
				PreparedStatement stmt = c.prepareStatement("SELECT paziente, valreg FROM Frequenza WHERE paziente = ? LIMIT 60 OFFSET ?;" );
				stmt.setString(1, CF);
				stmt.setInt(2, n_righe-40);
				ResultSet rs = stmt.executeQuery();
			
				while (rs.next()) {
					FC obj = new FC(rs.getInt("valreg"), rs.getString("paziente"));
					ListaFC.add(obj);
				}
				stmt.close();
			}
			
	        c.close();
	        System.out.println("Connessione chiusa");
		}  catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
		
		return ListaFC;
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
		
		ColonnaSBP.setCellValueFactory(new PropertyValueFactory<>("SBP"));
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaSBP.setItems(get60SBP(newValue.getCodSanitario().get())));
		
		ColonnaDBP.setCellValueFactory(new PropertyValueFactory<>("DBP"));
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaDBP.setItems(get60DBP(newValue.getCodSanitario().get())));
		
		ColonnaS.setCellValueFactory(new PropertyValueFactory<>("S"));
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaS.setItems(get24S(newValue.getCodSanitario().get())));

		ColonnaT.setCellValueFactory(new PropertyValueFactory<>("T"));
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaT.setItems(get40T(newValue.getCodSanitario().get())));
		
		ColonnaFC.setCellValueFactory(new PropertyValueFactory<>("FC"));
		TabellaPazienti.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> TabellaFC.setItems(get120FC(newValue.getCodSanitario().get())));
		
	}
}
