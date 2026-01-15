plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.team.edit"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)
    implementation(projects.feature.personalEdit)
    implementation(projects.feature.teamChecklist)
}
