plugins {
    alias(libs.plugins.bottari.android.application.compose)
    alias(libs.plugins.bottari.android.hilt)
    alias(libs.plugins.google.gms.google.services)
    alias(libs.plugins.google.firebase.crashlytics)
}

android {
    namespace = "com.bottari.bottari"
}

tasks.register("printVersionName") {
    doLast {
        println(libs.versions.versionName.get())
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.navigation)

    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.bundles.androidx.compose.navigation)

    // compose migration 완료 후 제거
    implementation(libs.androidx.appcompat)

    // features
    implementation(projects.feature.template)
    implementation(projects.feature.mybottari)
    implementation(projects.feature.more)
}
