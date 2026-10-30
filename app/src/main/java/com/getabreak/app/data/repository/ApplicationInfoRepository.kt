package com.getabreak.app.data.repository

import android.content.pm.PackageManager
import com.getabreak.app.DetoxDroidApplication
import com.getabreak.app.util.getAppCategoryTitle
import com.getabreak.app.util.isSystemApp

/**
 * Holds the list of all installed apps and the set of all app categories.
 */
data class ApplicationInfoData(
    val packageName: String,
    val appName: String,
    val appCategory: String,
    val isSystemApp: Boolean,
    /** The OS-assigned [android.content.pm.ApplicationInfo.category] (CATEGORY_UNDEFINED if none). */
    val osCategory: Int,
    val hasLaunchIntent: Boolean
)

/**
 * Repository for app information.
 */
object ApplicationInfoRepository {
    /**
     * Returns a list of all installed apps and a set of all app categories.
     */
    fun getInstalledApps(): List<ApplicationInfoData> {
        val context = DetoxDroidApplication.appContext
        val packageManager = DetoxDroidApplication.appContext.packageManager
        val installedApps =
            packageManager.getInstalledApplications(PackageManager.MATCH_UNINSTALLED_PACKAGES)
                .map { appInfo ->
                    ApplicationInfoData(
                        packageName = appInfo.packageName,
                        appName = appInfo.loadLabel(packageManager).toString(),
                        appCategory = appInfo.getAppCategoryTitle(context),
                        isSystemApp = appInfo.isSystemApp(),
                        osCategory = appInfo.category,
                        hasLaunchIntent = packageManager.getLaunchIntentForPackage(appInfo.packageName) != null
                    )
                }
        return installedApps
    }

    /**
     * How many of [packageNames] the package manager still knows about. A feature's app selection
     * survives the apps it names, so it can hold packages that are long gone; those are not apps
     * the feature acts on and must not be counted as such. Matches what [getInstalledApps] returns,
     * so a selection summary and the selection list itself always arrive at the same number.
     */
    fun countInstalled(packageNames: Set<String>): Int {
        val packageManager = DetoxDroidApplication.appContext.packageManager
        return packageNames.count { packageName ->
            runCatching {
                packageManager.getApplicationInfo(
                    packageName, PackageManager.MATCH_UNINSTALLED_PACKAGES
                )
            }.isSuccess
        }
    }
}