package com.bottari.core.ui.extension

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.Velocity
import kotlin.math.abs

/**
 * 자식 컴포저블이 **가로 스크롤**을 시작하면 부모의 스크롤 동작을 차단하는
 * [NestedScrollConnection]을 생성하고 기억합니다.
 *
 * 주로 `LazyColumn` 안에 `LazyRow`와 같은 **가로 스크롤 자식**이 포함된 경우에 사용합니다.
 * 이 함수를 적용하면, 사용자가 가로로 스와이프할 때 부모(`LazyColumn`)가 스크롤을 가로채지 않고
 * 자식(`LazyRow`)만 해당 제스처를 처리하도록 동작합니다.
 *
 * ### 동작 원리
 * - 사용자가 **가로 방향**으로 스크롤할 경우
 *   (`abs(available.x) > abs(available.y)`) → 남은 가로 오프셋을 소비하여
 *   부모 스크롤이 반응하지 않도록 막습니다.
 * - **세로 스크롤**은 그대로 부모에게 전달되어 기존처럼 동작합니다.
 * - **플링(fling)** 시에도 가로 속도만 차단하고, 세로 방향 속도는 부모로 전달됩니다.
 *
 * ### 사용 예시
 * ```kotlin
 * LazyColumn(
 *     modifier = Modifier.nestedScroll(rememberBlockParentAfterChild())
 * ) {
 *     item {
 *         LazyRow {
 *             items(10) { index ->
 *                 Card(
 *                     modifier = Modifier
 *                         .padding(8.dp)
 *                         .size(120.dp)
 *                 ) {
 *                     Text("아이템 $index")
 *                 }
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @return 자식이 가로 스크롤을 시작하면 부모의 스크롤을 차단하는 [NestedScrollConnection]
 */

@Composable
fun rememberBlockParentAfterChild(): NestedScrollConnection =
    remember {
        object : NestedScrollConnection {
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource,
            ): Offset {
                if (source == NestedScrollSource.UserInput && abs(available.x) > abs(available.y)) {
                    return Offset(available.x, 0f)
                }
                return Offset.Zero
            }

            override suspend fun onPostFling(
                consumed: Velocity,
                available: Velocity,
            ): Velocity = Velocity(available.x, 0f)
        }
    }
