# CropAdvisor mobile application
Developed in Code4Good Hackathon

## Introduction
This application act as a client application of [crop-service](https://github.com/Prasadct/crop-service) server application. This client server application communicate through HTTP protocol and data transfered in JSON format. Service application is a REST web service developed using NodeJS. All the degitized data are stored in the server side and the mobile application stored only chached data. Data caching is done when the first use of the each Android activity.

Mobile application is developed using Android Studio. 
 - _compileSdkVersion 24_   
 - _buildToolsVersion "24.0.0"_
 - _minSdkVersion 15_
 - _targetSdkVersion 24_

Each of Android activity named in a meamingful manner thus the functionaliy of each activity can undersdand easily.
Service IP address and HTTP Port need to congigured before building the application. SQLite database and Android file system is used to as the application storage. 
