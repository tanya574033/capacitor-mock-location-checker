package io.github.asephermann.plugins.mocklocationchecker

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.provider.Settings
import android.util.Log
import org.json.JSONArray

const val TAG: String = "MockLocationChecker"

class MockLocationChecker {

    private var indicated = JSONArray()

    fun checkMock(activity: Activity, whiteList: List<String>): CheckMockResult {

        val listData: ArrayList<String>
        var msg = ""

        // returns true if mock location enabled, false if not enabled.
        val isMock: Boolean = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            if (Settings.Secure.getString(
                    activity.contentResolver,
                    Settings.Secure.ALLOW_MOCK_LOCATION
                ) != "0"
            ) {
                msg = "Please turn off Allow Mock locations option in developer options."
                true
            } else {
                false
            }
        } else {
            listData = ArrayList()
            for (i in whiteList.indices) {
                listData.add(whiteList[i])
            }

            if (checkForAllowMockLocationsApps(activity, listData)) {
                msg =
                    "We've detected that there are other apps in the device, which are using Mock Location access (Location Spoofing Apps). Please uninstall first."
                true
            } else {
                false
            }
        }
        Log.i(TAG, "isMock: $isMock")
        Log.i(TAG, "msg: $msg")
        Log.i(TAG, "indicated: $indicated")
        return CheckMockResult(isMock, msg, indicated)
    }

    private fun checkForAllowMockLocationsApps(
        activity: Activity,
        whiteList: ArrayList<String>
    ): Boolean {
        var count = 0
        val pm = activity.packageManager
        val packages = pm.getInstalledApplications(PackageManager.GET_META_DATA)
        indicated = JSONArray()
        for (applicationInfo in packages) {
            try {
                val packageInfo = pm.getPackageInfo(
                    applicationInfo.packageName,
                    PackageManager.GET_PERMISSIONS
                )

                // Get Permissions
                val requestedPermissions = packageInfo.requestedPermissions
                if (requestedPermissions != null && requestedPermissions.contains("android.permission.ACCESS_MOCK_LOCATION")
                    && applicationInfo.packageName != activity.packageName
                    && !whiteList.contains(applicationInfo.packageName)
                ) {
                    count++
                    indicated.put(applicationInfo.packageName)
                }
            } catch (e: PackageManager.NameNotFoundException) {
                Log.e(TAG, "Got exception " + e.message)
            }
        }
        return count > 0
    }

    fun isLocationFromMockProvider(context: Context, location: Location): Boolean {
        return if (Build.VERSION.SDK_INT >= 18) {
            location.isFromMockProvider
        } else {
            Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION
            ) != "0"
        }
    }
}

data class CheckMockResult(
    var isMock: Boolean,
    var messages: String,
    var indicated: JSONArray
)