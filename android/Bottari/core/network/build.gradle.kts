import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.bottari.core.network"
    compileSdk =
        libs.versions.compileSdk
            .get()
            .toInt()

    val localProperties = gradleLocalProperties(rootDir, providers)

    fun getPropertyOrThrow(key: String): String = localProperties.getProperty(key)?.trim() ?: error("$key is missing in local.properties")

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        buildConfig = true
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":logger"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.network)

    ksp(libs.hilt.compiler)
    implementation(libs.hilt.android)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.installations)
    implementation(libs.firebase.config)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.androidx.junit)
}
