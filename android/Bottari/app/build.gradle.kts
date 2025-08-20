import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.bottari.bottari"
    compileSdk =
        libs.versions.complieSdk
            .get()
            .toInt()

    defaultConfig {
        applicationId = "com.bottari.bottari"
        versionName = libs.versions.versionName.get()
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull()
            ?: libs.versions.versionCode
                .get()
                .toInt()
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        targetSdk =
            libs.versions.targetSdk
                .get()
                .toInt()

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }

        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "보따리 (Dev)")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

tasks.register("printVersionName") {
    doLast {
        println(android.defaultConfig.versionName)
    }
}

configurations.all {
    resolutionStrategy {
        force("net.bytebuddy:byte-buddy:1.14.12")
    }
}

dependencies {
    implementation(project(":di"))
    implementation(project(":presentation"))
    implementation(project(":logger"))

    implementation(libs.material)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
