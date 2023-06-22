// Registration functionality
// last updated 17.06.2023
// Author Elisabeth Wadler
package com.example.unscripted

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class RegistrationActivity : BasisActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val tvLogin : TextView = findViewById(R.id.tv_register_login)
        tvLogin.setOnClickListener{
            onBackPressedDispatcher.onBackPressed()
        }


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
            TextUtils.isEmpty(firstName) -> {
                (findViewById<TextInputLayout>(R.id.til_register_firstname)).error = getString(R.string.errormessage_registration_firstName)
                false
            }
            TextUtils.isEmpty(lastName) -> {
                (findViewById<TextInputLayout>(R.id.til_register_lastname)).error = getString(R.string.errormessage_registration_lastName)
                false
            }
            TextUtils.isEmpty(email) -> {
                (findViewById<TextInputLayout>(R.id.til_register_email)).error = getString(R.string.errormessage_registration_email)
                false
            }
            TextUtils.isEmpty(password) -> {
                (findViewById<TextInputLayout>(R.id.til_register_password)).error = getString(R.string.errormessage_registration_password)
                false
            }
            TextUtils.isEmpty(confirmPassword) -> {
                (findViewById<TextInputLayout>(R.id.til_register_confirmpassword)).error = getString(R.string.errormessage_registration_confirmpassword)
                false
            }
            password!=confirmPassword ->{
                (findViewById<TextInputLayout>(R.id.til_register_confirmpassword)).error = getString(R.string.errormessage_registration_match_confirmpassword)
                false
            }
            !cbTermsAndCondition.isChecked ->{
                showCustomSnackbar(getString(R.string.errormessage_registration_terms), true);
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


    fun userRegistrationSuccess() {
        onBackPressedDispatcher.onBackPressed()
    }
}