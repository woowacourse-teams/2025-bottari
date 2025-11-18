package com.bottari.convention.plugin

import com.android.build.api.dsl.LibraryExtension
import com.bottari.convention.internal.Plugins
import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.KOTLIN_COMPOSE, "bottari.android.library")

            extensions.configure<LibraryExtension> {
                configureCompose(this)
            }
        }
    }
}
