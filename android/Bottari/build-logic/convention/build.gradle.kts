import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_21
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.compiler.gradle.plugin)
}

private fun getPluginIdFromProvider(provider: Provider<PluginDependency>): String = provider.get().pluginId

gradlePlugin {
    plugins {
        register("android.application") {
            id =
                getPluginIdFromProvider(
                    libs.plugins.bottari.android.application
                        .asProvider(),
                )
            implementationClass = "com.bottari.convention.plugin.AndroidApplicationConventionPlugin"
        }

        register("android.application.compose") {
            id = getPluginIdFromProvider(libs.plugins.bottari.android.application.compose)
            implementationClass =
                "com.bottari.convention.plugin.AndroidApplicationComposeConventionPlugin"
        }

        register("android.library") {
            id =
                getPluginIdFromProvider(
                    libs.plugins.bottari.android.library
                        .asProvider(),
                )
            implementationClass = "com.bottari.convention.plugin.AndroidLibraryConventionPlugin"
        }

        register("android.library.compose") {
            id = getPluginIdFromProvider(libs.plugins.bottari.android.library.compose)
            implementationClass =
                "com.bottari.convention.plugin.AndroidLibraryComposeConventionPlugin"
        }

        register("android.hilt") {
            id = getPluginIdFromProvider(libs.plugins.bottari.android.hilt)
            implementationClass = "com.bottari.convention.plugin.AndroidHiltConventionPlugin"
        }

        register("android.feature") {
            id = getPluginIdFromProvider(libs.plugins.bottari.android.feature)
            implementationClass = "com.bottari.convention.plugin.AndroidFeatureConventPlugin"
        }

        register("jvm.kotlin") {
            id = getPluginIdFromProvider(libs.plugins.bottari.jvm.kotlin)
            implementationClass = "com.bottari.convention.plugin.JvmKotlinConventionPlugin"
        }
    }
}
