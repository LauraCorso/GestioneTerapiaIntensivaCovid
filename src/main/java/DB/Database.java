package DB;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
	static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/terapiaintensiva";
	static final String USER = "postgres";
	static final String PASS = "admin";
	//static final String PASS = "elenaM9!";
		
	public static void CreazioneDB() throws SQLException {
		Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/postgres", USER, PASS); 
		Statement stmt = conn.createStatement();	  
		
		String sql = "CREATE DATABASE terapiaintensiva";
		stmt.executeUpdate(sql);
		
		System.out.println("Database creato con successo...");
	}
	
	public static Connection Connessione() throws Exception {
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS); 
		System.out.println("Connessione stabilita con successo...");
		
		return conn;
	}
	
	public static void Creazione() throws Exception {
		Connection conn = Connessione();
		Statement stmt = conn.createStatement();
		
		String query = "CREATE TABLE Paziente " +
						   "(CodSanitario VARCHAR(16) PRIMARY KEY," +
						   "Cognome VARCHAR(20) NOT NULL," +
						   "Nome VARCHAR(20) NOT NULL," +
						   "DataN DATE NOT NULL," +
						   "LuogoN VARCHAR(20)," +
						   "Ricovero BOOL NOT NULL DEFAULT true);" +
						   
					   "CREATE TABLE Reparto " +
							"(Nome VARCHAR(20)," +
							"Ospedale VARCHAR(60)," +
							"Provincia CHAR(2) NOT NULL,"+
							"PRIMARY KEY(Nome, Ospedale));" +
							
						"CREATE TABLE SBP " +
							"(Id SERIAL PRIMARY KEY," +
							"ValReg DECIMAL(4, 1) NOT NULL," +
							"Paziente VARCHAR(16) REFERENCES Paziente (CodSanitario));" +
							
						"CREATE TABLE DBP " +
							"(Id SERIAL PRIMARY KEY," +
							"ValReg DECIMAL(4, 1) NOT NULL," +
							"Paziente VARCHAR(16) REFERENCES Paziente (CodSanitario));" +	
							
						"CREATE TABLE Temperatura " +
							"(Id SERIAL PRIMARY KEY," +
							"ValReg DECIMAL(4, 1) NOT NULL," +
							"Paziente VARCHAR(16) REFERENCES Paziente (CodSanitario));" +	
							
						"CREATE TABLE Saturazione " +
							"(Id SERIAL PRIMARY KEY," +
							"ValReg DECIMAL(4, 1) NOT NULL," +
							"Paziente VARCHAR(16) REFERENCES Paziente (CodSanitario));" +
							
						"CREATE TABLE Frequenza " +
							"(Id SERIAL PRIMARY KEY," +
							"ValReg DECIMAL(4, 1) NOT NULL," +
							"Paziente VARCHAR(16) REFERENCES Paziente (CodSanitario));" +
					   
					   "CREATE TABLE Medico " +
							"(Id VARCHAR(4) PRIMARY KEY," +
							"Cognome VARCHAR(20) NOT NULL," +
							"Nome VARCHAR(20) NOT NULL," +
							"Specialita VARCHAR(20)," +
							"Password VARCHAR(8) NOT NULL," +
							"Primario BOOL NOT NULL);"+
							
					   "CREATE TABLE Farmaco " +
							"(Nome VARCHAR(20) PRIMARY KEY);" +
							
					   "CREATE TABLE Infermiere " +
						   "(Id VARCHAR(4) PRIMARY KEY," +
						   "Cognome VARCHAR(20) NOT NULL," +
						   "Nome VARCHAR(20) NOT NULL," +
						   "Password VARCHAR(8) NOT NULL);" +
						   
					   "CREATE TABLE Ricovero " +
						   "(Paziente VARCHAR(16) REFERENCES Paziente(CodSanitario)," +
						   "NomeRep VARCHAR(20)," +
						   "Ospedale VARCHAR(60)," +
						   "DataDiagnosi DATE NOT NULL," +
						   "PRIMARY KEY(Paziente, NomeRep, Ospedale),"+
						   "FOREIGN KEY (NomeRep, Ospedale) REFERENCES Reparto(Nome, Ospedale));" +
						   
					   "CREATE TABLE Prescrizione " +
						   "(Paziente VARCHAR(16) REFERENCES Paziente(CodSanitario)," +
						   "Medico VARCHAR(4) REFERENCES Medico(Id)," +
						   "Farmaco VARCHAR(20) REFERENCES Farmaco(Nome)," +
						   "Data DATE NOT NULL," +
						   "DosiGG INTEGER NOT NULL," +
						   "Quantita DECIMAL(5, 2) NOT NULL,"+
						   "Ventilazione BOOL NOT NULL,"+
						   "PRIMARY KEY(Paziente, Medico, Farmaco));"+
						   
					   "CREATE TABLE Somministrazione " +
						   "(Paziente VARCHAR(16) REFERENCES Paziente(CodSanitario)," +
						   "Infermiere VARCHAR(4) REFERENCES Infermiere(Id)," +
						   "Farmaco VARCHAR(20) REFERENCES Farmaco(Nome)," +
						   "Data DATE NOT NULL," +
						   "Ora TIME NOT NULL," +
						   "Dose INTEGER NOT NULL," +
						   "Note VARCHAR(500),"+
						   "PRIMARY KEY(Paziente, Infermiere, Farmaco, Data, Ora));" +
						   
					   "CREATE DOMAIN TipoP AS CHAR(3) CHECK(VALUE IN('SBP', 'DBP', 'TMP', 'SAT', 'FRC')); " +
						
					   "CREATE TABLE Anomalia " +
					   "(Paziente VARCHAR(16) REFERENCES Paziente(CodSanitario)," +
					   "Tipo TipoP NOT NULL," +
					   "Numero INTEGER NOT NULL," +
					   "Settimana INTEGER NOT NULL," +
					   "Anno INTEGER NOT NULL," +
					   "Note VARCHAR NOT NULL," +
					   "PRIMARY KEY(Paziente, Tipo, Settimana, Anno));" +
		
						"CREATE TABLE RicoveriPP " +
						   "(Paziente VARCHAR(16) REFERENCES Paziente(CodSanitario)," +
						   "NomeRep VARCHAR(20)," +
						   "Ospedale VARCHAR(60)," +
						   "Data DATE NOT NULL," +
						   "FOREIGN KEY (NomeRep, Ospedale) REFERENCES Reparto(Nome, Ospedale)," +
						   "PRIMARY KEY(Paziente, NomeRep, Ospedale));";
						   
		stmt.executeUpdate(query);
		stmt.close();
		conn.close();
		System.out.println("Tabelle create con successo!");
	}
	
	public static void Popolazione() throws Exception {
		Connection conn = Connessione();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		
		String query = "INSERT INTO Paziente VALUES"+
					"('RRGFRC80P14H501D', 'Arrigoni', 'Federico', '1980-09-14', 'Roma'),"+
					"('FRNMRA92D09L781B', 'Franceschini', 'Mario', '1992-04-09', 'Verona'),"+
					"('LMBGTN31H22F205A', 'Lombardo', 'Gaetano', '1931-06-22', 'Milano'),"+
					"('GMBTTR72M29G273G', 'Gambino', 'Ettore', '1972-08-29', 'Palermo'),"+
					"('CRPMTT89L03A572E', 'Carpenteri', 'Mattia', '1989-07-03', 'Bagnolo del salento'),"+
					"('CHSLCU66B48A271M', 'Chiesa', 'Lucia', '1966-02-08', 'Ancona'),"+
					"('ZMBLRI95T58I046Y', 'Zambelli', 'Ilaria', '1995-12-18', 'San Miniato'),"+
					"('RRCMRA73S55E970D', 'Errichiello', 'Maura', '1973-11-15', 'Marostica'),"+
					"('RCPGVR68M44B594C', 'Racioppi', 'Ginevra', '1968-08-04', 'Canelli'),"+
					"('CCHSNT47R57A069Z', 'Uccheddu', 'Assunta', '1947-10-17', 'Aggius');"+
							
				"INSERT INTO Reparto VALUES"+
					"('Cardiologia', 'Policlinico Agostino Gemelli', 'RM'),"+
					"('Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', 'VR'),"+
					"('Geriatria', 'Ospedale Maggiore', 'PR'),"+
					"('Medicina generale', 'Ospedale Conti', 'SS'),"+
					"('Pediatria', 'Ospedale Niguarda Ca Granda', 'MI');"+
											
				"INSERT INTO SBP VALUES" +
					"(DEFAULT, 110, 'RRGFRC80P14H501D'),"+
					"(DEFAULT, 120, 'FRNMRA92D09L781B'),"+
					"(DEFAULT, 130, 'LMBGTN31H22F205A'),"+
					"(DEFAULT, 100, 'GMBTTR72M29G273G'),"+
					"(DEFAULT, 110, 'CRPMTT89L03A572E'),"+
					"(DEFAULT, 80, 'CHSLCU66B48A271M'),"+
					"(DEFAULT, 120, 'ZMBLRI95T58I046Y'),"+
					"(DEFAULT, 120, 'RRCMRA73S55E970D'),"+
					"(DEFAULT, 122, 'RCPGVR68M44B594C'),"+
					"(DEFAULT, 122, 'CCHSNT47R57A069Z');"+
				
				"INSERT INTO DBP VALUES" +
					"(DEFAULT, 80, 'RRGFRC80P14H501D'),"+
					"(DEFAULT, 70, 'FRNMRA92D09L781B'),"+
					"(DEFAULT, 90, 'LMBGTN31H22F205A'),"+
					"(DEFAULT, 60, 'GMBTTR72M29G273G'),"+
					"(DEFAULT, 60, 'CRPMTT89L03A572E'),"+
					"(DEFAULT, 40, 'CHSLCU66B48A271M'),"+	
					"(DEFAULT, 70, 'ZMBLRI95T58I046Y'),"+
					"(DEFAULT, 70, 'RRCMRA73S55E970D'),"+
					"(DEFAULT, 77, 'RCPGVR68M44B594C'),"+
					"(DEFAULT, 77, 'CCHSNT47R57A069Z');"+
					
				"INSERT INTO Temperatura VALUES" +
					"(DEFAULT, 36.7, 'RRGFRC80P14H501D'),"+
					"(DEFAULT, 38, 'FRNMRA92D09L781B'),"+
					"(DEFAULT, 36, 'LMBGTN31H22F205A'),"+
					"(DEFAULT, 36, 'GMBTTR72M29G273G'),"+	
					"(DEFAULT, 36, 'CRPMTT89L03A572E'),"+
					"(DEFAULT, 36, 'CHSLCU66B48A271M'),"+
					"(DEFAULT, 36, 'ZMBLRI95T58I046Y'),"+
					"(DEFAULT, 36, 'RRCMRA73S55E970D'),"+
					"(DEFAULT, 36.8, 'RCPGVR68M44B594C'),"+
					"(DEFAULT, 36.8, 'CCHSNT47R57A069Z');"+
					
				"INSERT INTO Saturazione VALUES"+
					"(DEFAULT, 98, 'RRGFRC80P14H501D'),"+
					"(DEFAULT, 99, 'FRNMRA92D09L781B'),"+
					"(DEFAULT, 99, 'LMBGTN31H22F205A'),"+
					"(DEFAULT, 94, 'GMBTTR72M29G273G'),"+
					"(DEFAULT, 98, 'CRPMTT89L03A572E'),"+
					"(DEFAULT, 90, 'CHSLCU66B48A271M'),"+
					"(DEFAULT, 98, 'ZMBLRI95T58I046Y'),"+
					"(DEFAULT, 98, 'RRCMRA73S55E970D'),"+
					"(DEFAULT, 99, 'RCPGVR68M44B594C'),"+
					"(DEFAULT, 99, 'CCHSNT47R57A069Z');"+
				
				"INSERT INTO Frequenza VALUES" +
					"(DEFAULT, 70, 'RRGFRC80P14H501D'),"+
					"(DEFAULT, 75, 'FRNMRA92D09L781B'),"+
					"(DEFAULT, 110, 'LMBGTN31H22F205A'),"+
					"(DEFAULT, 70, 'GMBTTR72M29G273G'),"+
					"(DEFAULT, 40, 'CRPMTT89L03A572E'),"+
					"(DEFAULT, 110, 'CHSLCU66B48A271M'),"+
					"(DEFAULT, 70, 'ZMBLRI95T58I046Y'),"+
					"(DEFAULT, 70, 'RRCMRA73S55E970D'),"+
					"(DEFAULT, 74, 'RCPGVR68M44B594C'),"+
					"(DEFAULT, 74, 'CCHSNT47R57A069Z');"+
				
				"INSERT INTO Medico VALUES"+
					"('M001', 'Abati', 'Leonardo', 'Pneumologia', 'Psw001', 't'),"+
					"('M002', 'Einaudi', 'Gabriele', 'Cardiologia', 'Psw002', 'f'),"+
					"('M003', 'Bagnara', 'Cesare', 'Anestesia', 'Psw003', 'f'),"+
					"('M004', 'Ughi', 'Simona', 'Enterologia', 'Psw004', 't'),"+
					"('M005', 'Graneroli', 'Sofia', 'Traumatologia', 'Psw005', 'f'),"+
					"('M006', 'Nemesi', 'Ornella', 'Pediatria', 'Psw006', 'f');"+
						
				"INSERT INTO Ricovero VALUES"+
					"('RRGFRC80P14H501D', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-14'),"+
					"('FRNMRA92D09L781B', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-04-30'),"+
					"('LMBGTN31H22F205A', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-25'),"+
					"('GMBTTR72M29G273G', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-31'),"+
					"('CRPMTT89L03A572E', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-01'),"+
					"('CHSLCU66B48A271M', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-06'),"+
					"('ZMBLRI95T58I046Y', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-15'),"+
					"('RRCMRA73S55E970D', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-15'),"+
					"('RCPGVR68M44B594C', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-03'),"+
					"('CCHSNT47R57A069Z', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2021-05-29'),"+
					"('RRGFRC80P14H501D', 'Cardiologia', 'Policlinico Agostino Gemelli', '2016-01-28'),"+
					"('ZMBLRI95T58I046Y', 'Pediatria', 'Ospedale Niguarda Ca Granda', '1997-10-31'),"+
					"('LMBGTN31H22F205A', 'Geriatria', 'Ospedale Maggiore', '2019-06-20');" +

			       "INSERT INTO Infermiere (Id, Cognome, Nome, Password) VALUES" +
					"('I001', 'Rigobello', 'Mario', 'Psw001')," +
					"('I002', 'Bianchi', 'Roberto', 'Psw002')," +
					"('I003', 'Neri', 'AnnaChiara', 'Psw003')," +
					"('I004', 'Lugoboni', 'Martina', 'Psw004')," +
					"('I005', 'Ferrari', 'Francesco', 'Psw005')," +
					"('I006', 'Alberini', 'Giovanna', 'Psw006')," +
					"('I007', 'Orfei', 'Alberto', 'Psw007')," +
					"('I008', 'Carpentari', 'Daniele', 'Psw008');" +

			       "INSERT INTO Farmaco (Nome) VALUES" +
					"('Tachipirina')," +		//Analgesico ed antipiretico
					"('Adrenalina')," +		//Rilassa la muscolatura
					"('Fentanyl')," +		//Potente analgesico oppioide
					"('Morfina')," +		//oppioide
					"('Dopamina')," +		//controllo movimento, capacità memoria, umore, capacità di attenzione, meccanismo del sonno
					"('Soluz Fisiologica')," +	//Soluzione di cloruro di sodio in acqua purificata
					"('Midazolam')," +		//Lieve sedativo
					"('Soluz Glucosata')," +	//Soluzione di glucosio e destrosio
					"('Noradrenalina');" +		//Aumennto FC, aumento gittata cardiaca, aumento PA, ..

			       "INSERT INTO Prescrizione (Paziente, Medico, Farmaco, Data, DosiGG, Quantita, Ventilazione) VALUES" +
					"('RRGFRC80P14H501D', 'M001', 'Morfina', '2021-05-14', 2, 0.5, 't')," +
					"('RRGFRC80P14H501D', 'M001', 'Noradrenalina', '2021-05-14', 1, 0.5, 't')," +
					"('LMBGTN31H22F205A', 'M002', 'Midazolam', '2021-05-12', 3, 0.5, 'f')," +
					"('RRGFRC80P14H501D', 'M003', 'Dopamina', '2021-05-07', 1, 0.5, 'f')," +
					"('RRGFRC80P14H501D', 'M004', 'Adrenalina', '2021-05-14', 1, 0.5, 't')," + 
					"('FRNMRA92D09L781B', 'M005', 'Fentanyl', '2021-05-14', 1, 0.5, 't')," +
					"('GMBTTR72M29G273G', 'M006', 'Morfina', '2021-05-17', 2, 0.5, 't')," +
					"('GMBTTR72M29G273G', 'M003', 'Midazolam', '2021-05-16', 2, 0.5, 't')," +
					"('LMBGTN31H22F205A', 'M006', 'Noradrenalina', '2021-05-17', 1, 0.5, 'f')," +
					"('CRPMTT89L03A572E', 'M003', 'Dopamina', '2021-05-18', 1, 0.5, 'f')," +
					"('CRPMTT89L03A572E', 'M004', 'Adrenalina', '2021-05-18', 1, 0.5, 't')," + 
					"('CRPMTT89L03A572E', 'M005', 'Fentanyl', '2021-05-19', 1, 0.5, 't')," +
					"('CHSLCU66B48A271M', 'M006', 'Morfina', '2021-05-10', 2, 0.5, 't')," +
					"('CHSLCU66B48A271M', 'M003', 'Midazolam', '2021-05-12', 2, 0.5, 't')," +
					"('ZMBLRI95T58I046Y', 'M006', 'Noradrenalina', '2021-05-17', 1, 0.5,'f')," +
					"('ZMBLRI95T58I046Y', 'M003', 'Dopamina', '2021-05-18', 1, 0.5, 'f')," +
					"('RRCMRA73S55E970D', 'M002', 'Midazolam', '2021-05-12', 3, 0.5, 'f')," +
					"('RRCMRA73S55E970D', 'M001', 'Noradrenalina', '2021-05-14', 1, 0.5, 't')," +
					"('RCPGVR68M44B594C', 'M004', 'Adrenalina', '2021-05-14', 1, 0.5, 't')," + 
					"('RCPGVR68M44B594C', 'M006', 'Morfina', '2021-05-17', 2, 0.5, 't')," +
					"('CCHSNT47R57A069Z', 'M003', 'Dopamina', '2021-05-18', 1, 0.5, 'f')," +
					"('CCHSNT47R57A069Z', 'M001', 'Noradrenalina', '2021-05-17', 1, 0.5, 't');" +

			       "INSERT INTO Somministrazione (Paziente, Infermiere, Farmaco, Data, Ora, Dose, Note) VALUES" +
					"('RRGFRC80P14H501D', 'I001', 'Morfina', '2021-05-15', '08:00:00', 1, 'Il paziente era particolarmente sofferente')," +
					"('RRGFRC80P14H501D', 'I002', 'Morfina', '2021-05-15', '20:00:00', 2, NULL)," +
					"('RRGFRC80P14H501D', 'I003', 'Noradrenalina', '2021-05-15', '15:15:00', 1, NULL)," +
					"('LMBGTN31H22F205A', 'I001', 'Midazolam', '2021-05-13', '07:45:00', 1, 'Paziente in attesa di risveglio')," +
					"('LMBGTN31H22F205A', 'I002', 'Midazolam', '2021-05-13', '15:45:00', 2, 'Paziente in attesa di risveglio')," +
					"('LMBGTN31H22F205A', 'I005', 'Midazolam', '2021-05-13', '23:45:00', 3, 'Paziente in attesa di risveglio')," +
					"('RRGFRC80P14H501D', 'I003', 'Dopamina', '2021-05-07', '12:27:00', 1, NULL)," +
					"('RRGFRC80P14H501D', 'I001', 'Adrenalina', '2021-05-14', '10:10:00', 1, 'Somministrato in seguito ad attacco cardiaco'),"+
					"('FRNMRA92D09L781B', 'I002', 'Morfina', '2021-05-15', '20:00:00', 1, NULL)," +	
					"('GMBTTR72M29G273G', 'I004', 'Morfina', '2021-05-17', '20:00:00', 2, 'Il paziente era particolarmente sofferente')," +
					"('GMBTTR72M29G273G', 'I004', 'Morfina', '2021-05-18', '08:00:00', 3, NULL)," +
					"('GMBTTR72M29G273G', 'I007', 'Midazolam', '2021-05-16', '14:15:00', 1, NULL)," +
					"('GMBTTR72M29G273G', 'I002', 'Midazolam', '2021-05-17', '02:30:00', 2, NULL)," +
					"('LMBGTN31H22F205A', 'I003', 'Noradrenalina', '2021-05-17', '09:37:00', 1, NULL)," +
					"('CRPMTT89L03A572E', 'I005', 'Dopamina', '2021-05-18', '10:08:00', 1, NULL)," +
					"('CRPMTT89L03A572E', 'I001', 'Adrenalina', '2021-05-18', '11:29:00', 1, 'Somministrato in seguito ad attacco cardiaco')," +
					"('CRPMTT89L03A572E', 'I005', 'Morfina', '2021-05-19', '09:13:00', 1, 'Il paziente era particolarmente sofferente')," +
					"('CHSLCU66B48A271M', 'I001', 'Morfina', '2021-05-10', '08:00:00', 2, 'Il paziente era particolarmente sofferente')," +
					"('CHSLCU66B48A271M', 'I002', 'Morfina', '2021-05-10', '20:00:00', 3, NULL)," +
					"('CHSLCU66B48A271M', 'I006', 'Midazolam', '2021-05-12', '19:30:00', 1, NULL)," +
					"('CHSLCU66B48A271M', 'I006', 'Midazolam', '2021-05-12', '07:30:00', 2, NULL)," +
					"('ZMBLRI95T58I046Y', 'I003', 'Noradrenalina', '2021-05-17', '09:37:00', 1, NULL)," +
					"('ZMBLRI95T58I046Y', 'I003', 'Dopamina', '2021-05-18', '10:08:00', 1, NULL)," +
					"('RRCMRA73S55E970D', 'I007', 'Midazolam', '2021-05-13', '07:45:00', 1, NULL)," +
					"('RRCMRA73S55E970D', 'I008', 'Midazolam', '2021-05-13', '15:45:00', 2, NULL)," +
					"('RRCMRA73S55E970D', 'I003', 'Midazolam', '2021-05-13', '23:45:00', 3, NULL)," +
					"('RRCMRA73S55E970D', 'I003', 'Noradrenalina', '2021-05-15', '06:15:00', 1, NULL)," +
					"('RCPGVR68M44B594C', 'I001', 'Adrenalina', '2021-05-14', '15:10:00', 1, NULL)," +
					"('RCPGVR68M44B594C', 'I006', 'Morfina', '2021-05-17', '18:50:00', 1, 'Il paziente era particolarmente sofferente')," +
					"('RCPGVR68M44B594C', 'I002', 'Morfina', '2021-05-18', '07:10:00', 2, NULL)," +
					"('CCHSNT47R57A069Z', 'I007', 'Dopamina', '2021-05-18', '10:08:00', 1, NULL)," +
					"('CCHSNT47R57A069Z', 'I008', 'Noradrenalina', '2021-05-17', '20:37:00', 1, NULL);" +
					
					"INSERT INTO RicoveriPP VALUES"+
					"('RRGFRC80P14H501D', 'Cardiologia', 'Policlinico Agostino Gemelli', '2018-05-14'),"+
					"('RRGFRC80P14H501D', 'Pediatria', 'Ospedale Niguarda Ca Granda', '1981-05-14'),"+
					"('FRNMRA92D09L781B', 'Medicina generale', 'Ospedale Conti', '2020-04-30'),"+
					"('LMBGTN31H22F205A', 'Terapia intensiva', 'Azienda ospedaliera universitaria integrata di Verona', '2008-09-20');" ;
							
		stmt.executeUpdate(query);
		
		stmt.close();
		conn.commit();
		conn.close();
		System.out.println("Tabelle popolate con successo!");
	}
	
	public static void main (String[] args) {
		try {
			Popolazione();
		} catch(Exception e) {
			System.out.println(e.getMessage());
			if(e.getMessage().compareTo("FATALE: il database \"terapiaintensiva\" non esiste") == 0) {
				System.out.println("Creazione in corso...");
				try {
					CreazioneDB();
					Creazione();
				} catch(Exception exc) {
					System.out.println(exc.getMessage());
				}
			}
		}
	}
}