package com.example.unscripted

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : BasisActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)


        val btnSubmit : Button = findViewById(R.id.btn_forgotpassword_submit)
        btnSubmit.setOnClickListener {
            val emailID : String = findViewById<EditText>(R.id.et_forgotpassword_email).text
                .toString().trim { it <= ' ' }

            if(TextUtils.isEmpty(emailID)){
                findViewById<TextInputLayout>(R.id.til_forgotpassword_email)
                    .error = getString(R.string.errormessage_login_email)
            }else {
                FirebaseAuth.getInstance().sendPasswordResetEmail(emailID)
                    .addOnCompleteListener { task ->
                        if(task.isSuccessful){
                            showCustomSnackbar(
                                getString(R.string.infomessage_forgotpassword_emailtoresetsend),
                                false)
                            finish()
                        } else {
                            showCustomSnackbar(
                                getString(R.string.errormessage_forgotpassword_emailtoresetsend),
                                true)
                        }
                    }
            }
        }
    }

}