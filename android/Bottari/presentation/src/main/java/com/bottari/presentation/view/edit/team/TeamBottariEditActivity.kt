package com.bottari.presentation.view.edit.team

import android.os.Bundle
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.bottari.presentation.R
import com.bottari.presentation.common.base.BaseActivity
import com.bottari.presentation.databinding.ActivityTeamBottariEditBinding
import com.bottari.presentation.view.edit.team.management.TeamManagementFragment

class TeamBottariEditActivity :
    BaseActivity<ActivityTeamBottariEditBinding>(
        ActivityTeamBottariEditBinding::inflate,
    ) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        supportFragmentManager.commit {
            replace<TeamManagementFragment>(R.layout.fragment_team_management)
        }
    }
}
