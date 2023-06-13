package com.example.unscripted

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegistrationActivity : BasisActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val tvLogin : TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener{
            //startActivity(Intent(this, LoginActivity::class.java))
            onBackPressedDispatcher.onBackPressed()
        }

        setupActionBar()

        val btnRegister : Button = findViewById(R.id.btn_register_register)
        btnRegister.setOnClickListener {
            registerNewUser()
        }
    }

    private fun validateUserInformation(): Boolean {
        val etFirstName : EditText = findViewById(R.id.et_register_firstname)
        val etLastName : EditText = findViewById(R.id.et_register_lastname)
        val etEmail : EditText = findViewById(R.id.et_register_email)
        val etPassword : EditText = findViewById(R.id.et_register_password)
        val etConfirmPassword : EditText = findViewById(R.id.et_register_confirmpassword)

        val cbTermsAndCondition : CheckBox = findViewById(R.id.cb_register_termsandcondition)

        val firstName : String = etFirstName.text.toString().trim{ it <= ' '}
        val lastName : String = etLastName.text.toString().trim{ it <= ' '}
        val email : String = etEmail.text.toString().trim{ it <= ' '}
        val password : String = etPassword.text.toString().trim{ it <= ' '}
        val confirmPassword : String = etConfirmPassword.text.toString().trim{ it <= ' '}

        val returnValue = when{
            firstName.isEmpty() ->{
                showCustomSnackbar("Please enter first name.", true);
                false
            }
            lastName.isEmpty() ->{
                showCustomSnackbar("Please enter last name.", true);
                false
            }
            email.isEmpty() ->{
                showCustomSnackbar("Please enter email.", true);
                false
            }
            password.isEmpty() ->{
                showCustomSnackbar("Please enter a password.", true);
                false
            }
            confirmPassword.isEmpty() ->{
                showCustomSnackbar("Please confirm your password.", true);
                false
            }
            password!=confirmPassword ->{
                showCustomSnackbar("Password and confirm password does not match.", true);
                false
            }
            !cbTermsAndCondition.isChecked ->{
                showCustomSnackbar("Please aggre to the terms and conditions.", true);
                false
            }
            else -> true
        }

        return returnValue
    }

    private fun registerNewUser() {
        if (validateUserInformation()) {
            val etFirstName: EditText = findViewById(R.id.et_register_firstname)
            val etLastName: EditText = findViewById(R.id.et_register_lastname)
            val etEmail: EditText = findViewById(R.id.et_register_email)
            val etPassword: EditText = findViewById(R.id.et_register_password)

            val firstName: String = etFirstName.text.toString().trim { it <= ' ' }
            val lastName: String = etLastName.text.toString().trim { it <= ' ' }
            val email: String = etEmail.text.toString().trim { it <= ' ' }
            val password: String = etPassword.text.toString().trim { it <= ' ' }

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val firebaseUser = task.result!!.user!!
                        showCustomSnackbar("A new user is created with FirebaseID ${firebaseUser.uid}", false)

                        val user = User(firebaseUser.uid, firstName, lastName, email)
                        CloudFirestore().saveUserInfoOnCloudFirestore(this, user)

                    } else {
                        showCustomSnackbar(task.exception!!.toString(), false)
                    }
                }
        }
    }

    private fun setupActionBar(){
        val toolbarRegistrationActivity : Toolbar = findViewById(R.id.toolbar_register_activity)
        setSupportActionBar(toolbarRegistrationActivity)

        val actionBar = supportActionBar
        if(actionBar != null){
            //This will make own action clickable and the "<-" at the left side
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.arrow_back)
        }
        toolbarRegistrationActivity.setNavigationOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }
    }

    fun userRegistrationSuccess() {
        onBackPressedDispatcher.onBackPressed()
    }
}