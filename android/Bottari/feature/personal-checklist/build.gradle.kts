plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.personal.checklist"
}

dependencies {
    implementation(projects.core.ui)
    implementation(libs.swipeable.cards)
}
