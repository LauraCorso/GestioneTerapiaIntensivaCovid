package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Random;

import DB.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import model.Paziente;
import model.T;

/**
 * Questa classe è un servizio che calcola randomicamente un valore T
 *
 */
public class UpdateT extends ScheduledService<ObservableList<T>> {
	
	/**
	 * Creazione di un numero double casuale in un range
	 * @return numero double casuale 
	 */
	public static double randDouble(double min, double max) {	
		  Random rand = new Random();
		  double randomNum = min + (max - min)*rand.nextDouble();
		  return randomNum;
	}
	
	 @Override
	 protected Task<ObservableList<T>> createTask() {
	      return new Task<>() {

	    	  @Override
				protected ObservableList<T> call() throws Exception {
					// INSERT in ogni tabella parametro di un valore per ogni paziente
		        	// SELECT degli ultimi valori inseriti --> eventualmente
		        	// Creo la lista
		        	// FOR creo un oggetto parametro per ogni riga e popola la lista --> eventualmente
		        	// Nota: questa lista si crea ad ogni esecuzione del task
					
					// Creo la lista di oggetti T
					ObservableList<T> listaT = FXCollections.observableArrayList();
					
					// SELECT CF dei Pazienti
					try {
						Connection c = Database.Connessione();  

						Statement stmt = c.createStatement();
						
						boolean temp = false;
						double nuovoVal = 0;
						
						// Seleziono tutti i CF dei pazienti
						ResultSet rs = stmt.executeQuery( "SELECT CodSanitario FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
						while (rs.next()) {
								// Per ogni CF estratto dalla tabella pazienti creo un oggetto SBP
								String CodSanitario = rs.getString("CodSanitario");
								
								if(temp == false) {
									nuovoVal = randDouble(34.0, 42.0);
									temp = true;
								} else {
									nuovoVal = randDouble(36.0, 36.5);
								}
								
								T t = new T(nuovoVal, CodSanitario);
					
								// e lo aggiungo alla lista
								listaT.add(t);
						}
						stmt.close();
				        c.close();
				        System.out.println("Connessione chiusa");
	    	  		}
				    catch ( Exception e ) {
						System.err.println( e.getClass().getName()+": "+ e.getMessage() );
						System.exit(0);
				    }
					
					// INSERT della lista nel DB nella tabella temperatura
					try {
						Connection c = Database.Connessione();
						PreparedStatement ps = c.prepareStatement("INSERT INTO temperatura(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
						
						for (int i = 0; i < listaT.size(); i++) {
								ps.setDouble(1, listaT.get(i).getT());
								ps.setString(2, listaT.get(i).getCF());
								int nRighe = ps.executeUpdate();
						}
						ps.close();
				        c.close();
				        System.out.println("Connessione chiusa");
	    	  		}
				    catch ( Exception e ) {
						System.err.println( e.getClass().getName()+": "+ e.getMessage() );
						System.exit(0);
				    }
					
					/* Stampa della lista
					for (int i = 0; i < listaSBP.size(); i++) {
						System.out.println("CF " + i +": " + listaSBP.get(i).getCF());
						System.out.println("	SBP " + i +": " + listaSBP.get(i).getSBP());
					} */
					
					return listaT;
	        }
	      };
	    }
}
