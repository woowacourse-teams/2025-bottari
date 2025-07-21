package com.bottari.presentation.view.home.bottari.create

import androidx.lifecycle.ViewModel

class BottariCreateViewModel : ViewModel() {
    var bottariId: Int = 0

    fun createBottari(name: String) {
        bottariId = 1
    }
}
