package com.bottari.logger.model

data class CallerInfo(
    val className: String,
    val methodName: String,
    val lineNumber: Int,
) {
    fun display(): String = "$className.kt:$lineNumber"

    companion object {
        private const val UNKNOWN = "Unknown"
        private val EXCLUDED_PREFIXES =
            listOf(
                "com.bottari.logger",
                "com.bottari.logger.BottariTree",
                "java.lang",
                "timber.log",
            )

        fun extract(): CallerInfo {
            val stackTrace = Throwable().stackTrace
            val caller =
                stackTrace.firstOrNull { element ->
                    val className = element.className
                    EXCLUDED_PREFIXES.none { prefix -> className.startsWith(prefix) }
                }

            return caller?.let {
                CallerInfo(
                    className = it.className.substringAfterLast('.'),
                    methodName = it.methodName,
                    lineNumber = it.lineNumber,
                )
            } ?: CallerInfo(UNKNOWN, UNKNOWN, -1)
        }
    }
}
