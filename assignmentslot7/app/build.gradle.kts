plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.example.moviecatalog'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.moviecatalog"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }

    buildFeatures {
        compose true
    }

    composeOptions {
        kotlinCompilerExtensionVersion '1.5.1'
    }

    kotlinOptions {
        jvmTarget = '1.8'
    }
}

dependencies {
    implementation "androidx.core:core-ktx:1.13.1"
    implementation "androidx.activity:activity-compose:1.9.0"
    implementation platform("androidx.compose:compose-bom:2024.05.00")
    implementation "androidx.compose.ui:ui"
    implementation "androidx.compose.material3:material3"
    implementation "androidx.constraintlayout:constraintlayout-compose:1.0.1"

    // Coil for image loading
    implementation "io.coil-kt:coil-compose:2.6.0"
}
