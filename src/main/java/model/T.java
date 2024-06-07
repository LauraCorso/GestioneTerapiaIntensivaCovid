package model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Questa classe implementa l'oggetto T (Temperatura)
 */
public class T {
	private SimpleDoubleProperty T;
	private SimpleStringProperty CF;
	
	
	/**
	 * Costruttore
	 * @param T, CF
	 */
	public T(double T, String CF) {
		this.T = new SimpleDoubleProperty(T);
		this.CF = new SimpleStringProperty(CF);
	}
	
	/**
	 * Costruttore vuoto
	 * 
	 */
	public T() {
		this.T = new SimpleDoubleProperty(0.0);
		this.CF = new SimpleStringProperty("");
	}
	
	
	/**
	 * Metodo get del valore
	 * @return T
	 */
	public double getT() {
		return this.T.get();
	}
	
	/**
	 * Metodo get del codice fiscale
	 * @return CF
	 */
	public String getCF() {
		return this.CF.get();
	}
	
	/**
	 * Metodo get del valore come property
	 * @return T
	 */
	public SimpleDoubleProperty getTProperty() {
		return this.T;
	}
	
	/**
	 * Metodo get del codice fiscale come property
	 * @return CF
	 */
	public SimpleStringProperty getCFProperty() {
		return this.CF;
	}

	/**
	 * Metodo set del valore
	 * @param T
	 */
	public void setT(double T) {
		this.T = new SimpleDoubleProperty(T);
	}
	
	/**
	 * Metodo set del codice fiscale
	 * @param CF
	 */
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}

