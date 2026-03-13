# UML-Klassendiagramm

## Klassendiagramm für die Webapplikation

```
┌─────────────────────────────────────────────────────────────────┐
│              UML-KLASSENDIAGRAMM - TERMINKALENDER               │
└─────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                        CONTROLLER PACKAGE                        │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│      ReservationController            │
├──────────────────────────────────────┤
│ - reservationService: ReservationService │
├──────────────────────────────────────┤
│ + createReservation(ReservationDto): │
│   ResponseEntity<ReservationDto>     │
│ + updateReservation(String,           │
│   ReservationDto):                   │
│   ResponseEntity<ReservationDto>     │
│ + getReservationByPrivateCode(       │
│   String):                           │
│   ResponseEntity<ReservationDto>     │
│ + getReservationByPublicCode(        │
│   String):                           │
│   ResponseEntity<ReservationDto>     │
│ + deleteReservation(String):         │
│   ResponseEntity<Void>               │
│ + getAllReservations():              │
│   ResponseEntity<List<ReservationDto>>│
└──────────────────────────────────────┘
              │
              │ verwendet
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         SERVICE PACKAGE                          │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│      ReservationService              │
│         (Interface)                   │
├──────────────────────────────────────┤
│ + createReservation(ReservationDto): │
│   ReservationDto                      │
│ + updateReservation(String,           │
│   ReservationDto): ReservationDto    │
│ + getReservationByPrivateCode(       │
│   String): ReservationDto            │
│ + getReservationByPublicCode(        │
│   String): ReservationDto            │
│ + deleteReservation(String): void   │
│ + getAllReservations():              │
│   List<ReservationDto>               │
│ + isRoomAvailable(Integer,           │
│   LocalDate, LocalTime, LocalTime,  │
│   UUID): boolean                     │
└──────────────────────────────────────┘
              ▲
              │ implementiert
              │
┌──────────────────────────────────────┐
│    ReservationServiceImpl             │
├──────────────────────────────────────┤
│ - reservationRepository:              │
│   ReservationRepository               │
│ - roomRepository: RoomRepository      │
│ - participantRepository:              │
│   ParticipantRepository               │
│ - reservationMapper:                  │
│   ReservationMapper                   │
│ - secureRandom: SecureRandom          │
├──────────────────────────────────────┤
│ + createReservation(ReservationDto): │
│   ReservationDto                      │
│ + updateReservation(String,           │
│   ReservationDto): ReservationDto    │
│ + getReservationByPrivateCode(       │
│   String): ReservationDto            │
│ + getReservationByPublicCode(        │
│   String): ReservationDto            │
│ + deleteReservation(String): void   │
│ + getAllReservations():              │
│   List<ReservationDto>               │
│ + isRoomAvailable(Integer,           │
│   LocalDate, LocalTime, LocalTime,  │
│   UUID): boolean                     │
│ - initializeRooms(): void            │
│ - initializeTestData(): void          │
│ - generateCode(): String             │
└──────────────────────────────────────┘
              │
              │ verwendet
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                       REPOSITORY PACKAGE                         │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│   ReservationRepository              │
│   (Interface)                         │
├──────────────────────────────────────┤
│ + findByPrivateCode(String):         │
│   Optional<Reservation>              │
│ + findByPublicCode(String):          │
│   Optional<Reservation>              │
│ + findAllByOrderByReservationDate    │
│   AscTimeFromAsc():                  │
│   List<Reservation>                  │
│ + findOverlappingReservations(       │
│   Integer, LocalDate, LocalTime,    │
│   LocalTime): List<Reservation>     │
│ + findOverlappingReservations       │
│   Excluding(Integer, LocalDate,     │
│   LocalTime, LocalTime, UUID):      │
│   List<Reservation>                  │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│      RoomRepository                  │
│      (Interface)                     │
├──────────────────────────────────────┤
│ + findByRoomNumber(Integer):         │
│   Optional<Room>                     │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│    ParticipantRepository             │
│    (Interface)                        │
├──────────────────────────────────────┤
│ (Standard JPA-Methoden)              │
└──────────────────────────────────────┘

              │
              │ verwendet
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         ENTITY PACKAGE                          │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│         Reservation                   │
│         @Entity                        │
├──────────────────────────────────────┤
│ - id: UUID                           │
│ - reservationDate: LocalDate          │
│ - timeFrom: LocalTime                 │
│ - timeTo: LocalTime                   │
│ - room: Room                          │
│ - comment: String                     │
│ - privateCode: String                  │
│ - publicCode: String                  │
│ - participants: List<Participant>      │
├──────────────────────────────────────┤
│ + getId(): UUID                       │
│ + setId(UUID): void                   │
│ + getReservationDate(): LocalDate     │
│ + setReservationDate(LocalDate): void │
│ + getTimeFrom(): LocalTime            │
│ + setTimeFrom(LocalTime): void        │
│ + getTimeTo(): LocalTime              │
│ + setTimeTo(LocalTime): void          │
│ + getRoom(): Room                     │
│ + setRoom(Room): void                 │
│ + getComment(): String                 │
│ + setComment(String): void            │
│ + getPrivateCode(): String            │
│ + setPrivateCode(String): void        │
│ + getPublicCode(): String             │
│ + setPublicCode(String): void         │
│ + getParticipants(): List<Participant>│
│ + setParticipants(List<Participant>): │
│   void                                │
│ + addParticipant(Participant): void   │
│ + removeParticipant(Participant):    │
│   void                                │
└──────────────────────────────────────┘
              │
              │ 1:N
              │
┌──────────────────────────────────────┐
│         Participant                   │
│         @Entity                        │
├──────────────────────────────────────┤
│ - id: UUID                           │
│ - firstName: String                   │
│ - lastName: String                    │
│ - reservation: Reservation            │
├──────────────────────────────────────┤
│ + getId(): UUID                       │
│ + setId(UUID): void                   │
│ + getFirstName(): String              │
│ + setFirstName(String): void          │
│ + getLastName(): String               │
│ + setLastName(String): void           │
│ + getReservation(): Reservation       │
│ + setReservation(Reservation): void   │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│            Room                       │
│            @Entity                     │
├──────────────────────────────────────┤
│ - roomNumber: Integer                 │
├──────────────────────────────────────┤
│ + getRoomNumber(): Integer            │
│ + setRoomNumber(Integer): void        │
└──────────────────────────────────────┘

              │
              │ verwendet
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                          DTO PACKAGE                             │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│         ReservationDto                 │
├──────────────────────────────────────┤
│ - id: UUID                           │
│ - reservationDate: LocalDate          │
│ - timeFrom: LocalTime                 │
│ - timeTo: LocalTime                   │
│ - roomNumber: Integer                 │
│ - comment: String                      │
│ - participants: List<ParticipantDto>   │
│ - privateCode: String                 │
│ - publicCode: String                  │
├──────────────────────────────────────┤
│ + getId(): UUID                       │
│ + setId(UUID): void                   │
│ + getReservationDate(): LocalDate     │
│ + setReservationDate(LocalDate): void │
│ + getTimeFrom(): LocalTime            │
│ + setTimeFrom(LocalTime): void        │
│ + getTimeTo(): LocalTime              │
│ + setTimeTo(LocalTime): void          │
│ + getRoomNumber(): Integer            │
│ + setRoomNumber(Integer): void        │
│ + getComment(): String                 │
│ + setComment(String): void            │
│ + getParticipants():                  │
│   List<ParticipantDto>                │
│ + setParticipants(                    │
│   List<ParticipantDto>): void         │
│ + getPrivateCode(): String            │
│ + setPrivateCode(String): void        │
│ + getPublicCode(): String             │
│ + setPublicCode(String): void         │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│         ParticipantDto                │
├──────────────────────────────────────┤
│ - id: UUID                           │
│ - firstName: String                   │
│ - lastName: String                    │
├──────────────────────────────────────┤
│ + getId(): UUID                       │
│ + setId(UUID): void                   │
│ + getFirstName(): String              │
│ + setFirstName(String): void          │
│ + getLastName(): String               │
│ + setLastName(String): void           │
└──────────────────────────────────────┘

              │
              │ verwendet
              ▼
┌─────────────────────────────────────────────────────────────────┐
│                         MAPPER PACKAGE                           │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│      ReservationMapper                │
│      (Interface)                       │
├──────────────────────────────────────┤
│ + fromDto(ReservationDto):            │
│   Reservation                         │
│ + toDto(Reservation): ReservationDto  │
└──────────────────────────────────────┘
              ▲
              │ implementiert
              │
┌──────────────────────────────────────┐
│    ReservationMapperImpl              │
├──────────────────────────────────────┤
│ + fromDto(ReservationDto):            │
│   Reservation                         │
│ + toDto(Reservation): ReservationDto │
└──────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────┐
│                      FRONTEND COMPONENTS                         │
└─────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────┐
│         HomeScreen                   │
│         (React Component)             │
├──────────────────────────────────────┤
│ - code: string                        │
│ - error: string                       │
├──────────────────────────────────────┤
│ + handleCodeSubmit(): void           │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│    CreateReservationScreen            │
│    (React Component)                 │
├──────────────────────────────────────┤
│ - formData: Reservation               │
│ - loading: boolean                    │
│ - error: string                       │
├──────────────────────────────────────┤
│ + addParticipant(): void             │
│ + removeParticipant(number): void    │
│ + updateParticipant(number, string,  │
│   string): void                       │
│ + handleSubmit(): void               │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│      ConfirmationScreen               │
│      (React Component)               │
├──────────────────────────────────────┤
│ - privateCode: string                 │
│ - publicCode: string                  │
├──────────────────────────────────────┤
│ + copyToClipboard(string, string):   │
│   void                                │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│      EditReservationScreen            │
│      (React Component)               │
├──────────────────────────────────────┤
│ - formData: Reservation               │
│ - loading: boolean                    │
│ - saving: boolean                     │
│ - error: string                       │
├──────────────────────────────────────┤
│ + fetchReservation(): void           │
│ + addParticipant(): void             │
│ + removeParticipant(number): void    │
│ + updateParticipant(number, string, │
│   string): void                       │
│ + handleSubmit(): void               │
│ + handleDelete(): void               │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│      ViewReservationScreen            │
│      (React Component)               │
├──────────────────────────────────────┤
│ - reservation: Reservation            │
│ - loading: boolean                    │
│ - error: string                       │
├──────────────────────────────────────┤
│ + fetchReservation(): void           │
│ + formatDate(string): string         │
│ + formatTime(string): string         │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘

┌──────────────────────────────────────┐
│    ReservationsListScreen             │
│    (React Component)                 │
├──────────────────────────────────────┤
│ - reservations: Reservation[]         │
│ - loading: boolean                    │
├──────────────────────────────────────┤
│ + fetchReservations(): void          │
│ + formatDate(string): string         │
│ + formatTime(string): string         │
│ + render(): JSX.Element               │
└──────────────────────────────────────┘
```

## Package-Struktur

### Backend (Java)
- **com.devtiro.tasks.controllers**: REST-Controller für API-Endpunkte
- **com.devtiro.tasks.services**: Business-Logik und Validierung
- **com.devtiro.tasks.repositories**: Datenbankzugriff (JPA Repositories)
- **com.devtiro.tasks.domain.entities**: JPA-Entitäten
- **com.devtiro.tasks.domain.dto**: Data Transfer Objects
- **com.devtiro.tasks.mappers**: Konvertierung zwischen Entities und DTOs

### Frontend (TypeScript/React)
- **components**: React-Komponenten für UI
- **domain**: TypeScript-Interfaces für Domain-Modelle

## Beziehungen

- **Controller → Service**: Controller verwenden Services für Business-Logik
- **Service → Repository**: Services verwenden Repositories für Datenbankzugriff
- **Service → Mapper**: Services verwenden Mapper für Entity-DTO-Konvertierung
- **Repository → Entity**: Repositories arbeiten mit JPA-Entitäten
- **Entity → Entity**: Entitäten haben Beziehungen untereinander (1:N, N:1)
- **Frontend → API**: React-Komponenten kommunizieren mit REST-API

## Attribute und Methoden

### ReservationController
- **Attribute**: reservationService (Dependency Injection)
- **Methoden**: CRUD-Operationen für Reservierungen über REST-API

### ReservationService / ReservationServiceImpl
- **Attribute**: Repositories, Mapper, SecureRandom
- **Methoden**: Business-Logik, Validierung, Code-Generierung, Testdaten-Initialisierung

### Entities
- **Reservation**: Hauptentität mit allen Reservierungsdaten
- **Room**: Raum-Entität (101-105)
- **Participant**: Teilnehmer-Entität

### DTOs
- **ReservationDto**: Transfer-Objekt für API-Kommunikation
- **ParticipantDto**: Transfer-Objekt für Teilnehmer

### Frontend Components
- Alle Komponenten haben State-Management für Formulardaten und API-Kommunikation
- Verwenden React Hooks (useState, useEffect) für State und Side-Effects

