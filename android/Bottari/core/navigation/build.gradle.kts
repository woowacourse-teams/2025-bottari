plugins {
    alias(libs.plugins.bottari.android.library.compose)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.core.navigation"
}

dependencies {
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.kotlinx.serialization.core)
}
