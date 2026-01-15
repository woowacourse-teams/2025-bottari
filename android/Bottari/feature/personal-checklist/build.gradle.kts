plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.personal.checklist"
}

dependencies {
    implementation(projects.feature.personalEdit)
    implementation(libs.swipeable.cards)
}
