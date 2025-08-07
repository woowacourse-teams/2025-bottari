import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.bottari.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 28
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        val localProperties = gradleLocalProperties(rootDir, providers)
        val debugBaseUrl: String = localProperties.getProperty("DEBUG_BASE_URL") ?: ""
        val releaseBaseUrl: String = localProperties.getProperty("RELEASE_BASE_URL") ?: ""

        release {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"$releaseBaseUrl\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", "\"$debugBaseUrl\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "21"
    }

    buildFeatures {
        buildConfig = true
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":logger"))

    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.network)
    implementation(libs.bundles.local)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    testImplementation(libs.kotest.assertions.core)
}
