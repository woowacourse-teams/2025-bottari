import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.bottari.android.feature)
    id("kotlin-parcelize")
}

android {
    namespace = "com.bottari.presentation"

    val localProperties = gradleLocalProperties(rootDir, providers)

    fun getPropertyOrThrow(key: String): String = localProperties.getProperty(key) ?: error("$key is missing in local.properties")

    defaultConfig {
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
        buildConfigField(
            "int",
            "APP_VERSION_CODE",
            "${
                System.getenv("VERSION_CODE")?.toIntOrNull() ?: libs.versions.versionCode
                    .get()
                    .toInt()
            }",
        )
    }

    buildTypes {
        release {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${getPropertyOrThrow("RELEASE_BASE_URL")}\"",
            )
        }
        debug {
            buildConfigField(
                "String",
                "BASE_URL",
                "\"${getPropertyOrThrow("DEBUG_BASE_URL")}\"",
            )
        }
    }

    testOptions {
        unitTests.all { it.useJUnitPlatform() }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.hilt.work)

    implementation(libs.material)
    implementation(libs.cardstackview)
    implementation(libs.flexbox)
    implementation(libs.spinkit)
    implementation(libs.swipeable.cards)
    implementation(libs.wheel.picker.compose)

    api(platform(libs.firebase.bom))
    api(libs.firebase.messaging)

    testImplementation(libs.bundles.test)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
