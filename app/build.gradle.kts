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

    // ✅ FIX: Align Java + Kotlin to 17
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")

    // ✅ Fix transitive emoji issue
    implementation("androidx.emoji2:emoji2:1.3.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
}

// ✅ Ensure consistent dependency resolution
configurations.all {
    resolutionStrategy {
        force("androidx.emoji2:emoji2:1.3.0")
    }
}
