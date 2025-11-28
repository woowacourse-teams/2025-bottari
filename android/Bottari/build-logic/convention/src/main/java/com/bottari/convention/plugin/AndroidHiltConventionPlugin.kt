package com.bottari.convention.plugin

import com.bottari.convention.internal.Plugins
import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.implementation
import com.bottari.convention.internal.ksp
import com.bottari.convention.internal.libs
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
