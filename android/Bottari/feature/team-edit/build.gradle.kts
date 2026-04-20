plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.team.edit"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.personalEdit.api)
    implementation(projects.feature.teamChecklist)
}
