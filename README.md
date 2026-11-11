# Projektbeschreibung

Dieses Projekt entstand im Rahmen des Moduls **Datenbanken: Weiterführende Konzepte** als Prüfungsleistung.

Ziel des Projekts war die Entwicklung einer relationalen Datenbank für einen beispielhaften Online-Handel sowie die Implementierung einer REST-API zur Verwaltung und Auswertung der Daten.

Als Ausgangspunkt wurde eine grundlegende Projektstruktur bereitgestellt. Diese umfasste unter anderem `Application.java`, `StartPageController.java`, `pom.xml`, Docker-Compose-Konfigurationen sowie Beispieldaten in Form einer `data.sql`. Darauf aufbauend wurden die Datenbank- und Anwendungsfunktionalitäten im Rahmen der Aufgabenstellung umgesetzt.

## Tech-Stack
- Java 17
- Spring Boot
- PostgreSQL
- SQL
- JDBC
- Maven
- Docker und Docker Compose
- Swagger
- OpenAPI

## Projektinhalte

### Datenbankentwicklung

- Umsetzung eines vorgegebenen ER-Modells in ein relationales Datenbankschema
- Definition von Tabellen sowie Primär- und Fremdschlüsseln
- Verwendung von Constraints zur Sicherstellung der Datenintegrität
- Implementierung von Triggern zur Verwaltung der Lagerbestände und Bestellprozesse

### REST-API

- Implementierung einer REST-API mit Spring Boot und JDBC
- CRUD-Operationen für Kunden, Mitarbeiter, Adressen, Produkte, Bestellungen und Bestellpositionen
- Login-Endpunkte für Kunden und Mitarbeiter
- Anbindung der REST-API an die PostgreSQL-Datenbank

### Datenbankabfragen und Views

- Erstellung von SQL-Views für verschiedene Auswertungen
- Aggregation von Bestell- und Verkaufsdaten
- Auswertung von Mitarbeiteraktivitäten und Bestellstatus

## Ausführung

Voraussetzungen:
- Java 17
- Maven
- Docker und Docker Compose

Für den Betrieb der PostgreSQL-Datenbank stehen zwei Docker-Compose-Konfigurationen zur Verfügung:

**Nicht persistenter Modus**

```bash
docker compose up -d
```

**Persistenter Modus**

```bash
docker compose -f docker-compose-persistent.yml up -d
```

Anschließend die Spring-Boot-Anwendung starten:

```bash
cd spring-boot
mvn clean install
mvn spring-boot:run
```

Die REST-API ist anschließend unter http://localhost:8080 erreichbar.