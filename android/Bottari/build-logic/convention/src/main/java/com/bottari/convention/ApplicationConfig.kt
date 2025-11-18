package com.bottari.convention

internal object ApplicationConfig {
    const val COMPILE_VERSION = 36
    const val TARGET_VERSION = 36
    const val MIN_VERSION = 28

    val JavaVersion = org.gradle.api.JavaVersion.VERSION_21
    const val JAVA_VERSION_AS_INT = 21
}
