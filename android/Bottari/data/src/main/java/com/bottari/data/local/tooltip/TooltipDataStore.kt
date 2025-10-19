package com.bottari.data.local.tooltip

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bottari.domain.model.tooltip.TooltipType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "tooltip_dismissals")

class TooltipDataStore @Inject constructor(
    private val context: Context,
) {
    private val tooltipDismissalsKey = stringSetPreferencesKey("tooltip_dismissals")

    fun isTooltipDismissed(tooltipType: TooltipType): Flow<Boolean> =
        context.dataStore.data.map { preferences ->
            val dismissedTooltips = preferences[tooltipDismissalsKey] ?: emptySet()
            dismissedTooltips.contains(tooltipType.name)
        }

    suspend fun setTooltipDismissed(tooltipType: TooltipType) {
        context.dataStore.edit { preferences ->
            val dismissedTooltips = preferences[tooltipDismissalsKey] ?: emptySet()
            preferences[tooltipDismissalsKey] = dismissedTooltips + tooltipType.name
        }
    }
}
