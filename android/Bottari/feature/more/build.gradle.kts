import com.bottari.convention.external.getPropertyOrThrow

plugins {
    alias(libs.plugins.bottari.android.feature)
}

android {
    namespace = "com.bottari.feature.more"

    defaultConfig {
        buildConfigField(
            "String",
            "PRIVACY_POLICY_URL",
            "\"${getPropertyOrThrow("PRIVACY_POLICY_URL")}\"",
        )
        buildConfigField(
            "String",
            "USER_FEEDBACK_URL",
            "\"${getPropertyOrThrow("USER_FEEDBACK_URL")}\"",
        )
        buildConfigField(
            "int",
            "APP_VERSION_CODE",
            "${
                System.getenv("VERSION_CODE")?.toIntOrNull() ?: libs.versions.versionCode
                    .get()
                    .toInt()
            }",
        )
    }
}
