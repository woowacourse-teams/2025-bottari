plugins {
    alias(libs.plugins.bottari.android.library.compose)
}

android {
    namespace = "com.bottari.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    implementation(projects.logger)
}
