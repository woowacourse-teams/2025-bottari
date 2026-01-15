plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.team.checklist"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.feature.personalChecklist)
    implementation(projects.core.ui)
}
