package com.adammcneilly.permissionsmanager

/**
 * Utility methods used for handling the different cases a Permission Request would be handled.
 *
 * Created by adam.mcneilly on 2/25/17.
 */
interface PermissionsManager {
    /**
     * A callback method when the user grants the app permission for a feature.
     *
     * @param permission The permission the user is giving access to.
     * @param requestCode The request code sent with this permission request.
     */
    fun onPermissionGranted(permission: String, requestCode: Int)

    /**
     * A callback method when the user denies the app permission for a feature.
     *
     * @param permission The permission the user is denying access to.
     * @param requestCode The request code sent with this permission request.
     */
    fun onPermissionDenied(permission: String, requestCode: Int)

    /**
     * A callback method when the user denies the app permission for a feature and checks 'never ask again.'
     *
     * @param permission The permission the user is blocking access to.
     * @param requestCode The request code sent with this permission request.
     */
    fun onPermissionBlocked(permission: String, requestCode: Int)
}