package com.bottari.presentation.view.edit.team.item.main.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.bottari.presentation.R
import com.bottari.presentation.model.BottariItemTypeUiModel
import com.bottari.presentation.view.edit.team.item.assigned.TeamAssignedItemEditFragment
import com.bottari.presentation.view.edit.team.item.personal.TeamPersonalItemEditFragment
import com.bottari.presentation.view.edit.team.item.shared.TeamSharedItemEditFragment

class TeamItemEditFragmentAdapter(
    fragment: Fragment,
    private val bottariId: Long,
) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = TAB_INFO.size

    override fun createFragment(position: Int): Fragment {
        val tabInfo = TAB_INFO.getOrNull(position)
        return tabInfo?.fragmentProvider?.invoke(bottariId) ?: error(ERROR_UNKNOWN_FRAGMENT)
    }

    companion object {
        private const val ERROR_UNKNOWN_FRAGMENT = "[ERROR] 알 수 없는 프래그먼트입니다"

        private const val POSITION_SHARED = 0
        private const val POSITION_ASSIGNED = 1
        private const val POSITION_PERSONAL = 2

        private val TAB_INFO =
            listOf(
                TabInfo(
                    titleRes = R.string.bottari_item_type_shared_text,
                    fragmentProvider = { id -> TeamSharedItemEditFragment.newInstance(id) },
                ),
                TabInfo(
                    titleRes = R.string.bottari_item_type_assigned_text,
                    fragmentProvider = { id -> TeamAssignedItemEditFragment.newInstance(id) },
                ),
                TabInfo(
                    titleRes = R.string.bottari_item_type_personal_text,
                    fragmentProvider = { id -> TeamPersonalItemEditFragment.newInstance(id) },
                ),
            )

        fun tabTitleFromPosition(
            position: Int,
            context: Context,
        ): String =
            context.getString(
                TAB_INFO.getOrNull(position)?.titleRes ?: error(ERROR_UNKNOWN_FRAGMENT),
            )

        fun positionFromType(type: BottariItemTypeUiModel): Int =
            when (type) {
                BottariItemTypeUiModel.SHARED -> POSITION_SHARED
                is BottariItemTypeUiModel.ASSIGNED -> POSITION_ASSIGNED
                BottariItemTypeUiModel.PERSONAL -> POSITION_PERSONAL
            }

        fun typeFromPosition(position: Int): BottariItemTypeUiModel =
            when (position) {
                POSITION_SHARED -> BottariItemTypeUiModel.SHARED
                POSITION_ASSIGNED -> BottariItemTypeUiModel.ASSIGNED(emptyList())
                POSITION_PERSONAL -> BottariItemTypeUiModel.PERSONAL
                else -> error(ERROR_UNKNOWN_FRAGMENT)
            }
    }
}
