# Projektdokumentation - Terminkalender (Raumreservierungssystem)

## 1. Projektübersicht

Dieses Projekt implementiert ein Web-basiertes Raumreservierungssystem für ein Unternehmen. Die Anwendung ermöglicht es Benutzern, Räume (101-105) für Sitzungen und Veranstaltungen zu reservieren, ohne ein Benutzerkonto zu benötigen.

## 2. Technologie-Stack

### Backend
- **Java 21**
- **Spring Boot 3.3.5**
- **Spring Data JPA** (für Datenbankzugriffe)
- **PostgreSQL** (Produktions-Datenbank)
- **Maven** (Build-Management)

### Frontend
- **React 18** mit TypeScript
- **NextUI** (UI-Komponenten)
- **Vite** (Build-Tool)

### Datenbank
- **PostgreSQL** (für Produktion)
- **H2 Database** (für Tests)

## 3. Funktionale Anforderungen

### 3.1 Reservierung erstellen
- Formular mit folgenden Feldern:
  - **Datum**: TT.MM.JJJJ (muss in der Zukunft liegen)
  - **Von**: HH:MM
  - **Bis**: HH:MM
  - **Zimmer**: 101, 102, 103, 104, 105
  - **Bemerkung**: 10-200 alphanumerische Zeichen
  - **Teilnehmer**: Eine oder mehrere Vor- und Nachnamen (nur Buchstaben A-Z, a-z, Komma als Trennzeichen)

### 3.2 Validierungen
- Kein Feld darf leer bleiben
- Datum muss in der Zukunft liegen
- Zeit "Von" muss vor Zeit "Bis" liegen
- Zimmer muss verfügbar sein (keine Überschneidung mit anderen Reservationen)
- Bemerkung: 10-200 Zeichen
- Teilnehmer: Nur Buchstaben erlaubt

### 3.3 Code-System
Nach der Erstellung einer Reservation erhält der Benutzer zwei Codes:
- **Private Code**: Zum Bearbeiten und Löschen der Reservation
- **Public Code**: Zum Anzeigen der Reservation (für Teilnehmer)

### 3.4 Navigation
- Startseite mit Link "Reservation erfassen" und Code-Eingabefeld
- Code-Eingabe führt zu:
  - Edit-Seite (bei Private Code)
  - View-Seite (bei Public Code)

## 4. Datenmodell

### 4.1 Entitäten

#### Reservation
- `id` (UUID, Primary Key)
- `reservationDate` (LocalDate)
- `timeFrom` (LocalTime)
- `timeTo` (LocalTime)
- `room` (ManyToOne zu Room)
- `comment` (String, 10-200 Zeichen)
- `privateCode` (String, unique)
- `publicCode` (String, unique)
- `participants` (OneToMany zu Participant)

#### Room
- `roomNumber` (Integer, Primary Key, 101-105)

#### Participant
- `id` (UUID, Primary Key)
- `firstName` (String)
- `lastName` (String)
- `reservation` (ManyToOne zu Reservation)

## 5. API-Endpunkte

### Reservierungen
- `POST /api/reservations` - Neue Reservation erstellen
- `GET /api/reservations` - Alle Reservationen abrufen
- `GET /api/reservations/private/{privateCode}` - Reservation mit Private Code abrufen
- `GET /api/reservations/public/{publicCode}` - Reservation mit Public Code abrufen
- `PUT /api/reservations/private/{privateCode}` - Reservation aktualisieren
- `DELETE /api/reservations/private/{privateCode}` - Reservation löschen

## 6. Testdaten

Beim Start der Anwendung werden automatisch 2-3 Test-Reservationen erstellt:
1. **Reservation 1**: Zimmer 101, 1 Tag in der Zukunft, 10:00-11:30, 2 Teilnehmer
2. **Reservation 2**: Zimmer 102, 2 Tage in der Zukunft, 14:00-15:00, 1 Teilnehmer
3. **Reservation 3**: Zimmer 103, 3 Tage in der Zukunft, 09:00-10:30, 2 Teilnehmer

## 7. Sicherheit

- Keine Authentifizierung erforderlich für Reservierungen
- Private/Public Code-System für Zugriffskontrolle
- Validierung aller Eingaben auf Backend- und Frontend-Ebene
- CORS konfiguriert für Frontend-Zugriff

## 8. Installation und Ausführung

### Backend starten
```bash
./mvnw spring-boot:run
```

### Frontend starten
```bash
npm install
npm run dev
```

### Datenbank
Die Anwendung verwendet PostgreSQL. Die Datenbank wird automatisch initialisiert (Hibernate DDL: update).

## 9. Projektstruktur

```
src/
├── main/
│   ├── java/com/devtiro/tasks/
│   │   ├── controllers/
│   │   │   └── ReservationController.java
│   │   ├── domain/
│   │   │   ├── dto/
│   │   │   │   ├── ReservationDto.java
│   │   │   │   └── ParticipantDto.java
│   │   │   └── entities/
│   │   │       ├── Reservation.java
│   │   │       ├── Room.java
│   │   │       └── Participant.java
│   │   ├── mappers/
│   │   │   ├── ReservationMapper.java
│   │   │   └── impl/
│   │   │       └── ReservationMapperImpl.java
│   │   ├── repositories/
│   │   │   ├── ReservationRepository.java
│   │   │   ├── RoomRepository.java
│   │   │   └── ParticipantRepository.java
│   │   └── services/
│   │       ├── ReservationService.java
│   │       └── impl/
│   │           └── ReservationServiceImpl.java
│   └── resources/
│       └── application.properties
└── components/
    ├── HomeScreen.tsx
    ├── CreateReservationScreen.tsx
    ├── ConfirmationScreen.tsx
    ├── EditReservationScreen.tsx
    ├── ViewReservationScreen.tsx
    └── ReservationsListScreen.tsx
```

---

**Version**: 1.0  
**Datum**: 2025-12-08  
**Autor**: Projektteam

