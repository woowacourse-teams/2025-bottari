plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.personal.edit"
}

dependencies {
    implementation(projects.core.common)
    implementation(libs.wheel.picker.compose)
}
