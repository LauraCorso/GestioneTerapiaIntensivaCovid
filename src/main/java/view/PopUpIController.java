package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.DBP;
import model.FC;
import model.Infermiere;
import model.S;
import model.SBP;
import model.T;

public class PopUpIController implements Initializable {
	@FXML
	private Button NuovaS;
	
	@FXML
	private Button ListaP;
	
	@FXML
	private Button ListaS;
	
	@FXML
	private Button NuovoP;
	
	@FXML
	private Button Ricoveri;
	
	@FXML
	private Button LogOut;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// Controllo del pulsante Nuova Somministrazione --> inserisce una nuova somministrazione
		NuovaS.setOnAction(events -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)events.getSource()).getScene().getWindow())));
			popup.setTitle("Nuova somministrazione");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpNuovaSomministrazione.fxml"));
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
		
		// Controllo del pulsante Nuovo Paziente --> inserisce un nuovo paziente
		NuovoP.setOnAction(eventp -> {
			// POP - UP
			final Stage popup = new Stage();
			popup.initModality(Modality.APPLICATION_MODAL);
			popup.initOwner(((Stage)(((Button)eventp.getSource()).getScene().getWindow())));
			popup.setTitle("Nuovo paziente");
			FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/view/PopUpNuovoPaziente.fxml"));
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
