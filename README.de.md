<!-- portfolio:date=2020-07-14 -->

[Deutsch](README.de.md) · [English](README.md)

# Mein Lieblingsort

Eine native Android-App zum Suchen, Speichern und Kartieren von Lieblingsorten über die Google Places API, entwickelt mit Kotlin und MVVM.

<p align="center">
  <img src="docs/kotlin.svg" alt="Kotlin" width="170" />
</p>

### Funktionen

- **Ortssuche** über die Google Places API mit Autovervollständigung
- **Kartenansicht** zur visuellen Darstellung gespeicherter Orte
- **Offline-Speicherung** via Room DB (SQLite-Abstraktion)
- **MVVM-Architektur** für klare Trennung von UI und Datenschicht
- **Retrofit** für typsichere HTTP-Anfragen an die Places API
- **Android Jetpack** Komponenten (ViewModel, LiveData, Navigation)

### Technischer Aufbau

Die App folgt dem MVVM-Muster mit einem Repository-Layer, der zwischen der lokalen Room-Datenbank und der Remote-API vermittelt. ViewModels halten den UI-Zustand lifecycle-bewusst und kommunizieren über LiveData-Streams mit den Fragments.
