plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

configurations.all {
    resolutionStrategy {
        force("net.bytebuddy:byte-buddy:1.14.12")
    }
}

dependencies {
    testImplementation(libs.bundles.test)
    implementation(libs.kotlinx.coroutines.core)
}
