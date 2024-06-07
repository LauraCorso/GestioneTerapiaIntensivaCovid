package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Questa classe implementa l'oggetto Farmaco
 */
public class Farmaco {
	private SimpleStringProperty Farmaco;
	private SimpleStringProperty CF;
	
	public Farmaco(String Farmaco, String CF) {
		this.Farmaco = new SimpleStringProperty(Farmaco);
		this.CF = new SimpleStringProperty(CF);
	}
	
	public Farmaco() {
		this.Farmaco = new SimpleStringProperty("");
		this.CF = new SimpleStringProperty("");
	}	
	
	public String getFarmaco() {
		return this.Farmaco.get();
	}
	
	public SimpleStringProperty getFarmacoProperty() {
		return this.Farmaco;
	}
	
	public void setFarmaco(String Farmaco) {
		this.Farmaco = new SimpleStringProperty(Farmaco);
	}
	
	public String getCF() {
		return this.CF.get();
	}
	
	public SimpleStringProperty getCFProperty() {
		return this.CF;
	}
	
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}
