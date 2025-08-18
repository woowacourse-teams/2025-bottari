package com.bottari.presentation.view.common.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import com.bottari.presentation.R

@SuppressLint("RtlHardcoded")
class ExpandableFab
    @JvmOverloads
    constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
    ) : FrameLayout(context, attrs, defStyle) {
        enum class Direction(
            val value: Int,
        ) {
            UP(0),
            DOWN(1),
            LEFT(2),
            RIGHT(3),
            ;

            companion object {
                fun fromInt(value: Int) = entries.firstOrNull { it.value == value } ?: DOWN
            }
        }

        private var isExpanded = false
        private var direction: Direction = Direction.DOWN
        private var mainButton: ImageView? = null
        private val subButtons = mutableListOf<View>()
        private var buttonSpacing: Int = 0
        private var rotateOnExpand: Boolean = false

        init {
            context.theme.obtainStyledAttributes(attrs, R.styleable.ExpandableFab, 0, 0).apply {
                try {
                    isExpanded = getBoolean(R.styleable.ExpandableFab_expanded, false)
                    direction = Direction.fromInt(getInt(R.styleable.ExpandableFab_direction, 1))
                    buttonSpacing =
                        getDimensionPixelSize(
                            R.styleable.ExpandableFab_buttonSpacing,
                            dpToPx(DEFAULT_SPACING_DP),
                        )
                    rotateOnExpand = getBoolean(R.styleable.ExpandableFab_rotateOnExpand, false)
                } finally {
                    recycle()
                }
            }
        }

        // View Inflation 완료 후 초기화
        override fun onFinishInflate() {
            super.onFinishInflate()
            if (isEmpty()) return

            mainButton =
                getChildAt(0).also {
                    require(it is AppCompatImageView) { ERROR_MAIN_BUTTON_TYPE }
                } as ImageView

            subButtons.clear()
            for (i in 1 until childCount) subButtons.add(getChildAt(i))

            mainButton?.setOnClickListener { toggle() }

            if (isExpanded) expandImmediate() else collapseImmediate()
        }

        // 모든 자식 뷰 측정
        override fun onMeasure(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int,
        ) {
            measureAllChildren(widthMeasureSpec, heightMeasureSpec)
            val (calculatedWidth, calculatedHeight) = calculateMeasuredSize()
            setMeasuredDimension(
                resolveSize(calculatedWidth, widthMeasureSpec),
                resolveSize(calculatedHeight, heightMeasureSpec),
            )
        }

        // 각 자식 뷰 측정
        private fun measureAllChildren(
            widthMeasureSpec: Int,
            heightMeasureSpec: Int,
        ) {
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0)
            }
        }

        // 측정된 자식 뷰를 기반으로 ExpandableFab 전체 크기 계산
        private fun calculateMeasuredSize(): Pair<Int, Int> {
            var maxWidth = 0
            var maxHeight = 0

            for (i in 0 until childCount) {
                val child = getChildAt(i)
                val lp = child.layoutParams as MarginLayoutParams
                maxWidth = maxOf(maxWidth, child.measuredWidth + lp.leftMargin + lp.rightMargin)
                maxHeight = maxOf(maxHeight, child.measuredHeight + lp.topMargin + lp.bottomMargin)
            }

            mainButton?.let { main ->
                if (subButtons.isNotEmpty()) {
                    val totalOffset =
                        subButtons.size * (buttonSpacing + if (direction.isVertical()) main.measuredHeight else main.measuredWidth)
                    if (direction.isVertical()) {
                        maxHeight += totalOffset + EXTRA_SPACE_PX
                    } else {
                        maxWidth += totalOffset + EXTRA_SPACE_PX
                    }
                }
            }

            return maxWidth to maxHeight
        }

        // 자식 뷰 위치 배치
        override fun onLayout(
            changed: Boolean,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
        ) {
            val parentWidth = right - left
            val parentHeight = bottom - top

            mainButton?.let { main ->
                val (mainLeft, mainTop) = calculateMainButtonPosition(main, parentWidth, parentHeight)
                main.layout(
                    mainLeft,
                    mainTop,
                    mainLeft + main.measuredWidth,
                    mainTop + main.measuredHeight,
                )
                layoutSubButtons(main, mainLeft, mainTop)
            }
        }

        // 메인 버튼 위치 계산
        private fun calculateMainButtonPosition(
            main: View,
            parentWidth: Int,
            parentHeight: Int,
        ): Pair<Int, Int> {
            val params = main.layoutParams as LayoutParams
            val left = direction.horizontalPosition(parentWidth, main.measuredWidth, params.gravity)
            val top = direction.verticalPosition(parentHeight, main.measuredHeight, params.gravity)
            return left to top
        }

        // 서브 버튼 위치 배치
        private fun layoutSubButtons(
            main: View,
            mainLeft: Int,
            mainTop: Int,
        ) {
            val offset =
                if (direction.isVertical()) main.measuredHeight + buttonSpacing else main.measuredWidth + buttonSpacing
            subButtons.forEachIndexed { index, button ->
                val lp = button.layoutParams as LayoutParams
                val (childLeft, childTop) =
                    getSubButtonPosition(mainLeft, mainTop, index + 1, offset, main, button, lp.gravity)
                button.layout(
                    childLeft,
                    childTop,
                    childLeft + button.measuredWidth,
                    childTop + button.measuredHeight,
                )
            }
        }

        // 메인 버튼 토글
        fun toggle() {
            isExpanded = !isExpanded
            if (isExpanded) expand() else collapse()
        }

        // 서브 버튼 확장
        fun expand() = animateButtons(true)

        // 서브 버튼 축소
        fun collapse() = animateButtons(false)

        // 서브 버튼 Animation 처리
        private fun animateButtons(expanding: Boolean) {
            rotateMainButton(expanding)
            isExpanded = expanding

            val buttonsToAnimate = if (expanding) subButtons else subButtons.reversed()
            buttonsToAnimate.forEachIndexed { index, button ->
                val (translationX, translationY, alpha) = getButtonAnimationProperties(expanding)
                button.isVisible = true
                button
                    .animate()
                    .translationX(translationX)
                    .translationY(translationY)
                    .alpha(alpha)
                    .setInterpolator(OvershootInterpolator())
                    .setStartDelay(index * ANIMATION_DELAY_UNIT)
                    .withEndAction { if (!expanding) button.isVisible = false }
                    .start()
            }
        }

        // Animation 속성 계산
        private fun getButtonAnimationProperties(expanding: Boolean): Triple<Float, Float, Float> {
            val (translationX, translationY) = if (expanding) 0f to 0f else getCollapseTranslation()
            val alpha = if (expanding) 1f else 0f
            return Triple(translationX, translationY, alpha)
        }

        // 축소 시 이동 값 계산
        private fun getCollapseTranslation(): Pair<Float, Float> =
            if (direction.isVertical()) 0f to direction.collapseTranslation() else direction.collapseTranslation() to 0f

        // 메인 버튼 회전
        private fun rotateMainButton(expanding: Boolean) {
            if (!rotateOnExpand) return
            mainButton
                ?.animate()
                ?.rotation(if (expanding) ROTATION_EXPANDED else 0f)
                ?.setDuration(ROTATION_DURATION_MS)
                ?.start()
        }

        // 서브 버튼 즉시 축소
        private fun collapseImmediate() = applyToSubButtons(false, 0f, direction.collapseTranslation())

        // 서브 버튼 즉시 확장
        private fun expandImmediate() = applyToSubButtons(true, 1f, 0f)

        // 서브 버튼 속성 적용
        private fun applyToSubButtons(
            visible: Boolean,
            alpha: Float,
            translation: Float,
        ) {
            subButtons.forEach {
                it.isVisible = visible
                it.alpha = alpha
                if (direction.isVertical()) {
                    it.translationY = translation
                } else {
                    it.translationX =
                        translation
                }
            }
        }

        // 각 서브 버튼 위치 계산
        private fun getSubButtonPosition(
            mainLeft: Int,
            mainTop: Int,
            index: Int,
            offset: Int,
            mainButton: View,
            subButton: View,
            gravity: Int,
        ): Pair<Int, Int> {
            val horizontal = calculateHorizontalPosition(mainLeft, mainButton, subButton, gravity)
            val vertical = calculateVerticalPosition(mainTop, mainButton, subButton, gravity)
            return when (direction) {
                Direction.UP -> horizontal to vertical - index * offset
                Direction.DOWN -> horizontal to vertical + index * offset
                Direction.LEFT -> horizontal - index * offset to vertical
                Direction.RIGHT -> horizontal + index * offset to vertical
            }
        }

        // 서브 버튼 가로 위치 계산
        private fun calculateHorizontalPosition(
            mainLeft: Int,
            mainButton: View,
            subButton: View,
            gravity: Int,
        ): Int =
            when (
                Gravity.getAbsoluteGravity(
                    gravity,
                    this@ExpandableFab.layoutDirection,
                ) and Gravity.HORIZONTAL_GRAVITY_MASK
            ) {
                Gravity.LEFT -> mainLeft
                Gravity.CENTER_HORIZONTAL -> mainLeft + (mainButton.measuredWidth - subButton.measuredWidth) / 2
                Gravity.RIGHT -> mainLeft + mainButton.measuredWidth - subButton.measuredWidth
                else -> mainLeft
            }

        // 서브 버튼 세로 위치 계산
        private fun calculateVerticalPosition(
            mainTop: Int,
            mainButton: View,
            subButton: View,
            gravity: Int,
        ): Int =
            when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
                Gravity.TOP -> mainTop
                Gravity.CENTER_VERTICAL -> mainTop + (mainButton.measuredHeight - subButton.measuredHeight) / 2
                Gravity.BOTTOM -> mainTop + mainButton.measuredHeight - subButton.measuredHeight
                else -> mainTop
            }

        private fun dpToPx(dp: Int) =
            TypedValue
                .applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP,
                    dp.toFloat(),
                    resources.displayMetrics,
                ).toInt()

        private fun Direction.isVertical() = this == Direction.UP || this == Direction.DOWN

        // 메인 버튼 가로 위치 계산
        private fun Direction.horizontalPosition(
            parentWidth: Int,
            mainWidth: Int,
            gravity: Int = Gravity.NO_GRAVITY,
        ): Int {
            val abs = Gravity.getAbsoluteGravity(gravity, this@ExpandableFab.layoutDirection)
            return when (abs and Gravity.HORIZONTAL_GRAVITY_MASK) {
                Gravity.LEFT -> return 0
                Gravity.RIGHT -> return parentWidth - mainWidth
                Gravity.CENTER_HORIZONTAL -> (parentWidth - mainWidth) / 2
                else ->
                    when (this) {
                        Direction.LEFT -> parentWidth - mainWidth
                        Direction.RIGHT -> 0
                        else -> (parentWidth - mainWidth) / 2
                    }
            }
        }

        // 메인 버튼 세로 위치 계산
        private fun Direction.verticalPosition(
            parentHeight: Int,
            mainHeight: Int,
            gravity: Int = Gravity.NO_GRAVITY,
        ) = when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
            Gravity.TOP -> 0
            Gravity.BOTTOM -> parentHeight - mainHeight
            Gravity.CENTER_VERTICAL -> (parentHeight - mainHeight) / 2
            else ->
                when (this) {
                    Direction.UP -> parentHeight - mainHeight
                    Direction.DOWN -> 0
                    else -> (parentHeight - mainHeight) / 2
                }
        }

        // 축소 시 이동 거리 계산
        private fun Direction.collapseTranslation() =
            when (this) {
                Direction.UP -> DEFAULT_OFFSET
                Direction.DOWN -> -DEFAULT_OFFSET
                Direction.LEFT -> DEFAULT_OFFSET
                Direction.RIGHT -> -DEFAULT_OFFSET
            }

        companion object {
            private const val DEFAULT_SPACING_DP = 20
            private const val DEFAULT_OFFSET = 50f
            private const val ANIMATION_DELAY_UNIT = 50L
            private const val EXTRA_SPACE_PX = 20
            private const val ROTATION_EXPANDED = 45f
            private const val ROTATION_DURATION_MS = 200L
            private const val ERROR_MAIN_BUTTON_TYPE = "[ERROR] 첫 번째 자식은 AppCompatImageView"
        }
    }
