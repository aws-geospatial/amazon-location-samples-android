# Quick start App: Amazon Location Service Integration

This Quick Start app demonstrates how to load map and reverse geocode with Identity Pool Id and integrate Amazon Location Service with an Android application using Amazon Cognito to authenticate and track real-time location updates. It showcases the usage of Amazon Location Service's Tracking and Auth SDKs to authenticate users and track their location on a map.

- **Tracking SDK**: [Amazon Location Service Tracking SDK](https://github.com/aws-geospatial/amazon-location-mobile-tracking-sdk-android)
- **Auth SDK**: [Amazon Location Service Auth SDK](https://github.com/aws-geospatial/amazon-location-mobile-auth-sdk-android)

## Installation

1. Clone [this](https://github.com/aws-geospatial/amazon-location-samples-android.git) repository to your local machine.
2. Open the `quick-start` project in Android Studio.

   - In Android Studio, click on `File` -> `Open` and navigate to the `quick-start` folder in the cloned repository.
   - Select the project directory and open it.

3. Configure AWS credentials in the app using the following:

   **Configure custom.properties**: Fill in the required values in the `custom.properties` file to configure the AWS credentials. (Refer to the [Configuration](#configuration) section for details)

4. Build and run the app on your Android device or emulator.

## Configuration

Create a `custom.properties` file in your project directory if it does not exist and input the following lines, replacing placeholders with actual values:

 ```properties
IDENTITY_POOL_ID=<REGION>:xxxxxxxxxxxxxxxxxxxxxxxxxx
TRACKER_NAME=xxxxxxxxx
MAP_STYLE=xxxxxxxxxxxxxxxxxxx
API_KEY=xx.xxxxxx.xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
API_KEY_REGION=xxxxxxxxx
```

- **IDENTITY_POOL_ID**: Your AWS Cognito Identity Pool ID used for authenticating users for the Tracking/Geofencing services.
- **TRACKER_NAME**: The name of the tracker resource in Amazon Location Service used for tracking location updates.
- **MAP_STYLE**: The style of the map resource in the Amazon Location Service you want to use to display the map. ("Standard", "Monochrome", "Hybrid", or "Satellite")
- **API_KEY**: Your api key used for authenticating users to access maps, routes and places.
- **API_KEY_REGION**: Your api key AWS region.

These values are used to configure the app's connection to AWS services.

## Automated UI test case

With the below steps, you can run the automated UI test cases.

1. Create and run the emulator of the latest Android version or plug in the real Android device.
2. Now in Android studio open the `app/src/androidTest/DefaultFlowSuite.kt` file and click the play button.

## Unit test case

With the below step, you can run the Unit test cases.

Right-click on the `app/src/test [unitTest]` directory and select `Run Tests in'AndroidQuickStartApp.app.unitTest'`.