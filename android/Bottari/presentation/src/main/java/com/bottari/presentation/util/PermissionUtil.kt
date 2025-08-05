package com.bottari.presentation.util

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment

object PermissionUtil {
    val requiredPermissions: Array<String> by lazy {
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }.toTypedArray()
    }

    fun hasAllRuntimePermissions(context: Context): Boolean =
        requiredPermissions.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun hasExactAlarmPermission(context: Context): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = context.getSystemService(AlarmManager::class.java)
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }

    fun requestExactAlarmPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            navigateToSettings(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, context)
        }
    }

    fun openAppSettings(context: Context) {
        navigateToSettings(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, context)
    }

    fun isPermanentlyDenied(fragment: Fragment): Boolean =
        requiredPermissions.any { permission ->
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                permission,
            ) != PackageManager.PERMISSION_GRANTED &&
                !fragment.shouldShowRequestPermissionRationale(permission)
        }

    private fun navigateToSettings(
        settingFlag: String,
        context: Context,
    ) {
        val intent =
            Intent(settingFlag).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                data = "package:${context.packageName}".toUri()
            }
        context.startActivity(intent)
    }
}
