package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Questa classe implementa l'oggetto DBP (Diastolic Blood Pressure)
 */
public class DBP {
	private SimpleIntegerProperty DBP;
	private SimpleStringProperty CF;
	
	public DBP(int DBP, String CF) {
		this.DBP = new SimpleIntegerProperty(DBP);
		this.CF = new SimpleStringProperty(CF);
	}
	
	public DBP() {
		this.DBP = new SimpleIntegerProperty(0);
		this.CF = new SimpleStringProperty("");
	}
	
	public int getDBP() {
		return this.DBP.get();
	}
	
	public String getCF() {
		return this.CF.get();
	}
	
	public SimpleIntegerProperty getDBPProperty() {
		return this.DBP;
	}
	
	public SimpleStringProperty getCFProperty() {
		return this.CF;
	}
	
	public void setDBP(int DBP) {
		this.DBP = new SimpleIntegerProperty(DBP);
	}
	
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}
