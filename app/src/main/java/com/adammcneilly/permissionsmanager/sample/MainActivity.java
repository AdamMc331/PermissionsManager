package com.adammcneilly.permissionsmanager.sample;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.adammcneilly.permissionsmanager.PermissionsActivity;
import com.adammcneilly.permissionsmanager.PermissionsManager;

public class MainActivity extends PermissionsActivity implements PermissionsManager, View.OnClickListener {
    /**
     * Key for the permissions shared prefs.
     */
    private static final String PERMISSIONS = "permissions";

    /**
     * Request code for camera permission.
     */
    private static final int CAMERA_PERMISSION = 0;

    /**
     * Helper variable so we don't have to type the full name each time.
     */
    private static final String CAMERA_STRING = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setPermissionsManager(this);

        Button button = (Button) findViewById(R.id.permission_button);
        button.setOnClickListener(this);
    }

    private void permissionsCheck(String permission) {
        if (!hasPermission(permission)) {
            // Check to see if it's blocked
            boolean blocked = getSharedPreferences(PERMISSIONS, 0).getBoolean(permission, false);

            if (blocked) {
                showBlockedDialog();
            } else if (permissionDenied(permission)) {
                showExplanationDialog(permission);
            } else {
                requestPermissions(CAMERA_PERMISSION, permission);
            }
        } else {
            Toast.makeText(this, "We have permission: " + permission, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.permission_button:
                permissionsCheck(CAMERA_STRING);
                break;
        }
    }

    private void showExplanationDialog(final String permission) {
        new AlertDialog.Builder(this)
                .setTitle("Permission Denied")
                .setMessage("This dialog explains why we need the permission.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermissions(CAMERA_PERMISSION, permission);
                    }
                })
                .create()
                .show();
    }

    private void showBlockedDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Blocked.")
                .setMessage("This dialog explains that the permission is blocked and sends user to settings page.")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openSettings(CAMERA_PERMISSION);
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onPermissionGranted(@NonNull String permission, int requestCode) {
        Toast.makeText(this, "Granted permission: " + permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionDenied(@NonNull String permission, int requestCode) {
        Toast.makeText(this, "Denied permission: " + permission, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionBlocked(@NonNull String permission, int requestCode) {
        SharedPreferences prefs = getSharedPreferences(PERMISSIONS, 0);
        prefs.edit().putBoolean(permission, true).apply();

        Toast.makeText(this, "Blocked permission: " + permission, Toast.LENGTH_SHORT).show();
    }
}
