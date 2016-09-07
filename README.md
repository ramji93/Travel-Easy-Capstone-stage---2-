# Travel-Easy-Capstone-stage---2-
Travel Easy is a app for flight booking that searches and populates list of different itineraries available 
for a given flight journey ordered according to traveller’s preferences (like price, duration).
On drilling down a chosen itinerary, the app populates list of numerous travel agencies covering 
the same journey along with their price. Users can go to the booking link of any of the listed agencies
and book tickets or can share its booking link via sharing apps. 
(NOTE: Only for outbound flights)

Features
 Lists different itineraries just by entering the origin, destination and date of journey.
 Options to sort the itineraries list by user preferences (price, duration, arrival time).
 Displays all the travel agencies supporting the selected itinerary along with their prices at one place.
 Option to save a flight search and use it in the future.
 Provide a widget for listing the saved searches and clicking it takes directly to itinerary list screen within the app. 

Technical Details:

Google services:
1. google location service for autocompleting the locations.
2. Firebase analytics for keeping track of search requests by the user. (replaced for admob as mentioned in capstone stage 1)

Third Party Libraries:
1. Glide for loading images dynamically.
2. net.simonvt.schematic:schematic for content provider implementation.

Implemented Activity transition while exiting Itinerary list activity.

Makes use of content provider for saving flight searches.

API key should be entered in Travel-Easy-Capstone-stage---2-/app/src/main/res/values/strings.xml 

Signed release apk and keystore are located in Travel-Easy-Capstone-stage---2-/app  and keystore password is in app gradle file.



