Weather Android application

The Weather will display the tempature of the place whereever the applciation is launched.
It will fetch the GPS(Latitude & Longitude) coordinate using the api available in Android SDK.Once the 
GPS coordinate are fetched properly,they are sent to openweathermap.org.
openweathermap.org is the website which provides the temperature details based on the assigned API key(Specific to user) and parse the URL received from android applciation.
openweathermap.org will sent back the response in the form of JSON object using webservices which will be parsed on android application.

For running the aplication,the device should have GPS and Internet service enabled.The application will display the place name,date,current time,Cloud condition ,pressure,humidity & temperature values.

The API key can be generated from http://api.openweathermap.org website and set in the WeatherUtility class.

Eclipse IDE is used to build the application using the android SDK.

android:minSdkVersion="14"
android:targetSdkVersion="23"

##Steps to run the application.

1.) Open Eclipse

2.) Set the android SDK in the preferences.

3.) Connect the device and test using "adb devices" command in the "Platform-tools" folder of SDK

4.) Enable the developer option in the target device

5.) Enable GPS and Internet service in the target device

6.) Build and run the application in the target device.

7.) Application will launch in the device with the details.

