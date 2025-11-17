package com.bottari.convention.plugin

import com.bottari.convention.Plugins
import com.bottari.convention.applyPlugins
import com.bottari.convention.implementation
import com.bottari.convention.ksp
import com.bottari.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal class AndroidHiltConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.HILT, Plugins.KSP)

            dependencies {
                implementation(libs.findLibrary("hilt.android").get())
                ksp(libs.findLibrary("hilt.compiler").get())
                ksp(libs.findLibrary("androidx.hilt.compiler").get())
            }
        }
    }
}
