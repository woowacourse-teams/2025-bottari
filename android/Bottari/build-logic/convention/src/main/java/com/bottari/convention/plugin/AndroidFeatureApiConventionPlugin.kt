package com.bottari.convention.plugin

import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.implementation
import com.bottari.convention.internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(
                "bottari.android.library",
                "org.jetbrains.kotlin.plugin.serialization",
            )
            dependencies {
                implementation(libs.findLibrary("androidx.navigation3.runtime").get())
                implementation(libs.findLibrary("kotlinx.serialization.core").get())
            }
        }
    }
}
