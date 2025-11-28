plugins {
    alias(libs.plugins.bottari.jvm.kotlin)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    testImplementation(libs.bundles.test)
}
