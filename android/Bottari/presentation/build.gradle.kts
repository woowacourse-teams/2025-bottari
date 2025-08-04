import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.bottari.presentation"
    compileSdk = 35

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val devId = gradleLocalProperties(rootDir, providers).getProperty("DEV_ID") ?: ""
        buildConfigField("String", "DEV_ID", "\"$devId\"")

        val privacyPolicyUrl =
            gradleLocalProperties(rootDir, providers).getProperty("PRIVACY_POLICY_URL") ?: ""
        buildConfigField("String", "PRIVACY_POLICY_URL", "\"$privacyPolicyUrl\"")

        val userFeedbackUrl =
            gradleLocalProperties(rootDir, providers).getProperty("USER_FEEDBACK_URL") ?: ""
        buildConfigField("String", "USER_FEEDBACK_URL", "\"$userFeedbackUrl\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
    }
    testOptions {
        unitTests.all {
            it.useJUnitPlatform()
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":di"))

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
    implementation(libs.cardstackview)
    implementation(libs.flexbox)
    implementation(libs.number.picker)
    implementation(libs.spinkit)
    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
