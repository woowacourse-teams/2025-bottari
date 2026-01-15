import com.bottari.convention.external.getPropertyOrThrow

plugins {
    alias(libs.plugins.bottari.android.library)
    alias(libs.plugins.bottari.android.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.core.network"

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
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:logger"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.network)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.installations)
    implementation(libs.firebase.config)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.androidx.junit)
}
