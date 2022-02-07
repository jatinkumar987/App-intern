package com.example.uiclass

import android.content.ContentValues
import android.content.Intent
import android.opengl.ETC1
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.*
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import com.example.uiclass.databinding.ActivityLogin2Binding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity2 : AppCompatActivity() {
    companion object{
        const val CONST_SIGN_IN = 34
    }
    lateinit var binding:ActivityLogin2Binding
    private lateinit var auth:FirebaseAuth
    private lateinit var googleAuth: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login2)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login2)
        auth = FirebaseAuth.getInstance()
        checkUser()
        val tv_register:TextView = findViewById(R.id.tv_register)
        val  et_login_email:EditText = findViewById(R.id.et_login_email)
        val et_login_password:EditText = findViewById(R.id.et_login_password)
        val btn_login:Button = findViewById(R.id.btn_login)

        tv_register.setOnClickListener {
            startActivity(Intent(this@LoginActivity2,RegisterActivity2::class.java))

        }

        btn_login.setOnClickListener {
            loginclick(et_login_email,et_login_password)

        }

        val login:ImageView = findViewById(R.id.btn_sign_in)
        login.setOnClickListener {
            GoogleSignIn()
        }
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_iid))
            .requestEmail()
            .build()

        googleAuth = GoogleSignIn.getClient(this, gso)
    }

    private fun checkUser() {
        val firebaseUser = auth.currentUser
        if(firebaseUser!=null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }

    private fun loginclick(et_login_email:EditText,et_login_password:EditText) {
        when{
            TextUtils.isEmpty(et_login_email.text.toString().trim{ it <=' ' })->{
//                Toast.makeText(this@LoginActivity2, "Please enter email", Toast.LENGTH_SHORT).show()
                            binding.textInputLayoutEmail.error = "*Required"




            }
            TextUtils.isEmpty(et_login_password.text.toString().trim{ it <=' ' })->{
//                Toast.makeText(this@LoginActivity2, "Please enter password", Toast.LENGTH_SHORT).show()
                binding.textInputLayoutPassword.error = "*Required"
            }
            else->{
                val email:String = et_login_email.text.toString().trim{it<= ' '}
                val password:String = et_login_password.text.toString().trim{it<= ' '}


                //ilogin using firebase
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener({
                            task->
                        if(task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            Toast.makeText(
                                this@LoginActivity2,
                                "you are logged in successfully",
                                Toast.LENGTH_LONG
                            ).show()

                            val intent =
                                Intent(this@LoginActivity2, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            intent.putExtra("user_id",firebaseUser.uid)
                            intent.putExtra("email_id",email)
                            startActivity(intent)
                            finish()
                        }else{
//                                Toast.makeText(this@LoginActivity2,task.exception!!.message.toString(),
//                                    Toast.LENGTH_LONG).show()
//                            Toast.makeText(this@LoginActivity2,"enter a valid email",
//                                Toast.LENGTH_LONG).show()
                            binding.textInputLayoutEmail.error = "Enter a valid Email"
                            binding.textInputLayoutPassword.error = "Enter valid Password"

                        }

                    })


            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(ContentValues.TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    startActivity(Intent(this,MainActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(ContentValues.TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }
    private fun GoogleSignIn(){
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if(account==null){
            val singInIntent = googleAuth.signInIntent
            // new method for StartActivityForResult
//            registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
//                singInIntent
//                CONST_SIGN_IN
//            }
            startActivityForResult(singInIntent, CONST_SIGN_IN)
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== CONST_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken)
            }
            catch (e: ApiException){
                Toast.makeText(this,"${e}",Toast.LENGTH_LONG).show()
            }
        }
    }
}