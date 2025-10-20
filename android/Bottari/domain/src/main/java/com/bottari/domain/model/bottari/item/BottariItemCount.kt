package com.bottari.domain.model.bottari.item

data class BottariItemCount(
    val totalQuantity: Int,
    val checkedQuantity: Int,
) {
    init {
        require(totalQuantity >= 0) { "totalQuantity는 음수가 될 수 없습니다" }
        require(checkedQuantity >= 0) { "checkedQuantity는 음수가 될 수 없습니다" }
        require(checkedQuantity <= totalQuantity) { "checkedQuantity 는 totalQuantity를 초과할 수 없습니다" }
    }
}
