# UML-Zustandsdiagramm - Navigation der Webapplikation

## Zustandsdiagramm für die Navigation

```
┌─────────────────────────────────────────────────────────────────┐
│                    TERMINKALENDER NAVIGATION                     │
└─────────────────────────────────────────────────────────────────┘

[Startseite (HomeScreen)]
    │
    ├─[Button: "Reservation erfassen"]───→ [Reservierungsformular]
    │                                              │
    │                                              ├─[Submit]───→ [Validierung]
    │                                              │                    │
    │                                              │                    ├─[Fehler]───→ [Reservierungsformular] (mit Fehlermeldung)
    │                                              │                    │
    │                                              │                    └─[Erfolg]───→ [Bestätigungsseite]
    │                                              │                                        │
    │                                              │                                        ├─[Button: "Zur Startseite"]───→ [Startseite]
    │                                              │                                        │
    │                                              │                                        └─[Button: "Alle Reservationen"]───→ [Reservationsliste]
    │                                              │
    │                                              └─[Button: "Abbrechen"]───→ [Startseite]
    │
    ├─[Code-Eingabe + Submit]───→ [Code-Validierung]
    │                                    │
    │                                    ├─[Private Code erkannt]───→ [Edit-Seite]
    │                                    │                                │
    │                                    │                                ├─[Submit Änderungen]───→ [Validierung]
    │                                    │                                │                            │
    │                                    │                                │                            ├─[Fehler]───→ [Edit-Seite] (mit Fehlermeldung)
    │                                    │                                │                            │
    │                                    │                                │                            └─[Erfolg]───→ [Reservationsliste]
    │                                    │                                │
    │                                    │                                ├─[Button: "Löschen"]───→ [Bestätigung]───→ [Reservationsliste]
    │                                    │                                │
    │                                    │                                └─[Button: "Abbrechen"]───→ [Startseite]
    │                                    │
    │                                    ├─[Public Code erkannt]───→ [View-Seite]
    │                                    │                              │
    │                                    │                              └─[Button: "Zur Startseite"]───→ [Startseite]
    │                                    │
    │                                    └─[Ungültiger Code]───→ [Startseite] (mit Fehlermeldung)
    │
    └─[Button: "Alle Reservationen anzeigen"]───→ [Reservationsliste]
                                                          │
                                                          └─[Button: "Zur Startseite"]───→ [Startseite]
```

## Zustände (Seiten)

1. **Startseite (HomeScreen)**
   - Initialer Zustand
   - Enthält: Button "Reservation erfassen", Code-Eingabefeld, Button "Alle Reservationen anzeigen"

2. **Reservierungsformular (CreateReservationScreen)**
   - Formular zum Erfassen einer neuen Reservation
   - Felder: Datum, Von, Bis, Zimmer, Bemerkung, Teilnehmer

3. **Bestätigungsseite (ConfirmationScreen)**
   - Zeigt Private und Public Code nach erfolgreicher Erstellung
   - Übergang zu Startseite oder Reservationsliste

4. **Edit-Seite (EditReservationScreen)**
   - Bearbeitung einer Reservation mit Private Code
   - Enthält: Formular (wie Reservierungsformular), Button "Löschen"

5. **View-Seite (ViewReservationScreen)**
   - Anzeige einer Reservation mit Public Code (nur lesend)

6. **Reservationsliste (ReservationsListScreen)**
   - Liste aller Reservationen in Tabellenform

## Signale/Events

- **Button-Klick**: "Reservation erfassen", "Zur Startseite", "Alle Reservationen anzeigen", etc.
- **Formular-Submit**: Absenden des Reservierungsformulars oder Edit-Formulars
- **Code-Submit**: Eingabe und Absenden eines Codes
- **Validierung**: Prüfung der Eingaben (Datum, Zeit, Zimmer verfügbar, etc.)

## Entscheidungen

1. **Code-Validierung**: Private Code? → Edit-Seite | Public Code? → View-Seite | Ungültig? → Fehlermeldung
2. **Formular-Validierung**: Gültig? → Weiter | Ungültig? → Fehlermeldung anzeigen
3. **Zimmer-Verfügbarkeit**: Verfügbar? → Reservation erstellen | Nicht verfügbar? → Fehlermeldung

## Zustandsübergänge

- **Startseite → Reservierungsformular**: Button "Reservation erfassen"
- **Reservierungsformular → Bestätigungsseite**: Formular erfolgreich validiert und gespeichert
- **Reservierungsformular → Startseite**: Button "Abbrechen" oder Fehler
- **Startseite → Edit-Seite**: Private Code erkannt
- **Startseite → View-Seite**: Public Code erkannt
- **Edit-Seite → Reservationsliste**: Änderungen erfolgreich gespeichert oder gelöscht
- **Edit-Seite → Startseite**: Button "Abbrechen"
- **View-Seite → Startseite**: Button "Zur Startseite"
- **Startseite → Reservationsliste**: Button "Alle Reservationen anzeigen"
- **Bestätigungsseite → Startseite**: Button "Zur Startseite"
- **Bestätigungsseite → Reservationsliste**: Button "Alle Reservationen anzeigen"

