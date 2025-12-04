plugins {
    alias(libs.plugins.bottari.android.library.compose)
}

android {
    namespace = "com.bottari.core.designsystem"
}

dependencies {
    implementation(project(":core:ui"))
}
