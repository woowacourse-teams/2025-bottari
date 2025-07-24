package com.bottari.data.extension

import retrofit2.Response

fun Response<*>.extractIdFromHeader(
    idPrefix: String,
    headerName: String = "Location",
): Long? {
    val headerValue = this.headers()[headerName]
    return headerValue
        ?.substringAfter(idPrefix)
        ?.takeWhile { it.isDigit() }
        ?.toLongOrNull()
}
