plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.template.detail"
}

dependencies {
    implementation(projects.feature.personalEdit.api)
}
