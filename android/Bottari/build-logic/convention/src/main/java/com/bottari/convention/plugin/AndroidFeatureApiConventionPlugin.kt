package com.bottari.convention.plugin

import com.bottari.convention.internal.implementation
import com.bottari.convention.internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureApiConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("serialization")
            dependencies {
                implementation(libs.findLibrary("androidx.navigation3.runtime"))
                implementation(libs.findLibrary("kotlinx.serialization.core"))
            }
        }
    }
}
