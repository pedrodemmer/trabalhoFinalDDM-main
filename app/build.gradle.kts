plugins {
    id("com.google.devtools.ksp")
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.trabalhofinal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.trabalhofinal"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Android Room
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("androidx.test.espresso:espresso-core:3.6.1")
    val lifecycle_version = "2.8.7"
    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // ViewModel utilities for Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycle_version")

    implementation("androidx.core:core-ktx:1.13.1")

    val nav_version = "2.8.9"

    implementation("androidx.compose.material:material")
    implementation(platform("androidx.compose:compose-bom:2024.05.00"))
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-text")
    implementation("androidx.compose.material:material-icons-core")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.navigation:navigation-compose:$nav_version")

    testImplementation(libs.junit)
}