plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.callanalyzer"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.callanalyzer"
        minSdk = 23
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")   // ✅ downgrade
    implementation("androidx.activity:activity-compose:1.7.2") // ✅ downgrade

    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")

    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
}
