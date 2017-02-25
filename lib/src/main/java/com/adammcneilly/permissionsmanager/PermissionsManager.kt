package com.adammcneilly.permissionsmanager

/**
 * Utility methods used for handling the different cases a Permission Request would be handled.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
interface PermissionsManager {
    fun onPermissionGranted(permission: String, requestCode: Int)
    fun onPermissionDenied(permission: String, requestCode: Int)
    fun onPermissionBlocked(permission: String, requestCode: Int)
}