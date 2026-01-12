plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.main"
}

dependencies {
    implementation(projects.feature.template)
    implementation(projects.feature.mybottari)
    implementation(projects.feature.more)
}
