plugins {
    alias(libs.plugins.bottari.android.feature)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.feature.mybottari"
}

dependencies {
    implementation(projects.core.common)

    implementation(projects.feature.personalChecklist)
    implementation(projects.feature.personalEdit)
    implementation(projects.feature.teamChecklist)
    implementation(projects.feature.teamEdit)
}
