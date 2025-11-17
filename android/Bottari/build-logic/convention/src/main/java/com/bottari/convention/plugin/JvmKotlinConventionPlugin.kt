package com.bottari.convention.plugin

import com.bottari.convention.ApplicationConfig
import com.bottari.convention.Plugins
import com.bottari.convention.applyPlugins
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension

internal class JvmKotlinConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.JAVA_LIBRARY, Plugins.KOTLIN_JVM)

            extensions.configure<JavaPluginExtension> {
                sourceCompatibility = ApplicationConfig.JavaVersion
                targetCompatibility = ApplicationConfig.JavaVersion
            }

            extensions.configure<KotlinProjectExtension> {
                jvmToolchain(ApplicationConfig.JAVA_VERSION_AS_INT)
            }
        }
    }
}
