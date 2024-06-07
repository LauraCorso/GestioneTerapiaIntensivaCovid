package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Questa classe implementa l'oggetto FC (Frequenza Cardiaca)
 */
public class FC {
	private SimpleIntegerProperty FC;
	private SimpleStringProperty CF;
	
	public FC(int FC, String CF) {
		this.FC = new SimpleIntegerProperty(FC);
		this.CF = new SimpleStringProperty(CF);
	}
	
	public FC() {
		this.FC = new SimpleIntegerProperty(0);
		this.CF = new SimpleStringProperty("");
	}
	
	public int getFC() {
		return this.FC.get();
	}
	
	public String getCF() {
		return this.CF.get();
	}
	
	public SimpleIntegerProperty getFCProperty() {
		return this.FC;
	}
	
	public SimpleStringProperty getCFProperty() {
		return this.CF;
	}
	
	public void setFC(int FC) {
		this.FC = new SimpleIntegerProperty(FC);
	}
	
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}