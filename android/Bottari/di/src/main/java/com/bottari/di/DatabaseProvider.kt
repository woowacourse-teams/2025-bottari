package com.bottari.di

import com.bottari.data.local.bottari.BottariDatabase
import com.bottari.data.local.tooltip.TooltipDatabase

object DatabaseProvider {
    val bottariDatabase: BottariDatabase by lazy {
        BottariDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }

    val tooltipDatabase: TooltipDatabase by lazy {
        TooltipDatabase.getDatabase(ApplicationContextProvider.applicationContext)
    }
}
