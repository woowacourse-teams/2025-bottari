plugins {
    alias(libs.plugins.bottari.android.application.compose)
    alias(libs.plugins.bottari.android.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.bottari.bottari"

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
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
}

tasks.register("printVersionName") {
    doLast {
        println(libs.versions.versionName.get())
    }
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.common)
    implementation(projects.feature.main)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.bundles.androidx.compose.navigation)
    implementation(libs.androidx.core.splashscreen)

    // compose migration 완료 후 제거
    implementation(libs.androidx.appcompat)
}
