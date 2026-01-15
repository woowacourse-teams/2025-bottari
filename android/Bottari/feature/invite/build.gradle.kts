plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.invite"
}

dependencies {
    implementation(projects.feature.mybottari)
}
