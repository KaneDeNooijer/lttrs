# LTTRS - Werkdruk Inzicht voor Medewerkers

LTTRS is een JavaFX-applicatie gericht op het inzichtelijk maken van werkdruk. De kern van de applicatie is de
werkdruk-meter: een visuele weergave van hoeveel uren een medewerker heeft gewerkt ten opzichte van zijn of haar
contracturen. Daarnaast biedt de applicatie functionaliteit voor het bijhouden van urenregistraties, verlofperiodes en
contracten.

## Aan de slag

### Vereisten

* Java Development Kit versie 25 (JDK 25)
* Maven
* MySQL Server (bijvoorbeeld via XAMPP of MAMP)

### Installatie

#### 1. Repository downloaden

```
git clone https://github.com/KaneDeNooijer/lttrs.git
```

#### 2. Database opzetten

```
Importeer het bestand 'setup.sql' via phpMyAdmin of een andere MySQL client.
Zorg ervoor dat de database 'lttrs' heet.
Deze staat in src/main/resources/me/kanedenooijer/lttrs/database
```

#### 3. Database verbinding configureren

**BELANGRIJK:** Pas de databasegegevens aan voordat je de applicatie bouwt.

```
Open: src/main/java/me/kanedenooijer/lttrs/model/DatabaseConnection.java

Pas de volgende constanten aan:
- URL (standaard: "jdbc:mysql://localhost:3306/lttrs")
- USERNAME (standaard: "root")
- PASSWORD (standaard: "")
```

#### 4. Bouwen en starten

```
mvn clean package
java -jar target/lttrs-1.0-jar-with-dependencies.jar
```

Of direct via Maven:

```
mvn javafx:run
```

## De werkdruk-meter

De werkdruk-meter is het centrale onderdeel van LTTRS. Op het dashboard wordt per week berekend hoeveel uren een
medewerker heeft gewerkt ten opzichte van de contracturen. De meter loopt van groen naar rood op een schaal van nul tot
twee keer de contracturen:

* **Groen** — weinig tot geen overbelasting
* **Oranje** — werkdruk loopt op
* **Rood** — medewerker werkt significant meer dan het contract

De berekening en kleurschaling worden uitgevoerd in Java op basis van twee databasequeries: één voor het ophalen van het
actieve contract, en één voor het optellen van de geregistreerde uren in de huidige week.

## Functionaliteiten

* Registreren en beheren van dagelijkse werkuren
* Bijhouden van verlofperiodes
* Beheren van contracten per medewerker
* Dashboard met werkdruk-meter op weekbasis
* Account registratie en login

## Technologieën

* JavaFX 21.0.6
* MySQL met JDBC
* Maven

## Roadmap

- [x] Urenregistraties CRUD
- [x] Verlof CRUD
- [x] Contracten CRUD
- [x] Werkdruk-meter op het dashboard
- [x] Account registratie en login
- [x] JAR-bestand genereren
- [ ] Adminpaneel voor gebruikersbeheer
- [ ] Wachtwoord hashing
- [ ] Exporteren naar PDF of Excel

## Contact

Kane de Nooijer - kanedenooijer@gmail.com
