import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension

plugins {
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
    alias(libs.plugins.bottari.android.application.compose)
    alias(libs.plugins.bottari.android.hilt)
}

android {
    namespace = "com.bottari.bottari"

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

            configure<CrashlyticsExtension> {
                mappingFileUploadEnabled = false
            }
        }
    }
}

tasks.register("printVersionName") {
    doLast {
        println(libs.versions.versionName.get())
    }
}

dependencies {
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
}
