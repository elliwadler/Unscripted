// Firebase Accesses
// last updated 22.06.2023
// Author Elisabeth Wadler
package com.example.unscripted

import Entry
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class CloudFirestore {

    private val firestormInstance = FirebaseFirestore.getInstance()

    fun saveUserInfoOnCloudFirestore(registrationActivity: RegistrationActivity, currentUser: User) {
        firestormInstance.collection(Constant.TABLENAME_USER)
            .document(currentUser.id.toString())
            .set(currentUser, SetOptions.merge())
            .addOnSuccessListener {
                registrationActivity.userRegistrationSuccess()
            }
            .addOnFailureListener { exc ->
                Log.e("Error", "An error occurred!", exc)
            }
    }

    fun saveEntryInfoOnCloudFirestore(newEntryActivity: com.example.unscripted.NewEntryActivity, newEntry: Entry) {
        newEntry.userId = getCurrentUserID() // Set the user ID as a field in the Entry object

        firestormInstance.collection(Constant.TABLENAME_ENTRY)
            .document(newEntry.id.toString())
            .set(newEntry, SetOptions.merge())
            .addOnSuccessListener {
                newEntryActivity.newEntrySuccess()
            }
            .addOnFailureListener { exc ->
                Log.e("Error", "An error occurred!", exc)
            }
    }

    fun deleteEntryCloudFirestore(detailActivity:DetailActivity, entry:Entry) {
        firestormInstance.collection(Constant.TABLENAME_ENTRY).document(entry.id!!)
            .delete()
            .addOnSuccessListener {
                detailActivity.deleteEntrySuccess()
            }
            .addOnFailureListener { exc ->
                Log.e("Error", "An error occurred!", exc)
            }
    }

    fun getUserDetails(LoginActivity: LoginActivity) {
        firestormInstance
            .collection(Constant.TABLENAME_USER)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val user: User = document.toObject(User::class.java)!!
                LoginActivity.userLoggedInSuccess(user)
            }
            .addOnFailureListener { exc ->
                Log.e(LoginActivity.javaClass.name, "An error occurred!", exc)
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser != null) {
            return currentUser.uid
        }
        return ""
    }

    fun getAllEntries(onSuccess: (List<Entry>) -> Unit, onFailure: (Exception) -> Unit) {
        firestormInstance.collection(Constant.TABLENAME_ENTRY)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val entries = mutableListOf<Entry>()
                for (document in querySnapshot) {
                    val entry = document.toObject(Entry::class.java)
                    entries.add(entry)
                }
                onSuccess(entries)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}
