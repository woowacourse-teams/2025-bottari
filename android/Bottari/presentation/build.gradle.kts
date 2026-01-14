import com.bottari.convention.external.getPropertyOrThrow

plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.presentation"

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
}
