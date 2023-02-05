package com.example.mpos

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.mpos.dataStore.UserSoredData
import com.example.mpos.databinding.ActivityMainBinding
import com.example.mpos.utils.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    private lateinit var binding: ActivityMainBinding

    private val isShowMainScr by lazy {
        intent?.getBooleanExtra("Show_main_scr", false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.hide()
        this.changeStatusBarColor()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getPermission()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_graph)

        if (isShowMainScr != null && isShowMainScr == true) {
            graph.setStartDestination(R.id.tableManagementOrCostEstimate)
        } else {
            graph.setStartDestination(R.id.splashScreenFragment)
        }
        val navController = navHostFragment.navController
        navController.setGraph(graph, intent.extras)
    }

    fun getPermission() {
        if (!this.checkCameraPermission()) {
            requestPermission()
        }
        getPermissionForBlueTooth()
    }

    fun getPermissionForBlueTooth() {
        if (!this.checkBlueToothPermission()) {
            requestPermission(Manifest.permission.BLUETOOTH, BLUE_TOOTH, "Bluetooth")
        }
        if (!this.checkBlueAdminPermission()) {
            requestPermission(Manifest.permission.BLUETOOTH_ADMIN, BLUE_ADIMS, "Bluetooth")
        }
        if (!this.checkBlueScanPermission()) {
            requestPermission(Manifest.permission.BLUETOOTH_SCAN, BLUE_SCAN, "Bluetooth")
        }
        if (!this.checkBlueConnectPermission()) {
            requestPermission(Manifest.permission.BLUETOOTH_CONNECT, BLUE_CONNECT, "Bluetooth")
        }
    }


    fun requestPermission(
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