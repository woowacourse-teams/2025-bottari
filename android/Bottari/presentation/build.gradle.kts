import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
}

android {
    namespace = "com.bottari.presentation"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    val localProperties = gradleLocalProperties(rootDir, providers)

    fun getPropertyOrThrow(key: String): String = localProperties.getProperty(key) ?: error("$key is missing in local.properties")

    defaultConfig {
        minSdk =
            libs.versions.minSdk
                .get()
                .toInt()
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "PRIVACY_POLICY_URL",
            "\"${getPropertyOrThrow("PRIVACY_POLICY_URL")}\"",
        )
        buildConfigField(
            "String",
            "USER_FEEDBACK_URL",
            "\"${getPropertyOrThrow("USER_FEEDBACK_URL")}\"",
        )
        buildConfigField("int", "APP_VERSION_CODE", "${libs.versions.versionCode.get().toInt()}")
    }

    buildTypes {
        release {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "BASE_URL", "\"${getPropertyOrThrow("RELEASE_BASE_URL")}\"")
        }
        debug {
            buildConfigField("String", "BASE_URL", "\"${getPropertyOrThrow("DEBUG_BASE_URL")}\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

configurations.all {
    resolutionStrategy {
        force("net.bytebuddy:byte-buddy:1.14.12")
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":di"))
    implementation(project(":logger"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.material)
    implementation(libs.cardstackview)
    implementation(libs.flexbox)
    implementation(libs.number.picker)
    implementation(libs.spinkit)
    implementation(libs.material.calendarview)
    implementation(libs.threetenabp)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    testImplementation(libs.bundles.test)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
