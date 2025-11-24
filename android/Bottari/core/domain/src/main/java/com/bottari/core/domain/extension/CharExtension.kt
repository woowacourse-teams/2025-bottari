package com.bottari.core.domain.extension

fun Char.isEnglishLetter(): Boolean {
    if (this in 'a'..'z') return true
    if (this in 'A'..'Z') return true
    return false
}

/** 완성형 한글(가-힣)인지 */
fun Char.isHangulSyllable(): Boolean = this in '\uAC00'..'\uD7A3'

fun Char.isNotHangulSyllable(): Boolean = !isHangulSyllable()

/** 한글 자모(모음/자음) 여부 */
fun Char.isHangulJamoAny(): Boolean {
    if (this in '\u3130'..'\u318F') return true
    if (this in '\u1100'..'\u11FF') return true
    if (this in '\uA960'..'\uA97F') return true
    if (this in '\uD7B0'..'\uD7FF') return true
    return false
}
