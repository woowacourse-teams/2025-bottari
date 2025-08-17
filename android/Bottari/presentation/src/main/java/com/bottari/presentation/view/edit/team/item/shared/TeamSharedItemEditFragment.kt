package com.bottari.presentation.view.edit.team.item.shared

import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment

class TeamSharedItemEditFragment : Fragment() {
    private val bottariId: Long by lazy { requireArguments().getLong(ARG_BOTTARI_ID) }

    companion object {
        private const val ARG_BOTTARI_ID = "ARG_BOTTARI_ID"

        fun newInstance(bottariId: Long): TeamSharedItemEditFragment =
            TeamSharedItemEditFragment().apply {
                arguments = bundleOf(ARG_BOTTARI_ID to bottariId)
            }
    }
}
