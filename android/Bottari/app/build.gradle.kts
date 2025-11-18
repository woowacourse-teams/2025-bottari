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
    implementation(libs.androidx.hilt.work)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.appcompat)
}
