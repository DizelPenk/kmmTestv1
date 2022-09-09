plugins {
    id("com.android.application")
    id("kotlin-android-extensions")
    kotlin("android")
}

android {
    compileSdk = 32
    defaultConfig {
        applicationId = "com.example.kmmtest.androidApp"
        minSdk = 19
        targetSdk = 32
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.appcompat:appcompat:1.4.2")
    implementation("com.google.android.material:material:1.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    //Swipe Fresh Layout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
}