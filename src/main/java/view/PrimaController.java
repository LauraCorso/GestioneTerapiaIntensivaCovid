package view;

import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.lang.Math;

import DB.Database;
import controller.Main;
import controller.UpdateDBP;
import controller.UpdateFC;
import controller.UpdateS;
import controller.UpdateSBP;
import controller.UpdateT;
import javafx.animation.PauseTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.PopupWindow;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.SBP;
import model.Paziente;
import model.DBP;
import model.S;
import model.FC;
import model.Infermiere;
import model.Medico;
import model.T;

public class PrimaController implements Initializable {
	@FXML
	private TableView<Paziente> TabellaPazienti;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCognome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaNome;
	
	@FXML
	private TableColumn<Paziente, String> ColonnaCF;
	
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
	
	@FXML
	private TextField Username;
	
	@FXML
	private PasswordField Password;
	
	@FXML
	private Button btnLogin; 
	
	@FXML
	private Label Conferma;
	
	// Definisco un servizio che si occupa di andare a leggere da DB
	// i nuovi valori di SBP
	private UpdateSBP servizioSBP;
	
	// Definisco un servizio che si occupa di andare a leggere da DB i nuovi valori di DBP
	private UpdateDBP servizioDBP;
	
	// Definisco un servizio che si occupa di andare a leggere da DB i nuovi valori di FC
	private UpdateFC servizioFC;
	
	// Definisco un servizio che si occupa di andare a leggere da DB i nuovi valori di S
	private UpdateS servizioS;
	
	// Definisco un servizio che si occupa di andare a leggere da DB i nuovi valori di T
	private UpdateT servizioT;
	
	// Definisco un oggetto SBP che prende il valore generato
	private SBP ValueSBP;
	
	// Valore ObservableBoolean che avverte se cambia il numero di pazienti
	private static SimpleBooleanProperty modificaNP = new SimpleBooleanProperty(false);
	
	// Stringa che memorizza il CodSanitario di un nuovo paziente inserito
	private static String nuovoP;
	
	// Stringa che memorizza il CodSanitario di un paziente dimesso
	private static SimpleStringProperty CFD = new SimpleStringProperty("default");
	
	// Stringa che memorizza il codice di chi fa il login
	private static String us;
	
	/**
	 * Metodo get di us
	 * 
	 * @return String us
	 */
	public static String getUs() {
		return us;
	}
	
	/**
	 * Metodo set di us
	 * 
	 * @param String us
	 */
	public static void setUs(String newUs) {
		us = new String(newUs);
	}
	
	/** 
	 * Metodo get di CFD
	 * 
	 * @return SimpleStringProperty CF paziente dimesso
	 */
	public static SimpleStringProperty getCFD() {
		return CFD;
	}
	
	/** 
	 * Metodo set di CFD
	 * 
	 */
	public static void setCFD(String CF) {
		CFD.set(CF);
	}
	
	/**
	 * Metodo get che ritorna il valore di modificaNP
	 * 
	 * @return boolean valore modificaNP
	 */
	public static boolean getModificaNP() {
		return modificaNP.get();
	}
	
	/**
	 * Metodo set che imposta il valore di modificaNP
	 * 
	 */
	public static void setModificaNP(boolean val) {
		modificaNP.set(val);
	}
	
	/**
	 * Metodo get che ritorna il valore di nuovoP
	 * 
	 * @return String nuovoP
	 */
	public static String getNuovoP() {
		return nuovoP;
	}
	
	/**
	 * Metodo set che imposta il valore di nuovoP
	 * 
	 */
	public static String setNuovoP(String nuovoCF) {
		nuovoP = new String(nuovoCF);
		return nuovoP;
	}
	
	/** 
	 * Metodo che estrae dalla BD tutti i pazienti e li mette in una lista
	 * 
	 * @return ObservableList<Paziente> ListaPazienti
	 */
	public static ObservableList<Paziente> getAllPazienti() {
		// Creo la lista di oggetti Paziente
		ObservableList<Paziente> listaPazienti = FXCollections.observableArrayList();
		
		// SELECT lista pazienti
		try {
			Connection c = Database.Connessione();  
	
			Statement stmt = c.createStatement();
			
			// Seleziono tutti i pazienti
			ResultSet rs = stmt.executeQuery( "SELECT * FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
			while ( rs.next() ) {
					String CodSanitario = rs.getString("CodSanitario");
					String  Cognome = rs.getString("Cognome");
					String  Nome = rs.getString("Nome");
					Calendar DataN  = Calendar.getInstance();
					DataN.setTime(rs.getDate("DataN"));
					String  LuogoN = rs.getString("LuogoN");
					
					Paziente p = new Paziente(CodSanitario, Cognome, Nome, DataN, LuogoN);
					listaPazienti.add(p);
			}
			stmt.close();
	        c.close();
	        System.out.println("Connessione chiusa");
			}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }

		return listaPazienti;
	}
	
	/**
	 * Metodo che estrae dalla BD tutti gli infermieri e li mette in una lista
	 * @return ObservableList<Infermiere> ListaInfermieri
	 */
	public static ObservableList<Infermiere> getAllInfermieri() {
    	ObservableList<Infermiere> ListaInfermieri = FXCollections.observableArrayList();
    	try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM Infermiere;" );
			while ( rs.next() ) {
					String id = rs.getString("Id");
					String  Cognome = rs.getString("Cognome");
					String  Nome = rs.getString("Nome");
					String  Password = rs.getString("Password");
					
					Infermiere i = new Infermiere(id, Cognome, Nome, Password);
					ListaInfermieri.add(i);
			}
			rs.close();
			stmt.close();
	        c.close();
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return ListaInfermieri;    
    }

	/**
	 * Metodo che estrae dalla BD tutti i medici e li mette in una lista
	 * @return ObservableList<Medico> ListaMedici
	 */
	public static ObservableList<Medico> getAllMedici() {
    	ObservableList<Medico> ListaMedici = FXCollections.observableArrayList();
    	try {
			Connection c = Database.Connessione();  
			c.setAutoCommit(false);

			Statement stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM Medico;" );
			while ( rs.next() ) {
					String id = rs.getString("Id");
					String  Cognome = rs.getString("Cognome");
					String  Nome = rs.getString("Nome");
					String  Specialita = rs.getString("Specialita");
					String  Password = rs.getString("Password");
					Boolean  Primario = rs.getBoolean("Primario");
					
					Medico m = new Medico(id, Cognome, Nome, Specialita, Password, Primario);
					ListaMedici.add(m);
			}
			rs.close();
			stmt.close();
	        c.close();
    	}
	    catch ( Exception e ) {
			System.err.println( e.getClass().getName()+": "+ e.getMessage() );
			System.exit(0);
	    }
    	return ListaMedici;    
    }
	
	/**
	 * Metodo che incrementa l'attributo Numero della Tabella Anomalie del DB.
	 * 
	 * @param String tipo parametro
	 * @param String CodSanitario (CF) del paziente
	 */
	private static void NuovaAnomalia(String tipo, String CF, String descrizione) {
		int n = 0;
		StringBuffer note = new StringBuffer();
		boolean trovateRighe = false;
		// Estraggo il numero di settimana dalla data corrente
		Calendar dataCorrente = Calendar.getInstance();
		int settimana = dataCorrente.get(Calendar.WEEK_OF_YEAR);
		int anno = dataCorrente.get(Calendar.YEAR);
		
		// Seleziono il numero di anomalie
		try {
			Connection c = Database.Connessione();
			
			PreparedStatement stmt = c.prepareStatement("SELECT Numero, Note FROM Anomalia WHERE Paziente = ? AND Tipo = ? AND Settimana = ? AND Anno = ?;");
			stmt.setString(1, CF);
			stmt.setString(2, tipo);
			stmt.setInt(3, settimana);
			stmt.setInt(4, anno);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				n = rs.getInt("Numero");
				note.append(rs.getString("Note"));
				trovateRighe = true;
			}
			
			stmt.close();
			c.close();
			System.out.println("Connessione chiusa!");
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// INSERT o UPDATE
		if (!trovateRighe) {
			try {
				Connection c = Database.Connessione();
				
				PreparedStatement stmt = c.prepareStatement("INSERT INTO Anomalia VALUES (?, ?, ?, ?, ?, ?);");
				stmt.setString(1, CF);
				stmt.setString(2, tipo);
				stmt.setInt(3, 1);
				stmt.setInt(4, settimana);
				stmt.setInt(5, anno);
				stmt.setString(6, (note.append(descrizione).append(", ").append("1").append(", ").append(String.valueOf(dataCorrente.get(Calendar.DAY_OF_MONTH))).append("/").append(String.valueOf(dataCorrente.get(Calendar.MONTH)+1)).append("/").append(String.valueOf(dataCorrente.get(Calendar.YEAR))).append(" ").append(String.valueOf(dataCorrente.get(Calendar.HOUR_OF_DAY))).append(":").append(String.valueOf(dataCorrente.get(Calendar.MINUTE))).append("\n")).toString());
				stmt.executeUpdate();
				
				stmt.close();
				c.close();
				System.out.println("Connessione chiusa!");
			} catch(Exception e) {
				System.out.println(e);
			}
		}
		else {
			try {
				Connection c = Database.Connessione();
				
				PreparedStatement stmt = c.prepareStatement("UPDATE Anomalia SET Numero = ?, Note = ? WHERE Paziente = ? AND Tipo = ? AND Settimana = ? AND Anno = ?;");
				stmt.setInt(1, n+1);
				stmt.setString(3, CF);
				stmt.setString(4, tipo);
				stmt.setInt(5, settimana);
				stmt.setInt(6, anno);
				stmt.setString(2, (note.append(" ").append(",").append(" - ").append(descrizione).append(", ").append(String.valueOf(n+1)).append(", ").append(String.valueOf(dataCorrente.get(Calendar.DAY_OF_MONTH))).append("/").append(String.valueOf(dataCorrente.get(Calendar.MONTH)+1)).append("/").append(String.valueOf(dataCorrente.get(Calendar.YEAR))).append(" ").append(String.valueOf(dataCorrente.get(Calendar.HOUR_OF_DAY))).append(":").append(String.valueOf(dataCorrente.get(Calendar.MINUTE))).append("\n")).toString());
				stmt.executeUpdate();
				
				stmt.close();
				c.close();
				System.out.println("Connessione chiusa!");
			} catch(Exception e) {
				System.out.println(e);
			}
		}
	}
	
	
	
	/**
	 * Metodo che inizializza la scena
	 */
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		/**
		 * Listener che controlla se viene inserito un nuovo paziente.
		 * Nel caso in cui un nuovo paziente venga inserito si aggiorna la view.
		 */
		modificaNP.addListener((observable, oldValue, newValue) -> {
			if(newValue) {
				// Aggiorno la tabella pazienti
				ColonnaCognome.setCellValueFactory(cellData -> cellData.getValue().getCognome());
				ColonnaNome.setCellValueFactory(cellData -> cellData.getValue().getNome());
				ColonnaCF.setCellValueFactory(cellData -> cellData.getValue().getCodSanitario());
				ObservableList<Paziente> listaP = getAllPazienti();
				TabellaPazienti.setItems(listaP);
				
				// Trovo la posizione del nuovo paziente nella lista
				int index = 0;
				boolean trovato = false;
				String cf = getNuovoP();
				while (index < listaP.size() && !trovato) {
					if (listaP.get(index).getCodSanitario().get().equals(cf)) {
						trovato = true;
						break;
					}
					index++;
				}
				
				// Aggiorno la tabella SBP
				ObservableList<SBP> listaSBP = TabellaSBP.getItems();
				listaSBP.add(index, new SBP(110, cf));
				TabellaSBP.setItems(listaSBP);
				
				// Aggiorno la tabella DBP
				ObservableList<DBP> listaDBP = TabellaDBP.getItems();
				listaDBP.add(index, new DBP(80, cf));
				TabellaDBP.setItems(listaDBP);
				
				// Aggiorno la tabella S
				ObservableList<S> listaS = TabellaS.getItems();
				listaS.add(index, new S(98, cf));
				TabellaS.setItems(listaS);
				
				// Aggiorno la tabella T
				ObservableList<T> listaT = TabellaT.getItems();
				listaT.add(index, new T(36.0, cf));
				TabellaT.setItems(listaT);
				
				// Aggiorno la tabella FC
				ObservableList<FC> listaFC = TabellaFC.getItems();
				listaFC.add(index, new FC(80, cf));
				TabellaFC.setItems(listaFC);
				
				// Inserisco i nuovi valori nella base di dati
				try {
					Connection c = Database.Connessione();
					PreparedStatement stSBP = c.prepareStatement("INSERT INTO SBP(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
					stSBP.setString(2, cf);
					stSBP.setInt(1, 110);
					stSBP.executeUpdate();
					stSBP.close();
					c.close();
					System.out.println("Connessione chiusa");
				} catch(Exception e) {
					System.out.println(e);
				}
				
				try {			
					Connection c = Database.Connessione();
					PreparedStatement stDBP = c.prepareStatement("INSERT INTO DBP(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
					stDBP.setString(2, cf);
					stDBP.setInt(1, 80);
					stDBP.executeUpdate();
					stDBP.close();
					c.close();
					System.out.println("Connessione chiusa");
				} catch(Exception e) {
					System.out.println(e);
				}
				
				try {
					Connection c = Database.Connessione();
					PreparedStatement stS = c.prepareStatement("INSERT INTO Saturazione(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
					stS.setString(2, cf);
					stS.setInt(1, 98);
					stS.executeUpdate();
					stS.close();
					c.close();
					System.out.println("Connessione chiusa");
				} catch(Exception e) {
					System.out.println(e);
				}
				
				try {	
					Connection c = Database.Connessione();
					PreparedStatement stT = c.prepareStatement("INSERT INTO temperatura(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
					stT.setString(2, cf);
					stT.setDouble(1, 36.0);
					stT.executeUpdate();
					stT.close();
					c.close();
					System.out.println("Connessione chiusa");
				} catch(Exception e) {
					System.out.println(e);
				}
					
				try {
					Connection c = Database.Connessione();
					PreparedStatement stFC = c.prepareStatement("INSERT INTO frequenza(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
					stFC.setString(2, cf);
					stFC.setInt(1, 80);
					stFC.executeUpdate();
					stFC.close();
					
					c.close();
					System.out.println("Connessione chiusa");
				} catch(Exception e) {
					System.out.println(e);
				}
				setModificaNP(false);
			}
		});
		
		/**
		 * Listener che controlla se un paziente viene dimesso.
		 * Nel caso in cui un paziente venga dimesso si aggiorna la view.
		 */
		CFD.addListener((observable, oldValue, newValue) -> {
			// Aggiorno la tabella pazienti
			ObservableList<Paziente> listaP = TabellaPazienti.getItems();
			int index = 0;
			boolean trovato = false;
			
			while (index < listaP.size() && !trovato) {
				if (listaP.get(index).getCodSanitario().get().equals(getCFD().get())) {
					trovato = true;
					break;
				}
				index++;
			}
			
			TabellaPazienti.setItems(getAllPazienti());
			
			// Aggiorno la tabella SBP
			ObservableList<SBP> listaSBP = TabellaSBP.getItems();
			listaSBP.remove(index);
			TabellaSBP.setItems(listaSBP);
			
			// Aggiorno la tabella DBP
			ObservableList<DBP> listaDBP = TabellaDBP.getItems();
			listaDBP.remove(index);
			TabellaDBP.setItems(listaDBP);
			
			// Aggiorno la tabella S
			ObservableList<S> listaS = TabellaS.getItems();
			listaS.remove(index);
			TabellaS.setItems(listaS);
			
			// Aggiorno la tabella T
			ObservableList<T> listaT = TabellaT.getItems();
			listaT.remove(index);
			TabellaT.setItems(listaT);
			
			// Aggiorno la tabella FC
			ObservableList<FC> listaFC = TabellaFC.getItems();
			listaFC.remove(index);
			TabellaFC.setItems(listaFC);
		});
		
		/**
		 * Inizializzazione della tabella dei pazienti
		 */ 
		ObservableList<Paziente> lista = getAllPazienti();
		ColonnaCognome.setCellValueFactory(cellData -> cellData.getValue().getCognome());
		ColonnaNome.setCellValueFactory(cellData -> cellData.getValue().getNome());
		ColonnaCF.setCellValueFactory(cellData -> cellData.getValue().getCodSanitario());
		TabellaPazienti.setItems(lista);		
		
		/**
		 * Inizializzazione e aggiornamento della tabella della SBP ogni 2 min
		 */
		servizioSBP = new UpdateSBP();
		servizioSBP.setPeriod(Duration.minutes(2.0));
		servizioSBP.setOnSucceeded(
				e -> {
					ColonnaSBP.setCellValueFactory(new PropertyValueFactory<>("SBP"));
					TabellaSBP.setItems(servizioSBP.getValue());
					
					// Allarme
					for(int i = 0; i < servizioSBP.getValue().size(); i++) {
						if(servizioSBP.getValue().get(i).getSBP() > 150) {
							Alert allarmeSBP = new Alert(Alert.AlertType.WARNING);
							allarmeSBP.setTitle("ALLARME!");
							allarmeSBP.setHeaderText(null);
							allarmeSBP.setContentText("Il paziente " + servizioSBP.getValue().get(i).getCF() + " e' iperteso!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeSBP.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeSBP.show();
							toolkit.beep();
							
							//TO DO: modificare in 3 minuti
							PauseTransition pausa = new PauseTransition(Duration.minutes(3.0));
							pausa.setOnFinished(event -> {
								allarmeSBP.close();
							});
							
							pausa.play();
							
							NuovaAnomalia("SBP", servizioSBP.getValue().get(i).getCF(), "ipertensione");
						}
					}
				}
		);
				
		servizioSBP.start();
		
		/**
		 * Inizializzazione e aggiornamento della tabella della DBP ogni 2 min
		 */
		servizioDBP = new UpdateDBP();
		servizioDBP.setPeriod(Duration.minutes(2.0));
		servizioDBP.setOnSucceeded(
				e -> {
					ColonnaDBP.setCellValueFactory(new PropertyValueFactory<>("DBP"));
					TabellaDBP.setItems(servizioDBP.getValue());
					
					// Allarme
					for(int i = 0; i < servizioDBP.getValue().size(); i++) {
						if(servizioDBP.getValue().get(i).getDBP() < 90) {
							Alert allarmeDBP = new Alert(Alert.AlertType.WARNING);
							allarmeDBP.setTitle("ALLARME!");
							allarmeDBP.setHeaderText(null);
							allarmeDBP.setContentText("Il paziente " + servizioDBP.getValue().get(i).getCF() + " e' ipoteso!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeDBP.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeDBP.show();
							toolkit.beep();
							
							//TO DO: modificare in 3 minuti
							PauseTransition pausa = new PauseTransition(Duration.minutes(3.0));
							pausa.setOnFinished(event -> {
								allarmeDBP.close();
							});
							
							pausa.play();
							
							NuovaAnomalia("DBP", servizioDBP.getValue().get(i).getCF(), "ipotensione");
						}
					}
				}
		);
		
		servizioDBP.start();
		
		/**
		 * Inizializzazione e aggiornamento della tabella della FC ogni 1 minuto
		 */
		servizioFC = new UpdateFC();
		servizioFC.setPeriod(Duration.minutes(1.0));
		servizioFC.setOnSucceeded(
				e -> {
					ColonnaFC.setCellValueFactory(new PropertyValueFactory<>("FC"));
					
					TabellaFC.setItems(servizioFC.getValue());
					
					// Allarme
					for(int i = 0; i < servizioFC.getValue().size(); i++) {
						
						//Connessione al DB
						double media = 0.0;
						
						try {
							Connection c = Database.Connessione();  
							c.setAutoCommit(false);

							PreparedStatement stmt = c.prepareStatement("SELECT AVG(valReg) AS Media FROM frequenza WHERE Paziente = ?;");
							stmt.setString(1, servizioFC.getValue().get(i).getCF());
							
							ResultSet rs = stmt.executeQuery();
							rs.next();
							media = rs.getInt("Media");
							
							rs.close();
							stmt.close();
					        c.close();
				    	}
					    catch ( Exception ex ) {
							System.err.println( e.getClass().getName()+": "+ ex.getMessage() );
							System.exit(0);
					    } 
						
						if(java.lang.Math.abs(media - servizioFC.getValue().get(i).getFC()) > 80) {
							Alert allarmeTipo = new Alert(Alert.AlertType.WARNING);
							allarmeTipo.setTitle("ALLARME!");
							allarmeTipo.setHeaderText(null);
							allarmeTipo.setContentText("Il paziente " + servizioFC.getValue().get(i).getCF() + " è in aritmia!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeTipo.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeTipo.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(2.0));
							pausa.setOnFinished(event -> {
								allarmeTipo.close();
							});
							
							pausa.play();
							NuovaAnomalia("FRC", servizioFC.getValue().get(i).getCF(), "aritmia");
						}
						else if (servizioFC.getValue().get(i).getFC() > 160) {
							Alert allarmeTiper = new Alert(Alert.AlertType.WARNING);
							allarmeTiper.setTitle("ALLARME!");
							allarmeTiper.setHeaderText(null);
							allarmeTiper.setContentText("Il paziente " + servizioFC.getValue().get(i).getCF() + " ha un flutter ventricolare!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeTiper.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeTiper.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(2.0));
							pausa.setOnFinished(event -> {
								allarmeTiper.close();
							});
							
							pausa.play();
							NuovaAnomalia("FRC", servizioFC.getValue().get(i).getCF(), "flutter ventricolare");
						}
						else if (servizioFC.getValue().get(i).getFC() > 100) {
							Alert allarmeTiper = new Alert(Alert.AlertType.WARNING);
							allarmeTiper.setTitle("ALLARME!");
							allarmeTiper.setHeaderText(null);
							allarmeTiper.setContentText("Il paziente " + servizioFC.getValue().get(i).getCF() + " è in tachicardia!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeTiper.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeTiper.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(2.0));
							pausa.setOnFinished(event -> {
								allarmeTiper.close();
							});
							
							pausa.play();
							NuovaAnomalia("FRC", servizioFC.getValue().get(i).getCF(), "tachicardia");
						}
					}
				}
		);
				
		servizioFC.start();
		
		/**
		 * Inizializzazione e aggiornamento della tabella S ogni 5 min
		 */
		servizioS = new UpdateS();
		servizioS.setPeriod(Duration.minutes(5.0));
		servizioS.setOnSucceeded(
				e -> {
					ColonnaS.setCellValueFactory(new PropertyValueFactory<>("S"));
					TabellaS.setItems(servizioS.getValue());
					
					// Allarme
					for(int i = 0; i < servizioS.getValue().size(); i++) {
						if(servizioS.getValue().get(i).getS() < 90) {
							Alert allarmeS = new Alert(Alert.AlertType.WARNING);
							allarmeS.setTitle("ALLARME!");
							allarmeS.setHeaderText(null);
							allarmeS.setContentText("Il paziente " + servizioS.getValue().get(i).getCF() + " è dispnoico!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeS.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeS.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(1.0));
							pausa.setOnFinished(event -> {
								allarmeS.close();
							});
							
							pausa.play();
							NuovaAnomalia("SAT", servizioS.getValue().get(i).getCF(), "dispnea");
						}
					}
				}
		);
		servizioS.start();
		
		/**
		 * Inizializzazione e aggiornamento della tabella T ogni 3 min
		 */
		servizioT = new UpdateT();
		servizioT.setPeriod(Duration.minutes(3.0));
		servizioT.setOnSucceeded(
				e -> {
					ColonnaT.setCellValueFactory(new PropertyValueFactory<T, Double>("T"));
					// Visualizzo numeri Double con una sola cifra decimale
					ColonnaT.setCellFactory(tc -> new TableCell<T, Double>() {
					    @Override
					    protected void updateItem(Double t, boolean empty) {
					        super.updateItem(t, empty);
					        if (empty) {
					            setText(null);
					        } else {
					            setText(String.format("%.1f", t.doubleValue()));
					        }
					    }
					});
					TabellaT.setItems(servizioT.getValue());
					
					// Allarme
					for(int i = 0; i < servizioT.getValue().size(); i++) {
						if(servizioT.getValue().get(i).getT() < 36.0) {
							Alert allarmeTipo = new Alert(Alert.AlertType.WARNING);
							allarmeTipo.setTitle("ALLARME!");
							allarmeTipo.setHeaderText(null);
							allarmeTipo.setContentText("Il paziente " + servizioT.getValue().get(i).getCF() + " è in ipotermia!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeTipo.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeTipo.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(2.0));
							pausa.setOnFinished(event -> {
								allarmeTipo.close();
							});
							
							pausa.play();
							NuovaAnomalia("TMP", servizioT.getValue().get(i).getCF(), "ipotermia");
							
						}
						else if (servizioT.getValue().get(i).getT() > 36.9) {
							Alert allarmeTiper = new Alert(Alert.AlertType.WARNING);
							allarmeTiper.setTitle("ALLARME!");
							allarmeTiper.setHeaderText(null);
							allarmeTiper.setContentText("Il paziente " + servizioT.getValue().get(i).getCF() + " è in ipertermia!");
							ButtonType pulsante = new ButtonType("Risolto", ButtonData.CANCEL_CLOSE);
							allarmeTiper.getButtonTypes().setAll(pulsante);
							Toolkit toolkit = Toolkit.getDefaultToolkit();
							allarmeTiper.show();
							toolkit.beep();
							
							PauseTransition pausa = new PauseTransition(Duration.minutes(2.0));
							pausa.setOnFinished(event -> {
								allarmeTiper.close();
							});
							
							pausa.play();
							NuovaAnomalia("TMP", servizioT.getValue().get(i).getCF(), "ipertermia");
						}
					}
				}
		);
		servizioT.start();
		
		//-------------------------------- LOGIN -------------------------------------------
		btnLogin.setOnAction(event -> {
			// Recupero lo username e la password inseriti dall'utente
			setUs(Username.getText().toString());
			String psw = Password.getText().toString();
			
			// Recupero il primo carattere dello username
			char tipoUtente = us.charAt(0);
			
			// Se è i = INFERMIERE
			if (tipoUtente == 'I') {
				// Creo la lista degli infermieri dal DB
				ObservableList<Infermiere> ListaInf = getAllInfermieri();
				boolean trovato = false;
				
				// Cerco l'infermiere inserito nella lista di quelli consentiti
				for (int i = 0; i < ListaInf.size(); i++) {
					if (ListaInf.get(i).getId().equals(us) && ListaInf.get(i).getPassword().equals(psw)) {
						trovato = true;
						break;
					}
				}
				
				// Se l'infermiere esiste
				if (trovato) {
					Conferma.setText("Accesso consentito!");
					Conferma.setTextFill(Color.GREEN);
					
					// POP - UP
					final Stage popup = new Stage();
					popup.initModality(Modality.APPLICATION_MODAL);
					popup.initOwner(Main.getPrimaryStage());
					popup.setTitle("Infermiere: " + us);
					FXMLLoader loader = new FXMLLoader();
		            loader.setLocation(Main.class.getResource("/view/PopUpI.fxml"));
		            AnchorPane popUppy;
					try {
						popUppy = (AnchorPane) loader.load();
						Scene scene = new Scene(popUppy);
			            popup.setScene(scene);
			            popup.show();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				else {
					Conferma.setText("Accesso negato!");
					Conferma.setTextFill(Color.RED);
				}
			}
			// se è m = MEDICO
			else if (tipoUtente == 'M') {
				// Creo la lista dei medici dal DB
				ObservableList<Medico> ListaMed = getAllMedici();
				boolean trovato = false;
				
				// Cerco il medico inserito nella lista di quelli consentiti
				int i = 0;
				for (i = 0; i < ListaMed.size(); i++) {
					if (ListaMed.get(i).getId().equals(us) && ListaMed.get(i).getPassword().equals(psw)) {
						trovato = true;
						break;
					}
				}
				
				// Se il medico esiste
				if (trovato) {
					Conferma.setText("Accesso consentito!");
					Conferma.setTextFill(Color.GREEN);
					
					// Se è primario
					if (ListaMed.get(i).getPropertyPrimario().get()) {
						// POP - UP
						final Stage popup = new Stage();
						popup.initModality(Modality.APPLICATION_MODAL);
						popup.initOwner(Main.getPrimaryStage());
						popup.setTitle("Medico: " + us);
						FXMLLoader loader = new FXMLLoader();
			            loader.setLocation(Main.class.getResource("/view/PopUpMPrimario.fxml"));
			            AnchorPane popUppy;
						try {
							popUppy = (AnchorPane) loader.load();
							Scene scene = new Scene(popUppy);
				            popup.setScene(scene);
				            popup.show();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					} else {
						// POP - UP
						final Stage popup = new Stage();
						popup.initModality(Modality.APPLICATION_MODAL);
						popup.initOwner(Main.getPrimaryStage());
						popup.setTitle("Medico: " + us);
						FXMLLoader loader = new FXMLLoader();
			            loader.setLocation(Main.class.getResource("/view/PopUpM.fxml"));
			            AnchorPane popUppy;
						try {
							popUppy = (AnchorPane) loader.load();
							Scene scene = new Scene(popUppy);
				            popup.setScene(scene);
				            popup.show();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				else {
					Conferma.setText("Accesso negato!");
					Conferma.setTextFill(Color.RED);
				}
			}
			// altrimenti
			else {
				Conferma.setText("Accesso negato!");
				Conferma.setTextFill(Color.RED);
			}
		});
	}
}
