# ERM/ERD - Entity Relationship Diagram

## Datenbankmodell für Reservationen

```
┌─────────────────────────────────────────────────────────────────┐
│                      ERD - RESERVATIONEN                         │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────┐
│      ROOMS          │
├─────────────────────┤
│ room_number (PK)    │◄──────┐
│ INTEGER             │       │
│ NOT NULL            │       │
│ UNIQUE              │       │
│ CHECK (101-105)     │       │
└─────────────────────┘       │
                              │
                              │ 1:N
                              │
┌─────────────────────┐       │
│   RESERVATIONS      │       │
├─────────────────────┤       │
│ id (PK)             │       │
│ UUID                │       │
│ NOT NULL            │       │
│                     │       │
│ reservation_date    │       │
│ DATE                │       │
│ NOT NULL            │       │
│                     │       │
│ time_from           │       │
│ TIME                │       │
│ NOT NULL            │       │
│                     │       │
│ time_to             │       │
│ TIME                │       │
│ NOT NULL            │       │
│                     │       │
│ room_number (FK)    │───────┘
│ INTEGER             │
│ NOT NULL            │
│                     │
│ comment             │
│ VARCHAR(200)        │
│                     │
│ private_code        │
│ VARCHAR(8)          │
│ NOT NULL            │
│ UNIQUE              │
│                     │
│ public_code         │
│ VARCHAR(8)          │
│ NOT NULL            │
│ UNIQUE              │
└─────────────────────┘
         │
         │ 1:N
         │
┌─────────────────────┐
│   PARTICIPANTS      │
├─────────────────────┤
│ id (PK)             │
│ UUID                │
│ NOT NULL            │
│                     │
│ first_name          │
│ VARCHAR(255)        │
│ NOT NULL            │
│                     │
│ last_name           │
│ VARCHAR(255)        │
│ NOT NULL            │
│                     │
│ reservation_id (FK) │
│ UUID                │
│ NOT NULL            │
└─────────────────────┘
```

## Tabellenbeschreibung

### Tabelle: rooms

| Spalte        | Typ      | Constraints                    | Beschreibung                    |
|---------------|----------|--------------------------------|---------------------------------|
| room_number   | INTEGER  | PRIMARY KEY, NOT NULL, UNIQUE  | Zimmernummer (101-105)          |

**Beziehungen:**
- 1:N zu RESERVATIONS (ein Raum kann mehrere Reservationen haben)

### Tabelle: reservations

| Spalte           | Typ         | Constraints                    | Beschreibung                    |
|------------------|-------------|--------------------------------|---------------------------------|
| id               | UUID        | PRIMARY KEY, NOT NULL          | Eindeutige Reservations-ID     |
| reservation_date | DATE        | NOT NULL                       | Datum der Reservation           |
| time_from        | TIME        | NOT NULL                       | Startzeit (HH:MM)               |
| time_to          | TIME        | NOT NULL                       | Endzeit (HH:MM)                 |
| room_number      | INTEGER     | FOREIGN KEY, NOT NULL          | Referenz zu rooms.room_number   |
| comment          | VARCHAR(200)|                                | Bemerkung (10-200 Zeichen)      |
| private_code     | VARCHAR(8)  | NOT NULL, UNIQUE               | Code zum Bearbeiten/Löschen    |
| public_code      | VARCHAR(8)  | NOT NULL, UNIQUE               | Code zum Anzeigen               |

**Beziehungen:**
- N:1 zu ROOMS (jede Reservation gehört zu einem Raum)
- 1:N zu PARTICIPANTS (jede Reservation hat mehrere Teilnehmer)

**Indizes:**
- Index auf `room_number` für schnelle Verfügbarkeitsprüfungen
- Index auf `reservation_date` für Sortierung
- Unique Index auf `private_code`
- Unique Index auf `public_code`

### Tabelle: participants

| Spalte         | Typ         | Constraints                    | Beschreibung                    |
|----------------|-------------|--------------------------------|---------------------------------|
| id             | UUID        | PRIMARY KEY, NOT NULL          | Eindeutige Teilnehmer-ID        |
| first_name     | VARCHAR(255)| NOT NULL                       | Vorname (nur Buchstaben)        |
| last_name      | VARCHAR(255)| NOT NULL                       | Nachname (nur Buchstaben)       |
| reservation_id | UUID        | FOREIGN KEY, NOT NULL          | Referenz zu reservations.id     |

**Beziehungen:**
- N:1 zu RESERVATIONS (jeder Teilnehmer gehört zu einer Reservation)

**Indizes:**
- Index auf `reservation_id` für schnelle Abfragen

## Kardinalitäten

- **ROOMS : RESERVATIONS** = 1 : N
  - Ein Raum kann mehrere Reservationen haben
  - Eine Reservation gehört zu genau einem Raum

- **RESERVATIONS : PARTICIPANTS** = 1 : N
  - Eine Reservation kann mehrere Teilnehmer haben
  - Ein Teilnehmer gehört zu genau einer Reservation

## Constraints und Validierungen

### Reservations
- `reservation_date` muss in der Zukunft liegen (Application-Level)
- `time_from` muss vor `time_to` liegen (Application-Level)
- `comment` muss zwischen 10 und 200 Zeichen lang sein (Application-Level)
- `private_code` und `public_code` müssen eindeutig sein (Database-Level)

### Participants
- `first_name` und `last_name` dürfen nur Buchstaben enthalten (Application-Level)
- Mindestens ein Teilnehmer pro Reservation (Application-Level)

### Rooms
- `room_number` muss zwischen 101 und 105 liegen (Application-Level)

## SQL CREATE Statements

```sql
-- Tabelle: rooms
CREATE TABLE rooms (
    room_number INTEGER PRIMARY KEY,
    CONSTRAINT check_room_number CHECK (room_number BETWEEN 101 AND 105)
);

-- Tabelle: reservations
CREATE TABLE reservations (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    reservation_date DATE NOT NULL,
    time_from TIME NOT NULL,
    time_to TIME NOT NULL,
    room_number INTEGER NOT NULL,
    comment VARCHAR(200),
    private_code VARCHAR(8) NOT NULL UNIQUE,
    public_code VARCHAR(8) NOT NULL UNIQUE,
    CONSTRAINT fk_room FOREIGN KEY (room_number) REFERENCES rooms(room_number),
    CONSTRAINT check_time_order CHECK (time_from < time_to)
);

-- Tabelle: participants
CREATE TABLE participants (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    reservation_id UUID NOT NULL,
    CONSTRAINT fk_reservation FOREIGN KEY (reservation_id) 
        REFERENCES reservations(id) ON DELETE CASCADE
);

-- Indizes
CREATE INDEX idx_reservations_room_date ON reservations(room_number, reservation_date);
CREATE INDEX idx_reservations_date ON reservations(reservation_date);
CREATE INDEX idx_participants_reservation ON participants(reservation_id);
```

## Initialdaten

```sql
-- Räume initialisieren
INSERT INTO rooms (room_number) VALUES (101), (102), (103), (104), (105);
```

