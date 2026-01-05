plugins {
    alias(libs.plugins.bottari.android.library.compose)
}

android {
    namespace = "com.bottari.core.ui"
}

dependencies {
    api(project(":core:designsystem"))
}
