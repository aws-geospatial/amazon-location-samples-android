import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "software.amazon.location.sample"
    compileSdk = 34
    val customConfig = Properties()
    customConfig.load(project.rootProject.file("custom.properties").inputStream())
    defaultConfig {
        applicationId = "software.amazon.location.sample"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "MQTT_END_POINT", "\"${customConfig.getProperty("MQTT_END_POINT")}\"")
        buildConfigField("String", "POLICY_NAME", "\"${customConfig.getProperty("POLICY_NAME")}\"")
        buildConfigField("String", "GEOFENCE_COLLECTION_NAME", "\"${customConfig.getProperty("GEOFENCE_COLLECTION_NAME")}\"")
        buildConfigField("String", "TOPIC_TRACKER", "\"${customConfig.getProperty("TOPIC_TRACKER")}\"")
        buildConfigField("String", "DEFAULT_TRACKER_NAME", "\"${customConfig.getProperty("DEFAULT_TRACKER_NAME")}\"")
        buildConfigField("String", "TEST_POOL_ID", "\"${customConfig.getProperty("TEST_POOL_ID")}\"")
        buildConfigField("String", "TEST_MAP_STYLE", "\"${customConfig.getProperty("TEST_MAP_STYLE")}\"")
        buildConfigField("String", "TEST_API_KEY_REGION", "\"${customConfig.getProperty("TEST_API_KEY_REGION")}\"")
        buildConfigField("String", "TEST_API_KEY", "\"${customConfig.getProperty("TEST_API_KEY")}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation("androidx.activity:activity-compose:1.9.3")
    implementation(platform("androidx.compose:compose-bom:2024.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("org.maplibre.gl:android-sdk:11.5.2")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("aws.sdk.kotlin:cognitoidentity:1.3.65")
    implementation("com.amazonaws:aws-iot-device-sdk-java:1.3.9")
    implementation("aws.sdk.kotlin:iot:1.3.29")
    implementation("aws.sdk.kotlin:location:1.3.65")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.7.5")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.7.5")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.3.0")
    implementation("software.amazon.location:auth:1.1.0")
    implementation("software.amazon.location:tracking:1.0.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.10.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
