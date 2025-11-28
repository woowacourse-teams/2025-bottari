package com.bottari.convention.internal

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Project

internal fun Project.configureBuildTypes(extension: CommonExtension<*, *, *, *, *, *>) {
    when (extension) {
        is ApplicationExtension -> configureApplicationBuildTypes(extension)
        is LibraryExtension -> configureLibraryBuildTypes(extension)
        else -> logger.warn("No buildTypes available for $extension")
    }
}

private fun configureApplicationBuildTypes(extension: ApplicationExtension) {
    extension.buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = false

            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "보따리 (Dev)")
        }

        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                extension.getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
}

private fun configureLibraryBuildTypes(extension: LibraryExtension) {
    extension.buildTypes {
        // Nothing to configure
    }
}
