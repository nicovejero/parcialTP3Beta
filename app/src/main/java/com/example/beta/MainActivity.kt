package com.example.beta

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.beta.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var bottomNavView: BottomNavigationView
    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
            setupToolbar()
            setupNavigation()
    }


    private fun setupToolbar() {
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        // Then initialize navController with the NavController from the NavHostFragment
        navController = navHostFragment.navController

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        bottomNavView = binding.bottomNavView

        val toggle = ActionBarDrawerToggle(

            this, drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close

        )

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

//        val appBarConfiguration = AppBarConfiguration.Builder(
//            R.id.homeFragment, // Agrega aquí los IDs de fragmentos principales
//            R.id.nav_drawer_perfil, R.id.nav_drawer_configuracion
//        )
        appBarConfiguration = AppBarConfiguration.Builder(navController.graph)

            .setOpenableLayout(drawerLayout)
            .build()

        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(navView, navController)
        NavigationUI.setupWithNavController(bottomNavView, navController)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_drawer_perfil -> {

                    // Navegar al fragmento Perfil en el Drawer Menu
//                    navController.navigate(R.id.nav_drawer_perfil)
//                }
//                R.id.nav_drawer_configuracion -> {
//                    // Navegar al fragmento Configuración en el Drawer Menu
//                    navController.navigate(R.id.nav_drawer_configuracion)

                    bottomNavView.visibility = View.GONE
                    navController.navigate(R.id.action_global_nav_drawer_perfil)
                    Toast.makeText(this, "Item 1 selected in Drawer Menu", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_drawer_configuracion -> {
                    bottomNavView.visibility = View.GONE
                    navController.navigate(R.id.action_global_nav_drawer_configuracion)
                    Toast.makeText(this, "Item 2 selected in Drawer Menu", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    supportActionBar?.show()
                    bottomNavView.visibility = View.VISIBLE

                }
            }
            drawerLayout.closeDrawers()
            true
        }
    }

    override fun onSupportNavigateUp(): Boolean {

    //    return NavigationUI.navigateUp(navController, drawerLayout) || super.onSupportNavigateUp()

        // Check if the current destination is the PerfilFragment
        if (navController.currentDestination?.id == R.id.nav_drawer_perfil || navController.currentDestination?.id == R.id.nav_drawer_configuracion) {
            // Handle your custom back action here. For example, pop the back stack:
            navController.popBackStack()
            return true
        } else {
            return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
        }

    }
}
