plugins {
    alias(libs.plugins.bottari.android.library.compose)
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "com.bottari.core.ui"

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    api(projects.core.designsystem)
    implementation(projects.core.domain) // NetworkManger 때문에 추가함
    implementation(projects.core.logger)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)
}
