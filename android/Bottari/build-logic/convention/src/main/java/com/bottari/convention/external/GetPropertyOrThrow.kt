package com.bottari.convention.external

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.jetbrains.kotlin.konan.properties.Properties

/**
 * local.properties 캐싱 저장소
 * - 빌드 전체에서 단 1번만 읽고 재사용
 */
private val cachedLocalProperties: Properties by lazy {
    Properties().apply {
        putAll(loadedFromRoot ?: emptyMap<String, String>())
    }
}

/**
 * gradleLocalProperties는 Project 인스턴스가 필요하므로
 * 최초 호출 시 한 번만 읽어오기 위한 lazy holder
 */
private var loadedFromRoot: Properties? = null

/**
 * local.properties 읽기 (캐싱 처리)
 */
private fun Project.getLocalProperties(): Properties {
    if (loadedFromRoot == null) {
        loadedFromRoot = gradleLocalProperties(rootDir, providers)
    }
    return cachedLocalProperties
}

/**
 * key가 없으면 즉시 error() 발생
 */
fun Project.getPropertyOrThrow(key: String): String {
    val props = getLocalProperties()
    return props.getProperty(key)?.trim()
        ?: error("$key is missing in local.properties")
}
