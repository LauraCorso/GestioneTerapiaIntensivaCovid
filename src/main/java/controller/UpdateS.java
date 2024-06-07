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
import model.S;

/**
 * Questa classe è un servizio che calcola randomicamente un valore S
 *
 */
public class UpdateS extends ScheduledService<ObservableList<S>> {
	
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
	 protected Task<ObservableList<S>> createTask() {
	      return new Task<>() {

	    	  @Override
				protected ObservableList<S> call() throws Exception {
					// INSERT in ogni tabella parametro di un valore per ogni paziente
		        	// SELECT degli ultimi valori inseriti --> eventualmente
		        	// Creo la lista
		        	// FOR creo un oggetto parametro per ogni riga e popola la lista --> eventualmente
		        	// Nota: questa lista si crea ad ogni esecuzione del task
					
					// Creo la lista di oggetti S
					ObservableList<S> listaS = FXCollections.observableArrayList();
					
					// SELECT CF dei Pazienti
					try {
						Connection c = Database.Connessione();  

						Statement stmt = c.createStatement();
						
						boolean dispnoico = false;
						int nuovoVal = 0;
						
						// Seleziono tutti i CF dei pazienti
						ResultSet rs = stmt.executeQuery( "SELECT CodSanitario FROM Paziente WHERE ricovero = true ORDER BY CodSanitario;" );
						while (rs.next()) {
								// Per ogni CF estratto dalla tabella pazienti creo un oggetto SBP
								String CodSanitario = rs.getString("CodSanitario");
								
								if(dispnoico == false) {
									nuovoVal = randInt(80, 100);
									dispnoico = true;
								} else {
									nuovoVal = randInt(90, 100);
								}
								S s = new S(nuovoVal, CodSanitario);
					
								// e lo aggiungo alla lista
								listaS.add(s);
						}
						stmt.close();
				        c.close();
				        System.out.println("Connessione chiusa");
	    	  		}
				    catch ( Exception e ) {
						System.err.println( e.getClass().getName()+": "+ e.getMessage() );
						System.exit(0);
				    }
					
					// INSERT della lista nel DB nella tabella S
					try {
						Connection c = Database.Connessione();
						PreparedStatement ps = c.prepareStatement("INSERT INTO saturazione(id, valreg, paziente) VALUES (DEFAULT, ?, ?);");
						
						for (int i = 0; i < listaS.size(); i++) {
								ps.setInt(1, listaS.get(i).getS());
								ps.setString(2, listaS.get(i).getCF());
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
					
					return listaS;
	        }
	      };
	    }
}
