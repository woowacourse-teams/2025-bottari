package com.bottari.convention.plugin

import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.implementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

internal class AndroidFeatureConventPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(
                "bottari.android.library.compose",
                "bottari.android.hilt",
            )

            dependencies {
                implementation(project(":domain"))
                implementation(project(":di"))
                implementation(project(":logger"))
            }
        }
    }
}
