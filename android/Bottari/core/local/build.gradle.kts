plugins {
    alias(libs.plugins.bottari.android.library)
    alias(libs.plugins.bottari.android.hilt)
    alias(libs.plugins.serialization)
}

android {
    namespace = "com.bottari.core.local"
}

dependencies {
    implementation(project(":core:domain"))
    implementation(project(":core:logger"))

    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.core.ktx)
    implementation(libs.bundles.local)
    implementation(libs.kotlinx.serialization.json)

    testImplementation(libs.bundles.test)
    testImplementation(libs.kotest.assertions.core)
    androidTestImplementation(libs.androidx.junit)
}
