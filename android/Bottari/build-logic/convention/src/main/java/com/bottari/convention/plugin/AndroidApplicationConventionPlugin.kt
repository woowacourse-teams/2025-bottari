package com.bottari.convention.plugin

import com.android.build.api.dsl.ApplicationExtension
import com.bottari.convention.internal.ApplicationConfig
import com.bottari.convention.internal.Plugins
import com.bottari.convention.internal.applyPlugins
import com.bottari.convention.internal.configureAndroid
import com.bottari.convention.internal.configureBuildTypes
import com.bottari.convention.internal.implementation
import com.bottari.convention.internal.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

internal class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            applyPlugins(Plugins.ANDROID_APPLICATION, Plugins.KOTLIN_ANDROID)

            extensions.configure<ApplicationExtension> {
                configureAndroid(this)
                configureBuildTypes(this)

                defaultConfig {
                    targetSdk = ApplicationConfig.TARGET_VERSION
                    versionName = libs.findVersion("versionName").get().toString()
                    versionCode =
                        libs
                            .findVersion("versionCode")
                            .get()
                            .toString()
                            .toInt()
                }
            }

            dependencies {
                implementation(project(":domain"))
                implementation(project(":di"))
                implementation(project(":presentation"))
                implementation(project(":logger"))
            }
        }
    }
}
