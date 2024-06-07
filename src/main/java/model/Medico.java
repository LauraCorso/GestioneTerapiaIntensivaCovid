package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Medico {
	private SimpleStringProperty id;
	private SimpleStringProperty Cognome;
	private SimpleStringProperty Nome;
	private SimpleStringProperty Specialita;
	private SimpleStringProperty Password;
	private SimpleBooleanProperty Primario;
	
	/** 
	 * Costruttore
	 * 
	 * @param id
	 * @param cognome
	 * @param nome
	 * @param specialita
	 * @param password
	 * @param primario
	 */
	public Medico(String id, String cognome, String nome, String specialita, String password, Boolean primario) {
		this.id = new SimpleStringProperty(id);
		this.Cognome = new SimpleStringProperty(cognome);
		this.Nome = new SimpleStringProperty(nome);
		this.Specialita = new SimpleStringProperty(specialita);
		this.Password = new SimpleStringProperty(password);
		this.Primario = new SimpleBooleanProperty(primario);
	}

	public SimpleStringProperty getPropertyId() {
		return id;
	}

	public String getId() {
		return id.get();
	}

	
	public void setId(SimpleStringProperty id) {
		this.id = id;
	}
	
	public String getCognome() {
		return Cognome.get();
	}

	public SimpleStringProperty getPropertyCognome() {
		return Cognome;
	}

	public void setCognome(SimpleStringProperty cognome) {
		Cognome = cognome;
	}

	public String getNome() {
		return Nome.get();
	}
	
	public SimpleStringProperty getPropertyNome() {
		return Nome;
	}

	public void setNome(SimpleStringProperty nome) {
		Nome = nome;
	}
	
	public String getSpecialita() {
		return Specialita.get();
	}

	public SimpleStringProperty getPropertySpecialita() {
		return Specialita;
	}

	public void setSpecialita(SimpleStringProperty specialita) {
		Specialita = specialita;
	}

	public String getPassword() {
		return Password.get();
	}
	
	public SimpleStringProperty getPropertyPassword() {
		return Password;
	}

	public void setPassword(SimpleStringProperty password) {
		Password = password;
	}
	
	public Boolean getPrimario() {
		return Primario.get();
	}
	
	public SimpleBooleanProperty getPropertyPrimario() {
		return Primario;
	}

	public void setPrimario(SimpleBooleanProperty primario) {
		Primario = primario;
	}
}

