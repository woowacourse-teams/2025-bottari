import com.bottari.convention.external.getPropertyOrThrow

plugins {
    alias(libs.plugins.bottari.android.library)
    alias(libs.plugins.bottari.android.hilt)
}

android {
    namespace = "com.bottari.common"

    buildFeatures {
        buildConfig = true
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
}

dependencies {
}
