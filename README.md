# Software gestione terapia intensiva Covid-19
Progetto per l'esame *Sviluppo di Sistemi Software orientato ai dati* all'Università degli Studi di Verona (anno 2021).

## Descrizione
Il progetto realizza un sistema software per la gestione di una divisione di terapia intensiva Covid-19. Il prototipo ha un back-end scritto in *Java* che si interfaccia con un DBMS relazionale implementato usando *PostgreSQL*. L'interfaccia grafica è stata realizzata tramite *JavaFX*. 

Il progetto è provvisto di relativa documentazione che contiene:
- delle note di sviluppo riguardanti l'ingegneria dei requisiti e la progettazione architetturale svolta.
- i casi d'uso del sistema.
- gli schemi ER e relazionale della base di dati collegata.
- i diagrammi di sequenza dei principali casi d'uso e del software.
- i diagrammi di attività.
- i diagrammi delle classi.
- la descrizione dei principali design patterns utilizzati.

Il sofware è composto da una schermata iniziale, in continuo aggiornamento, dove sono visualizzati i parametri vitali dei pazienti attualmente ricoverati. In questa schermata appaiono anche eventuali allarmi, generati da parametri anomali dei pazienti.

Le figure professionali che possono accedere al software sono medico ed infermiere. Essi, tramite username e password, si autenticano nel sistema che gli permette di: gestire i pazienti (ricovero e dimissione), prescrivere farmaci, scrivere documenti inerenti al ricovero dei pazienti in carico e vedere lo storico.
