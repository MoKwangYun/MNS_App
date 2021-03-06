package com.example.kakaomaptest_1.Activity

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.kakaomaptest_1.R
import com.google.android.material.navigation.NavigationView
import java.security.MessageDigest
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private val requiredPermissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.USE_FULL_SCREEN_INTENT,
        Manifest.permission.FOREGROUND_SERVICE,
        Manifest.permission.RECEIVE_BOOT_COMPLETED
    )
    private val ACCESS_FINE_LOCATION = 1000
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var nav : NavigationView
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var currentUserID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        getAppKeyHash()
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
//        supportActionBar!!.setDisplayShowTitleEnabled(false)
        nav = findViewById(R.id.navmenu)
        drawerLayout = findViewById(R.id.drawer)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        permissionCheck()

        supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment


    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)

        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    fun setUserId(id: String) {
        this.currentUserID = id
    }

    fun getUserId(): String {
        return this.currentUserID
    }

    private fun getDeniedPermissions():ArrayList<String>{
        var deniedPermissions = ArrayList<String>()

        for(p in requiredPermissions){
            if(ContextCompat.checkSelfPermission(applicationContext, p) == PackageManager.PERMISSION_DENIED){
                deniedPermissions.add(p)
            }
        }

        return deniedPermissions
    }

    private fun requestPermissions(permissions: ArrayList<String>){
        if(permissions.isNotEmpty()){
            val array = arrayOfNulls<String>(permissions.size)
            ActivityCompat.requestPermissions(this, permissions.toArray(array), 1)
        }
    }

    private fun getAppKeyHash() {
        try {
            val info =
                packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                var md: MessageDigest
                md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                val something = String(Base64.encode(md.digest(), 0))
                Log.e("Hash key", something)
            }
        } catch (e: Exception) {
            Log.e("name not found", e.toString())
        }
    }

    private fun permissionCheck(): Boolean {
        val preference = getPreferences(MODE_PRIVATE)
        val isFirstCheck = preference.getBoolean("isFirstPermissionCheck", true)

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("????????? ???????????? ????????? ?????? ????????? ??? ????????????. ????????? ??????????????????")
                builder.setPositiveButton("??????") { dialog, which ->
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                }
                builder.setNegativeButton("??????") { dialog, which ->
                    Toast.makeText(applicationContext, "?????? ??? ???????????????.", Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        finish()
                        exitProcess(0)
                    }, 1500)
                }
                builder.show()
            } else {
                if(isFirstCheck) {
                    preference.edit().putBoolean("isFirstPermissionCheck", false).apply()
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), ACCESS_FINE_LOCATION)
                } else {
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("?????? ????????? ?????????????????? ????????? ??????????????????")
                    builder.setPositiveButton("???????????? ??????") { dialog, which ->
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:com.example.kakaomaptest_1"))
                        startActivity(intent)
                    }
                    builder.setNegativeButton("??????") { dialog, which ->

                    }
                    builder.show()
                }
            }
        } else {
            return true
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == ACCESS_FINE_LOCATION) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "?????? ????????? ?????????????????????", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "?????? ????????? ?????????????????????", Toast.LENGTH_LONG).show()
                permissionCheck()
            }
        }
    }
}
