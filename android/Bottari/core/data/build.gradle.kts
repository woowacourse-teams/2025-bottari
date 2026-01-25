plugins {
    alias(libs.plugins.bottari.android.library)
    alias(libs.plugins.bottari.android.hilt)
}

android {
    namespace = "com.bottari.core.data"
}

dependencies {
    implementation(project(":core:local"))
    implementation(project(":core:network"))
    implementation(project(":core:domain"))
    implementation(project(":core:logger"))

    implementation(libs.androidx.room.runtime)
    implementation(libs.retrofit)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.core)
}
