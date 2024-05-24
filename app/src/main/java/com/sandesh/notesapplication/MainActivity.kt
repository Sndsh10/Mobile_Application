package com.sandesh.notesapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.navigation.ui.*
//import androidx.navigation.ui.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

//class MainActivity : ComponentActivity() {
class MainActivity : AppCompatActivity() {
    private var appBarConfiguration: AppBarConfiguration? = null
    private var drawerLayout: DrawerLayout? = null
    private var toolbar: Toolbar? = null
    private var navController: NavController? = null
    private var mainLayout: View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initiateToolbarCapabilities()
    }

    fun initiateToolbarCapabilities(){
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) //set the toolbar

        drawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.displayNotesFragment
            ), drawerLayout
        )

        setupActionBarWithNavController(navController!!, appBarConfiguration!!) //the most important part
        navView.setupWithNavController(navController!!)
        navView.setNavigationItemSelectedListener(object:
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId) {
                    R.id.listNotesFragmentMenuItem -> {
                        navController!!.navigate(R.id.displayNotesFragment)
                        drawerLayout!!.close()
                    }
                    R.id.logoutMenuItem -> {
                        FirebaseAuth.getInstance().signOut()
                        openAfterLogout()
                        //Log.d("loglog","logout clicked")
                    }
                }
                return false
            }
        });
    }
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        return true
//    }
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
//    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration!!) || super.onSupportNavigateUp()
    }
    fun openAfterLogout() {
        val myIntent = Intent(this@MainActivity, BeforeLoginActivity::class.java)
        this@MainActivity.startActivity(myIntent)
        finish()
        overridePendingTransition(0, 0)
    }

}