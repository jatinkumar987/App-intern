package com.example.uiclass

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        val btn_register:Button = findViewById(R.id.btn_register)
        val et_register_email:EditText = findViewById(R.id.et_register_email)
        val et_register_password:EditText = findViewById(R.id.et_register_password)
        val tv_login:TextView = findViewById(R.id.tv_login)
        tv_login.setOnClickListener {
//            startActivity(Intent(this@RegisterActivity2,LoginActivity2::class.java))

            onBackPressed() // alternative

        }
        btn_register.setOnClickListener {
            registerme(et_register_email,et_register_password)
            
        }
    }

    private fun registerme(et_register_email:EditText,et_register_password:EditText) {
        when{
            TextUtils.isEmpty(et_register_email.text.toString().trim{ it <=' ' })->{
                Toast.makeText(this@RegisterActivity2, "Please enter email", Toast.LENGTH_SHORT).show()
            }
            TextUtils.isEmpty(et_register_password.text.toString().trim{ it <=' ' })->{
                Toast.makeText(this@RegisterActivity2, "Please enter password", Toast.LENGTH_SHORT).show()


            }
            else->{
                val email:String = et_register_email.text.toString().trim{it<= ' '}
                val password:String = et_register_password.text.toString().trim{it<= ' '}


                //instance and creat a register a user with email and password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener({
                            task->
                        if(task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@RegisterActivity2,
                                "you are registered successfully",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent =
                                Intent(this@RegisterActivity2, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id",firebaseUser.uid)
                            intent.putExtra("email_id",email)
                            startActivity(intent)
                            finish()
                        }else{
                            Toast.makeText(this@RegisterActivity2,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()

                        }

                    })


            }
        }
    }
}