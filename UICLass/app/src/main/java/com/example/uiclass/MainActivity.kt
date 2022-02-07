package com.example.uiclass

import android.content.Intent
import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var  toogle : ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        auth = FirebaseAuth.getInstance()
//        val user:TextView = findViewById(R.id.user_id)


        // side navigation
        drawerLayout = findViewById(R.id.drawerLayout)
        val navView: NavigationView = findViewById(R.id.nav_view)


        toogle = ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener { MenuItem ->

            MenuItem.isChecked = true
            when(MenuItem.itemId){
                R.id.nav_myorder-> {
                    replacefrag(MyOrderFragment(),MenuItem.title.toString())
                }
                R.id.nav_setting-> Toast.makeText(this,"Clicked", Toast.LENGTH_LONG).show()
                R.id.nav_logout-> {
                    Toast.makeText(this,"Logout Successful",Toast.LENGTH_LONG).show()
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this@MainActivity,LoginActivity2::class.java))
                    finish()
                }

            }
            true
        }


    }



    private fun replacefrag(fragment: Fragment,title: String){
        val fragmentManager  = supportFragmentManager
        val fragTransaction = fragmentManager.beginTransaction()
        fragTransaction.replace(R.id.frameLayout,fragment)
        fragTransaction.commit()
        drawerLayout.closeDrawers()
        setTitle(title)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toogle.onOptionsItemSelected(item)){
            return true
        }
        return false
    }
}