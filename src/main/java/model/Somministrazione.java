package model;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Somministrazione {
	private SimpleStringProperty Paziente;
	private SimpleStringProperty Infermiere;
	private SimpleStringProperty Farmaco;
	private SimpleStringProperty Data;
	private SimpleObjectProperty<Time> Ora;
	private SimpleStringProperty Dose;
	private SimpleStringProperty Note;
	
	/**
	 * Costruttore
	 * 
	 * @param Paziente
	 * @param Infermiere
	 * @param Farmaco
	 * @param Data
	 * @param Ora
	 * @param Dose
	 * @param Note
	 */
	public Somministrazione(String Paziente, String Infermiere, String Farmaco, String Data, Time Ora, String Dose, String Note) {
		this.Paziente = new SimpleStringProperty(Paziente);
		this.Infermiere = new SimpleStringProperty(Infermiere);
		this.Farmaco = new SimpleStringProperty(Farmaco);
		this.Data = new SimpleStringProperty(Data);
		this.Ora = new SimpleObjectProperty<Time>(Ora);
		this.Dose = new SimpleStringProperty(Dose);
		this.Note = new SimpleStringProperty(Note);
	}

	public SimpleStringProperty getPaziente() {
		return this.Paziente;
	}
	
	public SimpleStringProperty getInfermiere() {
		return this.Infermiere;
	}
	
	public SimpleStringProperty getFarmaco() {
		return this.Farmaco;
	}
	
	public SimpleStringProperty getData() {
		return this.Data;
	}
	
	public SimpleObjectProperty<Time> getOra() {
		return this.Ora;
	}
	
	public SimpleStringProperty getDose() {
		return this.Dose;
	}
	
	public SimpleStringProperty getNote() {
		return this.Note;
	}
}
