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
import model.FC;
import model.FC;

/**
 * Questa classe è un servizio che calcola randomicamente un valore FC
 *
 */
public class UpdateFC extends ScheduledService<ObservableList<FC>> {
	
	/**
	 * Creazione di un numero intero casuale in un range
	 * @return numero intero casuale 
	 */
	public static int randInt(int min, int max) {	
		  Random rand = new Random();
		  int randomNum = rand.nextInt((max - min) + 1) + min;
		  return randomNum;
	}
	
	 @Override
	 protected Task<ObservableList<FC>> createTask() {
	      return new Task<>() {

	    	  @Override
				protected ObservableList<FC> call() throws Exception {
					// INSERT in ogni tabella parametro di un valore per ogni paziente
		        	// SELECT degli ultimi valori inseriti --> eventualmente
		        	// Creo la lista
		        	// FOR creo un oggetto parametro per ogni riga e popola la lista --> eventualmente
		        	// Nota: questa lista si crea ad ogni esecuzione del task
					
					// Creo la lista di oggetti FC
					ObservableList<FC> listaFC = FXCollections.observableArrayList();
					
					// SELECT CF dei Pazienti
					try {
						Connection c = Database.Connessione();  

						Statement stmt = c.createStatement();
						
						boolean frequenza = false;
						int nuovoVal = 0;
						
						// Seleziono tutti i CF dei pazienti
						ResultSet rs = stmt.executeQuery( "SELECT CodSanitario FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
						while (rs.next()) {
								// Per ogni CF estratto dalla tabella pazienti creo un oggetto FC
								String CodSanitario = rs.getString("CodSanitario");
								
								if(frequenza == false) {
									nuovoVal = randInt(50, 180);
									frequenza = true;
								} else {
									nuovoVal = randInt(60, 80);
								}
								
								FC p = new FC(nuovoVal, CodSanitario);
					
								// e lo aggiungo alla lista
								listaFC.add(p);
						}
						stmt.close();
				        c.close();
				        System.out.println("Connessione chiusa");
	    	  		}
				    catch ( Exception e ) {
						System.err.println( e.getClass().getName()+": "+ e.getMessage() );
						System.exit(0);
				    }
					
					// INSERT della lista nel DB nella tabella FC
					try {
						Connection c = Database.Connessione();
						PreparedStatement ps = c.prepareStatement("INSERT INTO Frequenza(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
						
						for (int i = 0; i < listaFC.size(); i++) {
								ps.setInt(1, listaFC.get(i).getFC());
								ps.setString(2, listaFC.get(i).getCF());
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
					for (int i = 0; i < listaFC.size(); i++) {
						System.out.println("CF " + i +": " + listaFC.get(i).getCF());
						System.out.println("	FC " + i +": " + listaFC.get(i).getFC());
					} */
					return listaFC;
	        }
	      };
	    }
}
