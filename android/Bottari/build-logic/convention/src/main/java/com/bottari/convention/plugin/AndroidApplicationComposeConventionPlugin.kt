package com.bottari.convention.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.bottari.convention.Plugins
import com.bottari.convention.applyPlugins
import com.bottari.convention.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.KOTLIN_COMPOSE, "bottari.android.application")

            extensions.configure<ApplicationExtension> {
                configureCompose(this)
            }
        }
    }
}
