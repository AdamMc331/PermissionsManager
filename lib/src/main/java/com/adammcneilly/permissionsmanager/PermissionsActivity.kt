package com.adammcneilly.permissionsmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity

/**
 * Activity that handles runtime permissions.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
open class PermissionsActivity: AppCompatActivity() {
    var permissionsManager: PermissionsManager? = null

    /**
     * Determines if the use has granted a certain permission to the application.
     *
     * @param permission The permission string that we are checking against.
     * @return True if the user has granted this permission, false otherwise.
     */
    open fun hasPermission(permission: String): Boolean {
        return ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Determines if the user has denied a permission to the application.
     *
     * @param permission The permission string that we are checking against.
     * @return True if the user has denied this permission (hit deny but not never ask again), false otherwise.
     */
    open fun permissionDenied(permission: String): Boolean {
        // If shouldShow... returns true, it means we were denied but not blocked.
        return (!hasPermission(permission) && ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
    }

    /**
     * Determines if the user has blocked a permission to the application.
     *
     * NOTE: This may also return true for a permission that has never been asked for, so it should
     * be used within the proper context. For that reason I've decided to comment this out but left
     * for education's sake.
     *
     * @param permission The permission string that we are checking against.
     * @return True if the user has blocked this permission (hit deny and never ask again), false otherwise.
     */
//    open fun permissionBlocked(permission: String): Boolean {
//        // User denied and hit never ask again
//        return (!hasPermission(permission) && !ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
//    }

    /**
     * Requests a number of permissions from the system.
     *
     * @param requestCode The request code used to handle the response for this request.
     * @param permissions The strings representing the permissions being requested.
     */
    open fun requestPermissions(requestCode: Int, vararg permissions: String) {
        ActivityCompat.requestPermissions(this, permissions, requestCode)
    }

    /**
     * Opens the settings page for this application.
     * Note: This same method exists in {@link PermissionsActivity}, but it was copied rather than call `getActivity`
     * because we cannot guarantee the user also uses PermissionsActivity.
     *
     * @param requestCode An integer request code for this intent call. The reason this is not set by default
     * is because we did not want to risk conflicting with any other request codes the user may have.
     */
    open fun openSettings(requestCode: Int) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, requestCode)
    }

    /**
     * Handles the callback from the system upon the result of a permission request. Once the result
     * is determined, we will call the appropriate method in the {@link PermissionsManager}.
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        permissions.forEachIndexed { index, permission ->
            if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                permissionsManager?.onPermissionGranted(permission, requestCode)
            } else {
                // If shouldShow... returns true, it means we were denied but not blocked.
                if (permissionDenied(permission)) {
                    permissionsManager?.onPermissionDenied(permission, requestCode)
                } else {
                    // User denied and hit never ask again
                    permissionsManager?.onPermissionBlocked(permission, requestCode)
                }
            }
        }
    }
}