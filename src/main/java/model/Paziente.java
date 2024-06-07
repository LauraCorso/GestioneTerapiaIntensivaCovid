package model;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.lang.*;

public class Paziente {
	private SimpleStringProperty CodSanitario;
	private SimpleStringProperty Cognome;
	private SimpleStringProperty Nome;
	private SimpleObjectProperty<LocalDate> DataN;
	private SimpleStringProperty LuogoN;
	
	/**
	 * Costruttore
	 * 
	 * @param CodSanitario
	 * @param Cognome
	 * @param Nome
	 * @param DataN
	 * @param LuogoN
	 */
	public Paziente(String CodSanitario, String Cognome, String Nome, Calendar DataN, String LuogoN) {
		this.CodSanitario = new SimpleStringProperty(CodSanitario);
		this.Cognome = new SimpleStringProperty(Cognome);
		this.Nome = new SimpleStringProperty(Nome);
		this.DataN = new SimpleObjectProperty<LocalDate>(LocalDate.of(DataN.get(Calendar.YEAR), DataN.get(Calendar.MONTH)+1, DataN.get(Calendar.DAY_OF_MONTH)));
		this.LuogoN = new SimpleStringProperty(LuogoN);
	}
	
	public SimpleStringProperty getCodSanitario() {
		return this.CodSanitario;
	}
	
	public SimpleStringProperty getCognome() {
		return this.Cognome;
	}
	
	public SimpleStringProperty getNome() {
		return this.Nome;
	}
	
	public SimpleObjectProperty<LocalDate> getDataN() {
		return this.DataN;
	}
	
	public SimpleStringProperty getLuogoN() {
		return this.LuogoN;
	}
}
