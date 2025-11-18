package com.bottari.convention.internal

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureCompose(extension: CommonExtension<*, *, *, *, *, *>) {
    extension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            implementation(platform(libs.findLibrary("androidx.compose.bom").get()))
            implementation(libs.findBundle("androidx.compose").get())
            implementation(libs.findBundle("androidx.compose.navigation").get())
            debugImplementation(libs.findBundle("androidx.compose.debug").get())
        }
    }
}
