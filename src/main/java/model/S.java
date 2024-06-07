package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * 
 * Questa classe implementa l'oggetto S (Saturazione)
 *
 */
public class S {
	private SimpleIntegerProperty S;
	private SimpleStringProperty CF;
	
	/**
	 * Costruttore
	 * @param S
	 */
	public S(int S, String CF) {
		this.S = new SimpleIntegerProperty(S);
		this.CF = new SimpleStringProperty(CF);
	}
	
	/**
	 * Costruttore vuoto
	 */
	public S() {
		this.S = new SimpleIntegerProperty(0);
		this.CF = new SimpleStringProperty("");
	}
	
	/**
	 * Metodo get del valore
	 * @return S
	 */
	public int getS() {
		return this.S.get();
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
	 * @return S
	 */
	public SimpleIntegerProperty getSProperty() {
		return this.S;
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
	 * @param S
	 */
	public void setS(int S) {
		this.S = new SimpleIntegerProperty(S);
	}
	
	/**
	 * Metodo set del codice fiscale
	 * @param CF
	 */
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}