package io.github.asephermann.plugins.mocklocationchecker

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import org.json.JSONArray
import java.util.Locale


const val TAG: String = "MockLocationChecker"

class MockLocationChecker {

    private lateinit var mFusedLocationClient: FusedLocationProviderClient
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val hasPermission =
                activity.checkSelfPermission(Manifest.permission.QUERY_ALL_PACKAGES) == PackageManager.PERMISSION_GRANTED
            if (!hasPermission) {
                activity.requestPermissions(arrayOf(Manifest.permission.QUERY_ALL_PACKAGES), 1)
            } else {
                val pm = activity.packageManager
                val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
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
                            && packageInfo.packageName != activity.packageName
                            && !whiteList.contains(packageInfo.packageName)
                        ) {
                            count++
                            indicated.put(packageInfo.packageName)
                        }
                    } catch (e: PackageManager.NameNotFoundException) {
                        Log.e(TAG, "Got exception " + e.message)
                    }
                }
            }
        } else {
            val pm = activity.packageManager
            val packages = pm.getInstalledPackages(PackageManager.GET_META_DATA)
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
                        && packageInfo.packageName != activity.packageName
                        && !whiteList.contains(packageInfo.packageName)
                    ) {
                        count++
                        indicated.put(packageInfo.packageName)
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(TAG, "Got exception " + e.message)
                }
            }
        }

        return count > 0
    }

    @SuppressLint("ObsoleteSdkInt")
    fun isLocationFromMockProvider(activity: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 18) {
            if (isLocationEnabled(activity)) {
                if (ActivityCompat.checkSelfPermission(
                        activity.applicationContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        activity.applicationContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // Open app settings to allow the user to grant permissions
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", activity.packageName, null)
                    intent.data = uri
                    activity.startActivity(intent)
                    return false
                }
                val location: Location? =
                    mFusedLocationClient.lastLocation.result
                return location?.isFromMockProvider ?: false
            } else {
                Toast.makeText(
                    activity.applicationContext,
                    "Please turn on location",
                    Toast.LENGTH_LONG
                ).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivity(intent)
                return false
            }
        } else {
            return Settings.Secure.getString(
                activity.applicationContext.contentResolver,
                Settings.Secure.ALLOW_MOCK_LOCATION
            ) != "0"
        }
    }

    private fun isLocationEnabled(activity: Activity): Boolean {
        val locationManager: LocationManager =
            activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
}

data class CheckMockResult(
    var isMock: Boolean,
    var messages: String,
    var indicated: JSONArray
)