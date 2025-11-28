package com.bottari.convention.internal

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal fun Project.configureAndroid(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        compileSdk = ApplicationConfig.COMPILE_VERSION
        defaultConfig.minSdk = ApplicationConfig.MIN_VERSION

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        extensions.configure<KotlinProjectExtension> {
            jvmToolchain(ApplicationConfig.JAVA_VERSION_AS_INT)
        }

        buildFeatures {
            buildConfig = true
        }
    }
}
