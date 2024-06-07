package view;

import java.io.*;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;

import DB.Database;
import controller.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopUpMPrimarioController implements Initializable {
	@FXML
	private Button NuovaP;
	
	@FXML
	private Button ListaP;
	
	@FXML
	private Button ListaS;
	
	@FXML
	private Button DimissioneP;
	
	@FXML
	private Button Report;
	
	@FXML
	private Button Ricoveri;
	
	@FXML
	private Button LogOut;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Controllo del pulsante Nuova Prescrizione --> inserisce una nuova prescrizione
		NuovaP.setOnAction(eventp -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventp.getSource()).getScene().getWindow())));
			popup.setTitle("Nuova prescrizione");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpNuovaPrescrizione.fxml"));
            AnchorPane popUppy;
			try {
				popUppy = (AnchorPane) loader.load();
				Scene scene = new Scene(popUppy);
	            popup.setScene(scene);
	            popup.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// Controllo del pulsante Lista Parametri --> visualizza i parametri delle ultime due ore
		ListaP.setOnAction(eventlp -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventlp.getSource()).getScene().getWindow())));
			popup.setTitle("Lista parametri");
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(Main.class.getResource("/view/PopUpListaParametri.fxml"));
	        AnchorPane popUppy;
			try {
				popUppy = (AnchorPane) loader.load();
				Scene scene = new Scene(popUppy);
	            popup.setScene(scene);
	            popup.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// Controllo del pulsante Lista Somministrazioni --> visualizza le somministrazioni degli ultimi due giorni
		ListaS.setOnAction(eventls -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventls.getSource()).getScene().getWindow())));
			popup.setTitle("Lista somministrazioni");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpListaSomministrazioni.fxml"));
            AnchorPane popUppy;
			try {
				popUppy = (AnchorPane) loader.load();
				Scene scene = new Scene(popUppy);
	            popup.setScene(scene);
	            popup.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// Controllo del pulsante Dimissione Paziente --> dimette un paziente
		DimissioneP.setOnAction(eventp -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventp.getSource()).getScene().getWindow())));
			popup.setTitle("Dimissione paziente");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpDimissionePaziente.fxml"));
            AnchorPane popUppy;
			try {
				popUppy = (AnchorPane) loader.load();
				Scene scene = new Scene(popUppy);
	            popup.setScene(scene);
	            popup.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// Controllo del pulsante Report --> stampa report settimanale in un file.csv
		Report.setOnAction(eventR -> {
			// Finestra di dialogo del sistema per il salvataggio del file
			FileChooser fc = new FileChooser();
			fc.setInitialFileName("Report_" + String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) + "." + String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1) + "." + String.valueOf(Calendar.getInstance().get(Calendar.YEAR)) + ".csv"); //nome iniziale del file
			File report = fc.showSaveDialog(((Stage)(((Button)eventR.getSource()).getScene().getWindow())));
			
			StringBuffer sb = new StringBuffer();
			int settimana = 0;
			String Paz = new String(" ");
			
			// Riempimento del file
			if (report != null) {
				try {
					PrintWriter fw = new PrintWriter(report);
					
					Connection c = Database.Connessione();
					Statement stmt = c.createStatement();
					
					ResultSet rs = stmt.executeQuery("SELECT * FROM Anomalia A JOIN Paziente P ON A.Paziente = P.CodSanitario WHERE P.ricovero = true GROUP BY A.Settimana, A.Paziente, A.Tipo, A.Numero, A.Anno, P.CodSanitario ORDER BY A.Settimana, A.Paziente, A.Tipo;");
					while (rs.next()) {
						if (settimana != rs.getInt("Settimana")) {
							Calendar cal = Calendar.getInstance();
							cal.clear();
							cal.set(Calendar.WEEK_OF_YEAR, rs.getInt("Settimana"));
							cal.set(Calendar.YEAR, rs.getInt("Anno"));
							
							Paz = new String(rs.getString("Paziente"));
							
							sb.append(new String("Settimana dal " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)) + " al " + String.valueOf(cal.get(Calendar.DAY_OF_MONTH)+6) + " " + cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ITALY) + " " + String.valueOf(cal.get(Calendar.YEAR)) + "\n"));
							sb.append("Paziente, Tipo, Numero, Data\n");
							sb.append(new String(rs.getString("Paziente") + "," + rs.getString("Tipo") + "," + rs.getInt("Numero") + "\n"));
							sb.append(new String(" " + "," + " - " + rs.getString("Note")));
							
							settimana = rs.getInt("Settimana");
						}
						else {
							if (Paz.equals(rs.getString("Paziente"))) {
								sb.append(new String(" " + "," + rs.getString("Tipo") + "," + rs.getInt("Numero") + "\n"));
								sb.append(new String(" " + "," + " - " + rs.getString("Note")));
							}
							else {
								sb.append(new String(rs.getString("Paziente") + "," + rs.getString("Tipo") + "," + rs.getInt("Numero") + "\n"));
								sb.append(new String(" " + "," + " - " + rs.getString("Note")));
								Paz = new String(rs.getString("Paziente"));
							}
						}
						
					}
					
					fw.println(sb.toString());
					fw.close();
					
					stmt.close();
					c.close();
				} catch (Exception e) {
					System.out.println(e);
				}
			}
		});
		
		// Controllo del pulsante Ricoveri Pregressi --> visualizza i ricoveri pregressi dei pazienti attualmente ricoverati
		Ricoveri.setOnAction(eventr -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventr.getSource()).getScene().getWindow())));
			popup.setTitle("Ricoveri pregressi");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpRicoveriPregressi.fxml"));
            AnchorPane popUppy;
			try {
				popUppy = (AnchorPane) loader.load();
				Scene scene = new Scene(popUppy);
	            popup.setScene(scene);
	            popup.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
				
		// Controllo del pulsante Log Out --> chiude il pop up
		LogOut.setOnAction(event -> {
			 ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
		});
	}

}
