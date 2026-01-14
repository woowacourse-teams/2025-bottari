plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.mybottari"
}

dependencies {
    implementation(projects.core.common)

    implementation(projects.presentation)
}
