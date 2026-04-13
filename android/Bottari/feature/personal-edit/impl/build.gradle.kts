plugins {
    alias(libs.plugins.bottari.android.feature.impl)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.feature.personal.edit.impl"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.personalEdit.api)
    implementation(libs.wheel.picker.compose)
}
