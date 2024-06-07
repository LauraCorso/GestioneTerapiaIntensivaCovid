package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import DB.Database;
import javafx.beans.property.*;

/**
 * Questa classe implementa l'oggetto SBP (Sistolic Blood Pressure)
 */
public class SBP {
	private SimpleIntegerProperty SBP;
	private SimpleStringProperty CF;
	
	
	/**
	 * Costruttore
	 * @param SBP, CF
	 */
	public SBP(int SBP, String CF) {
		this.SBP = new SimpleIntegerProperty(SBP);
		this.CF = new SimpleStringProperty(CF);
	}
	
	/**
	 * Costruttore vuoto
	 * 
	 */
	public SBP() {
		this.SBP = new SimpleIntegerProperty(0);
		this.CF = new SimpleStringProperty("");
	}
	
	
	/**
	 * Metodo get del valore
	 * @return SBP
	 */
	public int getSBP() {
		return this.SBP.get();
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
	 * @return SBP
	 */
	public SimpleIntegerProperty getSBPProperty() {
		return this.SBP;
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
	 * @param SBP
	 */
	public void setSBP(int SBP) {
		this.SBP = new SimpleIntegerProperty(SBP);
	}
	
	/**
	 * Metodo set del codice fiscale
	 * @param CF
	 */
	public void setCF(String CF) {
		this.CF = new SimpleStringProperty(CF);
	}
}
