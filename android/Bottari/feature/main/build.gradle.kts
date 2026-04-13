plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.main"
}

dependencies {
    implementation(projects.feature.invite)
    implementation(projects.feature.mybottari)
    implementation(projects.feature.more)
    implementation(projects.feature.personalChecklist)
    implementation(projects.feature.personalEdit.api)
    implementation(projects.feature.personalEdit.impl)
    implementation(projects.feature.teamChecklist)
    implementation(projects.feature.teamEdit)
    implementation(projects.feature.template.api)
    implementation(projects.feature.template.impl)
    implementation(projects.feature.templateCreate)
    implementation(projects.feature.templateDetail)
}
