package com.bottari.convention.external

import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.konan.properties.Properties

/**
 * local.properties 캐싱 저장소
 * - 빌드 전체에서 단 1번만 읽고 재사용
 */
private class LocalPropertiesHolder {
    private var project: Project? = null

    val properties: Properties by lazy {
        val proj = project ?: error("[ERROR] 프로젝트가 초기화되지 않았습니다.")
        gradleLocalProperties(proj.rootDir, proj.providers)
    }

    fun initialize(project: Project) {
        if (this.project == null) this.project = project
    }
}

/**
 * gradleLocalProperties는 Project 인스턴스가 필요하므로
 * 최초 호출 시 한 번만 읽어오기 위한 lazy holder
 */
private val holder = LocalPropertiesHolder()

/**
 * local.properties 읽기 (캐싱 처리)
 */
private fun Project.getLocalProperties(): Properties {
    holder.initialize(this)
    return holder.properties
}

/**
 * key가 없으면 즉시 error() 발생
 */
fun Project.getPropertyOrThrow(key: String): String {
    val props = getLocalProperties()
    return props.getProperty(key)?.trim()
        ?: error("$key is missing in local.properties")
}
