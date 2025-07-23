package com.bottari.presentation.util.permission

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
    fun getRequiredPermissions(): Array<String> {
        val permissions = mutableListOf<String>()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions += android.Manifest.permission.POST_NOTIFICATIONS
        }
        permissions += android.Manifest.permission.ACCESS_FINE_LOCATION
        permissions += android.Manifest.permission.ACCESS_COARSE_LOCATION
        return permissions.toTypedArray()
    }

    fun hasAllRuntimePermissions(context: Context): Boolean =
        getRequiredPermissions().all {
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
            val intent =
                Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = "package:${context.packageName}".toUri()
                }
            context.startActivity(intent)
        }
    }

    fun openAppSettings(context: Context) {
        val intent =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = "package:${context.packageName}".toUri()
            }
        context.startActivity(intent)
    }

    fun isPermanentlyDenied(fragment: Fragment): Boolean =
        getRequiredPermissions().any { permission ->
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                permission,
            ) != PackageManager.PERMISSION_GRANTED &&
                !fragment.shouldShowRequestPermissionRationale(permission)
        }
}
