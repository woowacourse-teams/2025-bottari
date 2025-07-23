package com.bottari.domain.util

class NicknameGenerator {
    private val adjectives =
        listOf(
            "꼼꼼한",
            "게으른",
            "바쁜",
            "무계획",
            "잠결의",
            "허둥지둥",
            "기억못함",
            "준비왕",
            "철두철미",
            "똑부러진",
        )

    private val nouns =
        listOf(
            "체커",
            "잊꾸",
            "짐장인",
            "출근러",
            "여행러",
            "물건러",
            "챙김러",
            "잊꾸러기",
            "보따리꾼",
            "가방요정",
        )

    fun generate(): String {
        val adj = adjectives.random()
        val noun = nouns.random()
        return "$adj $noun"
    }
}
