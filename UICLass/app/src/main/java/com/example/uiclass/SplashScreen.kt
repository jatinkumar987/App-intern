package com.example.uiclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    private  lateinit var mAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@SplashScreen,LoginActivity2::class.java)
            startActivity(intent)
        },1000)

//        mAuth = FirebaseAuth.getInstance()
//        val user = mAuth.currentUser
//        Handler(Looper.getMainLooper()).postDelayed({
//            if(user!=null){
//                val dashboardIntent = Intent(this,MainActivity::class.java)
//                startActivity(dashboardIntent)
//            }else{
//                val signInIntent = Intent(this,LoginActivity2::class.java)
//                startActivity(signInIntent)
//
//            }
//        }, 2000)
    }
}