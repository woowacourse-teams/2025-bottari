plugins {
    alias(libs.plugins.bottari.jvm.kotlin)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.javax.inject)
    testImplementation(libs.bundles.test)
}
