plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.bottari.logger"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
}

configurations.all {
    resolutionStrategy {
        force("net.bytebuddy:byte-buddy:1.14.12")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
}
