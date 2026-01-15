plugins {
    alias(libs.plugins.bottari.android.feature)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.feature.template"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.templateCreate)
    implementation(projects.feature.templateDetail)
}
