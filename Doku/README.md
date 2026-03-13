# Dokumentation - Terminkalender Projekt

Dieser Ordner enthält die vollständige Projektdokumentation für das Raumreservierungssystem.

## Dateien

1. **Projektdokumentation.md** - Hauptdokumentation mit Projektübersicht, Anforderungen, Technologie-Stack, API-Dokumentation und Projektstruktur

2. **UML_Zustandsdiagramm.md** - UML-Zustandsdiagramm für die Navigation der Webapplikation mit allen Zuständen, Signalen und Entscheidungen

3. **ERM_ERD.md** - Entity-Relationship-Diagramm (ERD) mit allen Tabellen, Beziehungen, Constraints und SQL CREATE Statements

4. **UML_Klassendiagramm.md** - UML-Klassendiagramm mit allen Controller- und Model-Klassen, Packages, Attributen und Methoden

## PDF-Export

Um die Dokumentation als PDF zu exportieren, können Sie:

1. **Markdown zu PDF konvertieren** mit Tools wie:
   - Pandoc: `pandoc Projektdokumentation.md -o Projektdokumentation.pdf`
   - Online-Tools wie markdown-pdf.com
   - VS Code Extension "Markdown PDF"

2. **Alle Dateien kombinieren** in eine einzige PDF-Datei:
   ```bash
   pandoc Projektdokumentation.md UML_Zustandsdiagramm.md ERM_ERD.md UML_Klassendiagramm.md -o Projektdokumentation_Vollstaendig.pdf
   ```

## Versionierung

- **Version 1.0** - 2025-12-08
  - Initiale Dokumentation
  - Alle Diagramme erstellt
  - Vollständige Projektdokumentation

## Hinweise

- Alle Diagramme sind in Textform (Markdown) dargestellt
- Für visuelle Diagramme können Tools wie PlantUML, Draw.io oder Lucidchart verwendet werden
- Die SQL-Statements in ERM_ERD.md können direkt in der Datenbank ausgeführt werden

