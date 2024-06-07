package model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Infermiere {
	private SimpleStringProperty id;
	private SimpleStringProperty Cognome;
	private SimpleStringProperty Nome;
	private SimpleStringProperty Password;
	
	/** 
	 * Costruttore
	 * 
	 * @param id
	 * @param cognome
	 * @param nome
	 * @param password
	 */
	public Infermiere(String id, String cognome, String nome, String password) {
		this.id = new SimpleStringProperty(id);
		this.Cognome = new SimpleStringProperty(cognome);
		this.Nome = new SimpleStringProperty(nome);
		this.Password = new SimpleStringProperty(password);
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

	public String getPassword() {
		return Password.get();
	}
	
	public SimpleStringProperty getPropertyPassword() {
		return Password;
	}

	public void setPassword(SimpleStringProperty password) {
		Password = password;
	}
}
