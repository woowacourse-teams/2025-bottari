package com.bottari.presentation.util

import android.text.TextPaint
import android.widget.TextView

object ItemSummaryUtil {
    private const val SEPARATOR = ", "
    private const val EMPTY_STRING = ""

    fun TextView.setSummaryItems(
        itemNames: List<String>,
        suffixFormat: String,
    ) {
        if (itemNames.isEmpty()) {
            text = EMPTY_STRING
            return
        }

        val maxWidth = width - paddingLeft - paddingRight
        val fittingItems = itemNames.getFittingItems(paint, maxWidth, suffixFormat)
        val remaining = itemNames.size - fittingItems.size
        val suffix = formatSuffix(suffixFormat, remaining)

        val summary = fittingItems.joinToString(SEPARATOR) + suffix
        text = summary
    }

    private fun List<String>.getFittingItems(
        paint: TextPaint,
        maxWidth: Int,
        suffixFormat: String,
    ): List<String> {
        val fittingItems = mutableListOf<String>()
        val prefix = StringBuilder()

        forEachIndexed { index, name ->
            val preview =
                buildPreviewText(prefix, fittingItems, name, size - (index + 1), suffixFormat)

            if (paint.measureText(preview) > maxWidth) return fittingItems

            if (fittingItems.isNotEmpty()) prefix.append(SEPARATOR)
            prefix.append(name)
            fittingItems += name
        }

        return fittingItems
    }

    private fun buildPreviewText(
        prefix: StringBuilder,
        items: List<String>,
        nextItem: String,
        remaining: Int,
        suffixFormat: String,
    ): String =
        buildString {
            append(prefix)
            if (items.isNotEmpty()) append(SEPARATOR)
            append(nextItem)
            append(formatSuffix(suffixFormat, remaining))
        }

    private fun formatSuffix(
        suffixFormat: String,
        remaining: Int,
    ): String = if (remaining > 0) suffixFormat.format(remaining) else EMPTY_STRING
}
