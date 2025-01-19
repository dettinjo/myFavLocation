# MyFavLocation - Android Application

#### Contributors:

- Joel Dettinger ( jd087)
- Sven Walter ( sw201)

## 1. Description

The application lets you explore the world around you, constantly showing you all locations around you 
(fetched from the *google-places-api*). You can store these locations as favorites of yours on your phone
and look up informations about them. 

You can also create your own favorite locations, for which you need to take a picture, give them a name, description,
category etc. These will be stored with your current coordinates on your phone.


## 2. Software Architecture

We designed our App using the *MVVM* architectural pattern.

### 2.1. Basic Structure

#### 2.1.1. Frontend

The frontend consists of the **Views** (Activities and Fragments) and of the **Viewmodels**. 
As usual in *MVVM* the views don't contain any logic themselves and only listen to updates in the *live-data*
objects that are stored in the viewmodel-classes. The *viewmodels* can then communicate with the backend
and fetch data from there

#### 2.1.2. Backend / Repositories

There exists a persistent, lifecycle independent repository for each of the fragments and activities
in the application, aswell as so called **Master-Repositories**, that manage backend-activity
such as **API-calls**, **DatabaseAccess** and **GPS**.

### 2.2. Key Features

#### 2.2.1. Service Manager

Due to permissions being requested at runtime we need a convient way for all the 
repositories to get the information, which permission has and which hasn't been granted.
This is the job of the `ServiceManager`. Repositories can register themselves with the `ServiceDependent`
Interface and get notified whenever some permissions change.

#### 2.2.2. Injector Utils

There are a lot of dependencies going on in a *MVVM* architecture. It is very convenient to handle
all of these dependencies in a central place, so changes to the architecture will only affect this class.
Also with this scheme you can easily provide mock-data, so you don't have to test your application with the
data stored in the local db.

#### 2.2.3. DataCaches

We use Datacaches in order to avoid unnessecarily calling the API and the local DB all the time.

#### 2.2.4. Communication Infrastructure

Another minor issue in the project is the communication between all the different parts of the program.
Traversing vertically along our *UML* was really easily done using *MVVM*, since most classes have a direct
dependency to their lower level counterparts and thus communication via. Methodcalls and return-types was easy to do.

However there is still a lot of communication that either needs to happen 
asynchronously (such as Database or *http*-API calls) and also acommunication between the different repositories.
This can all happen via. *callbacks* but the more communication is required, the less readable the code becomes.

Currently we are using *callback-interfaces* for both the *google-places-API* and for our local db, aswell
as a third interface for parameterless general-purpose communication between different parts of the application.

### 3. Still left open

#### 3.1. Kotlin Coroutines

Unfortunatly, we weren't able to implement our database-access using *kotlin-coroutines*.
Multiple asynchronous database-accesses always led to lost updates in one form or another. Also in terms of
displaying the data in an asynchronous way was many times more complicated, 
since we wanted all our data to be displayed in a deterministic order. We would need to use multiple buffers for 
the different chunks of data and then concatenate them in our specified order, once all database-calls were completed.
We unfortunatly weren't yet able to implement this scheme. 

#### 3.2. Cache Tracking / Image Caching

The way we interact with the API is to call once for a list of locations, then generate the URL's for the images and load
them seperatly with *Glide*. Currently, only the initial request is cached, while images will be retrieved
everytime the user switches fragments. This can lead to high data-consumption, which is why it would
be very convient to cache the images somewhere and only update changes



