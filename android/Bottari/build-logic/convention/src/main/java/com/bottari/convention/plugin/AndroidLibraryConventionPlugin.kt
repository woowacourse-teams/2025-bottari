package com.bottari.convention.plugin

import com.android.build.api.dsl.LibraryExtension
import com.bottari.convention.internal.ApplicationConfig
import com.bottari.convention.internal.Plugins
import com.bottari.convention.internal.androidTestImplementation
import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.configureAndroid
import com.bottari.convention.internal.configureBuildTypes
import com.bottari.convention.internal.libs
import com.bottari.convention.internal.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.ANDROID_LIBRARY, Plugins.KOTLIN_ANDROID)

            extensions.configure<LibraryExtension> {
                configureAndroid(this)
                configureBuildTypes(this)

                testOptions.targetSdk = ApplicationConfig.TARGET_VERSION
            }

            dependencies {
                testImplementation(libs.findBundle("test").get())
                androidTestImplementation(libs.findLibrary("androidx.junit").get())
                androidTestImplementation(libs.findLibrary("androidx.espresso.core").get())
            }
        }
    }
}
