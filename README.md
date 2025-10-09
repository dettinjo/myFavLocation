<!-- Improved compatibility of back to top link: See: https://github.com/othneildrew/Best-README-Template/pull/73 -->
<a id="readme-top"></a>


<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="#">
    <img src="https://i.imgur.com/U8a4a5A.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">MyFavLocation - Android Application</h3>

  <p align="center">
    An Android application to discover, save, and create your favorite locations.
    <br />
    <br />
    Project for the course 119310 Mobile Application Development at the Media University Stuttgart.
  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#software-architecture">Software Architecture</a></li>
    <li><a href="#still-left-open">Still Left Open</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

The application lets you explore the world around you, constantly showing you all locations around you (fetched from the Google Places API). You can store these locations as favorites of yours on your phone and look up information about them.

You can also create your own favorite locations, for which you need to take a picture, give them a name, description, category etc. These will be stored with your current coordinates on your phone.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



### Built With

This project was built using a modern Android development stack, focusing on a clean and scalable architecture.

*   [Kotlin](https://kotlinlang.org/)
*   [AndroidX Libraries](https://developer.android.com/jetpack/androidx) (AppCompat, Core KTX, Lifecycle, Navigation)
*   [MVVM Architecture](https://developer.android.com/jetpack/guide)
*   [Google Places API](https://developers.google.com/maps/documentation/places/web-service/overview)
*   [Room Persistence Library](https://developer.android.com/training/data-storage/room)
*   [Retrofit 2](https://square.github.io/retrofit/)
*   [Glide](https://github.com/bumptech/glide)
*   [Material Components for Android](https://material.io/develop/android/docs/getting-started)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

*   Android Studio IDE
*   An Android device or emulator with API level 26 or higher

### Installation

1.  **Get a Google Places API Key**
    You will need a valid API key for the Google Places API and Google Static Maps API. You can get one from the [Google Cloud Console](https://console.cloud.google.com/).
2.  **Clone the repo**
    ```sh
    git clone https://github.com/your_username/MyFavLocation.git
    ```
3.  **Enter your API Key**
    Open the file `app/src/main/java/de/hdm_stuttgart/myfavlocation/backend/data/Constants.kt` and replace the placeholder key with your own:
    ```kotlin
    const val PLACES_API_KEY = "YOUR_API_KEY_HERE"
    ```
4.  **Open in Android Studio**
    Open the cloned project directory in Android Studio. It will automatically sync the Gradle dependencies.
5.  **Run the app**
    Build and run the application on your Android device or emulator.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

*   **Home Screen**: Upon launching, the app requests location permissions and then displays a list of interesting places nearby fetched from the Google Places API. This list automatically refreshes every 30 seconds.
*   **Add New Location**: Use the floating action button to open the camera, take a picture of a new location, and then add details like a name, description, category, and rating. The app will save it with your current GPS coordinates.
*   **Search**: Navigate to the search tab to find specific locations by name. The app will query both the Google Places API and your local database of favorite places.
*   **Favorites**: The favorites tab shows a list of all the locations you have saved, either from nearby suggestions or ones you created yourself.
*   **Location Details**: Tapping on any location in a list will open a detail view with its image, map, rating, description, and proximity. For your own created locations, you can edit these details. You can also add a location to your favorites or remove it from this screen.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ROADMAP -->
## Software Architecture

We designed our App using the **MVVM (Model-View-ViewModel)** architectural pattern.

### 2.1. Basic Structure

#### 2.1.1. Frontend
The frontend consists of the **Views** (Activities and Fragments) and of the **ViewModels**. As usual in MVVM, the views don't contain any logic themselves and only listen to updates in the `LiveData` objects that are stored in the ViewModel classes. The ViewModels can then communicate with the backend and fetch data from there.

#### 2.1.2. Backend / Repositories
There exists a persistent, lifecycle-independent repository for each of the fragments and activities in the application, as well as so-called **Master-Repositories**, that manage backend-activity such as **API-calls**, **DatabaseAccess**, and **GPS**.

### 2.2. Key Features

#### 2.2.1. Service Manager
Due to permissions being requested at runtime, we need a convenient way for all the repositories to get the information, which permission has and which hasn't been granted. This is the job of the `ServiceManager`. Repositories can register themselves with the `ServiceDependent` Interface and get notified whenever some permissions change.

#### 2.2.2. Injector Utils
There are a lot of dependencies going on in an MVVM architecture. It is very convenient to handle all of these dependencies in a central place, so changes to the architecture will only affect this class. Also, with this scheme, you can easily provide mock-data, so you don't have to test your application with the data stored in the local DB.

#### 2.2.3. DataCaches
We use Datacaches in order to avoid unnecessarily calling the API and the local DB all the time.

#### 2.2.4. Communication Infrastructure
Another minor issue in the project is the communication between all the different parts of the program. Traversing vertically along our UML was really easily done using MVVM, since most classes have a direct dependency to their lower-level counterparts and thus communication via method calls and return types was easy to do.

However, there is still a lot of communication that either needs to happen asynchronously (such as Database or HTTP-API calls) and also communication between the different repositories. This can all happen via callbacks, but the more communication is required, the less readable the code becomes.

Currently, we are using callback-interfaces for both the Google Places API and for our local DB, as well as a third interface for parameterless general-purpose communication between different parts of the application.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

## Still Left Open

### 3.1. Kotlin Coroutines
Unfortunately, we weren't able to implement our database-access using Kotlin Coroutines. Multiple asynchronous database-accesses always led to lost updates in one form or another. Also, in terms of displaying the data in an asynchronous way was many times more complicated, since we wanted all our data to be displayed in a deterministic order. We would need to use multiple buffers for the different chunks of data and then concatenate them in our specified order, once all database-calls were completed. We unfortunately weren't yet able to implement this scheme.

### 3.2. Cache Tracking / Image Caching
The way we interact with the API is to call once for a list of locations, then generate the URLs for the images and load them separately with Glide. Currently, only the initial request is cached, while images will be retrieved every time the user switches fragments. This can lead to high data consumption, which is why it would be very convenient to cache the images somewhere and only update changes.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also simply open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1.  Fork the Project
2.  Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3.  Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4.  Push to the Branch (`git push origin feature/AmazingFeature`)
5.  Open a Pull Request

### Original Contributors:

*   Joel Dettinger (jd087)
*   Sven Walter (sw201)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- LICENSE -->
## License

Distributed under the MIT License. See `LICENSE.txt` for more information.

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- CONTACT -->
## Contact

Joel Dettinger - jd087@hdm-stuttgart.de

Sven Walter - sw201@hdm-stuttgart.de

Project Link: [https://github.com/your_username/MyFavLocation](https://github.com/your_username/MyFavLocation)

<p align="right">(<a href="#readme-top">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->
## Acknowledgments

*   This project was created as part of the course **119310 Mobile Application Development** at the Media University Stuttgart (Hochschule der Medien).
*   README template adapted from [Best-README-Template](https://github.com/othneildrew/Best-README-Template)

<p align="right">(<a href="#readme-top">back to top</a>)</p>
