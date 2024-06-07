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

public class UpdatePazienti extends ScheduledService<ObservableList<Paziente>> {
	
	 @Override
	 protected Task<ObservableList<Paziente>> createTask() {
	      return new Task<>() {

	    	  @Override
				protected ObservableList<Paziente> call() throws Exception {
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
					
					/* Stampa della lista
					for (int i = 0; i < listaSBP.size(); i++) {
						System.out.println("CF " + i +": " + listaSBP.get(i).getCF());
						System.out.println("	SBP " + i +": " + listaSBP.get(i).getSBP());
					} */
					
					return listaPazienti;
	        }
	      };
	    }
}
