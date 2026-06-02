## My Favourite Location

An Android application that lets users discover, save, and revisit their favourite places. Built with modern Android development practices, the app follows MVVM architecture and uses Jetpack components throughout.

### Features

- **Location Discovery**: Integrates with the Google Places API to search for and discover nearby points of interest.
- **Save & Organise**: Users can bookmark locations with custom notes and tags, stored locally in a Room database.
- **Offline-First**: All saved locations are available offline through Room's SQLite abstraction, with Retrofit handling remote API calls when connectivity is available.
- **Map View**: Displays saved locations on an interactive map, allowing users to visualise their collection geographically.
- **Clean Architecture**: The MVVM pattern separates concerns cleanly — ViewModel manages UI state, Repository abstracts data sources, Room handles local persistence, and Retrofit manages remote API calls.

### Technical Highlights

- **Room DB**: Type-safe SQLite access with DAO interfaces and LiveData/Flow integration for reactive UI updates
- **Retrofit**: Type-safe HTTP client for Google Places API integration with Gson serialization
- **Glide**: Efficient image loading for place photos from the Google Places Photo API
- **Android Jetpack**: Navigation component, ViewModel, LiveData, and ViewBinding used throughout
