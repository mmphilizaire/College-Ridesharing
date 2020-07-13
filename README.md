Original App Design Project - README Template
===

# College Carpool (need to change the name)

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
College Carpool is an application for university students only to carpool with other students to and from their university. Students who are offering rides will input the ride information (price per passenger, number of seats available, starting and ending locations, departure time and date) into the app. Students who are searching for a ride will input their desired starting and ending location and departure date/time. The app will then load a list of available rides that are within a specified radius of the inputted start and ending location. Users can message other students to find out more information about the ride. 

### App Evaluation
- **Category:** Transportation
- **Mobile:** The app uses location and maps to track the user and allow them to input the starting location and destination. Users are able to message other users and enable push notifications from the messages on the app. App also sends push notifications when users upload ride information that matches their requested ride. 
- **Story:** App allows students to find rides being offered by other university students for a cheaper price than other ride sharing apps such as Uber and Lyft. Students who are offering rides can post on the app and request a fee per seat in the carpool. Both parties benefit from the app. 
- **Market:** Any university student who is travelling long distance would find this app useful. Many students struggle to find a way to get home from college (and vice versa) and there are plenty of students with cars who drive home from campus (and vice versa) who have seats for extra passengers and want to make some extra money. In addition to find rides to and from home, the app would also be useful in finding rides to big events occuring nearby or to other college campuses that would be too expensive to Uber/Lyft. 
- **Habit:** Users would use the app if they are requesting a ride or offering a ride. In the process of finding a ride or finding ride passengers they would use the app very frequently to communicate with other students on the app. 
- **Scope:** The app would include a sign in for users and a way for them to post rides. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* User can create a new account
* User can login
* User can post a ride and upload the following information:
    * Starting location
    * Ending location
    * Ride date & time
    * Price per seat
    * Number of available seats
* User can request a ride with the following information:
    * Starting location
    * Ending location
    * Ride date & time range
* User can scroll through list of ride offers & requests
* User can click on a ride request or offer to view more details
* User can book a seat in a ride
* User can create a ride offer based on a user's ride request
* User can view their own profile and other profiles to see more details

**Optional Nice-to-have Stories**

* User can filter the stream of ride offers/requests by inputing information such as date, time, location (to/from), and price
* Users can upload a profile picture
* Users can rate other users in their carpool after the ride
* User can message other users to confirm details about a ride
* After requesting a ride, users can view rides matching their request and select a ride. If there are no rides matching their request or the user doesn't want to use the rides available, the request will go through
* After offering a ride, users can view requests matching their request and offer their ride to the user.
* Users can get push notifications when other users upload a ride offer that matches or is similar to their request
* Users can get push notificatins when other uers upload a ride request that matches or is similar to their offer 
* Users can pay through the app

### 2. Screen Archetypes

* Login Screen
   * User can login to their account
* Registration Screen
   * User can create a new account
* Ride Request Screen
    * User can input information and request a ride
* Ride Offer Screen
    * User can input information and offer a ride
* User Profile Screen
    * User can view profile information of themself or another user
* Ride Stream Screen
    * Ride Offers Stream Screen
        * Users can scroll through list of ride offers
    * Ride Requests Stream Screen
        * Users can scroll through list of ride requests
* Ride Detail Screen
    * Ride Offer Detail Screen
        * Users can view additional information about a ride offer
    * Ride Request Detail Screen
        * User can view additional information about a ride request

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Ride Stream Screen
* Ride Request Screen
* Ride Offer Screen
* User Profile Screen

**Flow Navigation** (Screen to Screen)

* Login Screen
--> Ride Offer Stream Screen
--> Registration
* Registration
--> Ride Offer Stream Screen
* Ride Request Stream Screen
--> Ride Request Detail Screen 
--> Ride Offers Stream Screen
* Ride Offer Stream Screen
--> Ride Offers Detail Screen 
--> Ride Request Stream Screen
* Ride Offer Detail Screen
--> User Profile Screen
* Ride Request Detail Screen
--> User Profile Screen
--> Ride Offer Screen
* Ride Request Screen
--> 
* Ride Offer Screen
--> 
* User Profile Screen
--> None


## Wireframes

<img src="wireframes.png" width=600>

## Schema

### Models

### Models

#### User

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | name          | String   | name of the user |
   | university    | String   | name of the user's university |
   | username      | String   | unique username for the user to login |
   | password      | String   | password for user to login |
   | profilePicture| File     | user's profile image |
   | createdAt     | DateTime | date when user profile is created (default field) |
   | updatedAt     | DateTime | date when user profile is last updated (default field) |
   
#### Ride Request

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the ride request (default field) |
   | user          | Pointer to User| user who requested the ride |
   | earliestDeparture  | DateTime     | earliest departure date & time |
   | latestDeparture    | DateTime   | latest departure date & time |
   | startLocation | Pointer to Location   | location of the requested start of the ride |
   | endLocation    | Pointer to Location   | location of the requested end of the ride |
   | createdAt     | DateTime | date when ride request is created (default field) |
   | updatedAt     | DateTime | date when ride request is last updated (default field) |
   
#### Ride Offer

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the ride request (default field) |
   | driver          | Pointer to User| user who is offering the ride |
   | departure  | DateTime     | ride departure date & time |
   | startLocation | Pointer to Location   | location of the start of the ride |
   | endLocation    | Pointer to Location   | location of the end of the ride |
   | pricePerSeat    | Number   | price per seat in the ride |
   | seats    | Number   | number of seats in the ride |
   | passengers    | Array of Pointers to User   | booked passengers for the ride |
   | createdAt     | DateTime | date when ride request is created (default field) |
   | updatedAt     | DateTime | date when ride request is last updated (default field) |
   
#### Location

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the location (default field) |
   | Longitude     | Number | longitude of the location |
   | Latitude      | Number     | latitude of the location |
   | City          | String  | city of the location |
   | State         | String   | state of the location |
   | createdAt     | DateTime | date when location is created (default field) |
   | updatedAt     | DateTime | date when location is last updated (default field) |

### Networking
#### List of network requests by screen
- Login Screen
    - (Read/GET) Use login method to login using the inputted username and password
        ```java
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
                //TODO: go to ride offer stream screen
            }
        });
        ```

- Registration Screen
    - (Create/POST) Create a new user with inputted information

        ```java
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(LoginActivity.this, "Issue with login!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_SHORT);
                //TODO: go to ride offer stream screen
            }
        });
        ```

- Ride Request Screen
    - (Create/POST) Create a new ride request with inputted information

        ```java
        RideRequest request = new RideRequest();
        request.put("user", currentUser);
        request.put("earliestDeparture", earliest);
        request.put("latestDeparture", latest);
        request.put("startLocation", startLocation);
        request.put("endLocation", endLocation);
        request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Ride request was successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ```

- Ride Offer Screen
    - (Create/POST) Create a new ride offer with inputted information

        ```java
        RideOffer offer = new RideOffer();
        offer.put("user", currentUser);
        offer.put("departure", departure);
        offer.put("startLocation", startLocation);
        offer.put("endLocation", endLocation);
        offer.put("pricePerSeat", price);
        offer.put("seats", seats);
        offer.put("passengers", new ArrayList<User>());
        offer.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getContext(), "Ride offer was successful!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        ```

- User Profile Screen
    - (Read/GET) Query logged in user object

    ```java
    ParseUser currentUser = ParseUser.getCurrentUser();
    String name = currentUser.getString("name");
    String university = currentUser.getString("university");
    ParseFile profiePicture = currentUser.getParseFile("profilePicture");
    //TODO: input information on the screen
    
    ```

    - (Update/PUT) Update user profile picture
    ```java
    //TODO: get new profile picture and set as variable
    ParseUser currentUser = ParseUser.getCurrentUser();
    currentuser.put("profilePicture", newProfilePicture)
    ```

- Ride Offers Stream Screen
    - (Read/GET) Query all future ride offers that satisfy criteria inputted by the user
    ```java
    ParseQuery<Post> query = ParseQuery.getQuery(RideOffers.class);
    query.include(Post."user");
    //TODO: ensure that ride offers satisfy criteria inputted by user using query.whereEqualTo() method
    query.setLimit(20);
    query.addDescendingOrder(Post."createdAt");
    query.findInBackground(new FindCallback<Post>() {
        @Override
         public void done(List<Post> posts, ParseException e) {
            if(e != null){
                 //TODO: alert user of error
                return;
            }
            //TODO: show the list of posts
            }
        });
    ```

- Ride Requests Stream Screen
    - (Read/GET) Query all future ride requests that satisfy criteria inputted by the user
        ```java
        ParseQuery<Post> query = ParseQuery.getQuery(RideRequest.class);
        query.include(Post."user");
        //TODO: ensure that ride offers satisfy criteria inputted by user using query.whereEqualTo() method
        query.setLimit(20);
        query.addDescendingOrder(Post."createdAt");
        query.findInBackground(new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if(e != null){
                    //TODO: alert user of error
                    return;
                }
                //TODO: show the list of posts
                }
            });
        ```

- Ride Offer Detail Screen
    - (Read/GET) Query specific ride offer information
        ```java
        Location startLocation = rideOffer.getParseObject("startLocation");
        Location endLocation = rideOffer.getParseObject("endLocation");
        User user = rideOffer.getParseObject("driver");
        DateTime departure = rideOffer.getDateTime("departure");
        
        ```

- Ride Request Detail Screen
    - (Read/GET) Query specific ride request information
    - (Read/GET) Query ride request user information

    
