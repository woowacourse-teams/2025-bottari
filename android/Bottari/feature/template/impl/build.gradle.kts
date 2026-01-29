plugins {
    alias(libs.plugins.bottari.android.feature.impl)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.feature.template.impl"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.template.api)
    implementation(projects.feature.templateCreate)
    implementation(projects.feature.templateDetail)
}
