package com.example.mpos

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.databinding.ActivityMainBinding
import com.example.mpos.utils.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermission()
    }

    fun getPermission() {
        if (!this.checkCameraPermission()) {
            requestPermission()
        }
    }

    private fun requestPermission(
        manifest: String = Manifest.permission.CAMERA,
        code: Int = CAMERA_INT,
        name: String = "Camera"
    ) =
        EasyPermissions.requestPermissions(
            this,
            "Kindly Give us $name permission,otherwise application may not work Properly.",
            code,
            manifest,
        )


    fun logout() {
        lifecycleScope.launch {
            val dataStore = UserSoredData(application)
            dataStore.logout()
            finish()
        }
    }


    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        perms.forEach {
            if (EasyPermissions.permissionPermanentlyDenied(this, it)) {
                SettingsDialog.Builder(this).build().show()
            } else
                getPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i(TAG, "onPermissionsGranted: $requestCode is given $perms")
    }

}