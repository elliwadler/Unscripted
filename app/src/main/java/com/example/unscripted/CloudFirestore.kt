package com.example.unscripted

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class CloudFirestore {

    private val firestoreInstance = FirebaseFirestore.getInstance()
    private val storageRef = Firebase.storage.reference

    fun saveUserInfoOnCloudFirestore(registrationActivity: RegistrationActivity, currentUser:User){
        firestoreInstance.collection(Constant.TABLENAME_USER)
            .document(currentUser.id.toString())
            .set(currentUser, SetOptions.merge())
            .addOnSuccessListener {
                registrationActivity.userRegistrationSuccess()
            }
            .addOnFailureListener{exc ->
                Log.e("test", "error occured", exc)
            }
    }

    fun getUserDetails(LoginActivity:LoginActivity){
        firestoreInstance
            .collection(Constant.TABLENAME_USER)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val user:User = document.toObject(User::class.java)!!
                LoginActivity.userLoggedInSuccess(user)
            }
            .addOnFailureListener{exc ->
                Log.e(LoginActivity.javaClass.name, "error occured", exc)
            }
    }

    private fun getCurrentUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser
        if(currentUser != null)
            return currentUser.uid
        return  ""
    }
}