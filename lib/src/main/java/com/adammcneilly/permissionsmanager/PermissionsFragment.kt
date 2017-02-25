package com.adammcneilly.permissionsmanager

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat

/**
 * Fragment that handles runtime permissions.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
open class PermissionsFragment : Fragment() {
    var permissionsManager: PermissionsManager? = null

    /**
     * Determines if the use has granted a certain permission to the application.
     *
     * @param permission The permission string that we are checking against.
     * @return True if the user has granted this permission, false otherwise.
     */
    open fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
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
        val uri = Uri.fromParts("package", activity.packageName, null)
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
                if (shouldShowRequestPermissionRationale(permission)) {
                    permissionsManager?.onPermissionDenied(permission, requestCode)
                } else {
                    // User denied and hit never ask again
                    permissionsManager?.onPermissionBlocked(permission, requestCode)
                }
            }
        }
    }
}