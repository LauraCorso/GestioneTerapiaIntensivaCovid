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

public class Ricovero {
	private SimpleStringProperty Paziente;
	private SimpleStringProperty Ospedale;
	private SimpleStringProperty Reparto;
	private SimpleStringProperty Data;
		
	/**
	 * Costruttore
	 * 
	 * @param Paziente
	 * @param Ospedale
	 * @param Reparto
	 * @param Data
	 */
	public Ricovero(String Paziente, String Ospedale, String Reparto, String Data) {
		this.Paziente = new SimpleStringProperty(Paziente);
		this.Ospedale = new SimpleStringProperty(Ospedale);
		this.Reparto = new SimpleStringProperty(Reparto);
		this.Data = new SimpleStringProperty(Data);
	}

	public SimpleStringProperty getPaziente() {
		return this.Paziente;
	}
	
	public SimpleStringProperty getOspedale() {
		return this.Ospedale;
	}
	
	public SimpleStringProperty getReparto() {
		return this.Reparto;
	}
	
	public SimpleStringProperty getData() {
		return this.Data;
	}
}
