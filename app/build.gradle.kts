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
    // ✅ stable versions for SDK 33
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.activity:activity-compose:1.7.2")

    implementation("androidx.compose.ui:ui:1.5.3")
    implementation("androidx.compose.material3:material3:1.1.2")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.3")

    // ✅ FORCE older emoji2 (fixes your error)
    implementation("androidx.emoji2:emoji2:1.3.0")

    debugImplementation("androidx.compose.ui:ui-tooling:1.5.3")
}

configurations.all {
    resolutionStrategy {
        // ✅ Force ALL emoji2 usages to safe version
        force("androidx.emoji2:emoji2:1.3.0")
    }
}
