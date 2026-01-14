plugins {
    alias(libs.plugins.bottari.android.library.compose)
}

android {
    namespace = "com.bottari.core.ui"
}

dependencies {
    api(projects.core.designsystem)
    implementation(projects.core.domain) // NetworkManger 때문에 추가함
    implementation(projects.logger)
}
