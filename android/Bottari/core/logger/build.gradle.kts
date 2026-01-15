plugins {
    alias(libs.plugins.bottari.android.library)
}

android {
    namespace = "com.bottari.logger"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.timber)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics.ndk)
    implementation(libs.firebase.analytics)
}
