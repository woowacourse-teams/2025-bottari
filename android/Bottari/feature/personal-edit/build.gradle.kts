plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.personal.edit"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(libs.wheel.picker.compose)
}
