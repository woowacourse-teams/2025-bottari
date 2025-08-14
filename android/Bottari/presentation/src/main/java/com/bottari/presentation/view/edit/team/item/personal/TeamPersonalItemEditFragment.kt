package com.bottari.presentation.view.edit.team.item.personal

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class TeamPersonalItemEditFragment : Fragment() {
    private val bottariId: Long by lazy { requireArguments().getLong(ARG_BOTTARI_ID) }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamPersonalItemEditFragment =
            TeamPersonalItemEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
