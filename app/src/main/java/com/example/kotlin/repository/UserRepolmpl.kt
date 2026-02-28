package com.example.kotlin.repository

import com.example.kotlin.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class UserRepolmpl : UserRepo {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val ref: DatabaseReference = database.getReference("Users")

    override fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Login Success")
            else callback(false, it.exception?.message)
        }
    }

    override fun register(email: String, password: String, callback: (Boolean, String, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                callback(true, "Register Success", auth.currentUser?.uid ?: "")
            } else {
                callback(false, it.exception?.message ?: "Error", "")
            }
        }
    }

    override fun addUserToDatabase(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        ref.child(userId).setValue(model).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "User data saved")
            else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun getUserById(userId: String, callback: (Boolean, UserModel) -> Unit) {
        ref.child(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(UserModel::class.java)
                if (user != null) callback(true, user)
                else callback(false, UserModel())
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, UserModel())
            }
        })
    }

    override fun getAllUser(callback: (Boolean, List<UserModel>) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<UserModel>()
                for (data in snapshot.children) {
                    data.getValue(UserModel::class.java)?.let { list.add(it) }
                }
                callback(true, list)
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false, emptyList())
            }
        })
    }

    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserData(callback: (UserModel?) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid != null) {
            getUserById(uid) { success, model ->
                if (success) callback(model) else callback(null)
            }
        } else {
            callback(null)
        }
    }

    override fun getCurrentUser(): FirebaseUser? = auth.currentUser

    override fun deleteUser(userId: String, callback: (Boolean, String) -> Unit) {
        ref.child(userId).removeValue().addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Deleted")
            else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun updateProfile(userId: String, model: UserModel, callback: (Boolean, String) -> Unit) {
        // FIXED: Using firstName and lastName to match your UserModel
        val updates = mapOf(
            "firstName" to model.firstName,
            "lastName" to model.lastName,
            "dob" to model.dob,
            "gender" to model.gender
        )
        ref.child(userId).updateChildren(updates).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Profile Updated")
            else callback(false, it.exception?.message ?: "Error")
        }
    }

    override fun forgetPassword(email: String, callback: (Boolean, String) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) callback(true, "Reset link sent")
            else callback(false, it.exception?.message ?: "Error")
        }
    }
    fun toggleSaveRestaurant(restaurantId: String, callback: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid ?: return
        val savedRef = database.getReference("Saved").child(uid).child(restaurantId)

        savedRef.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                savedRef.removeValue().addOnCompleteListener { callback(true) } // Unsave
            } else {
                savedRef.setValue(true).addOnCompleteListener { callback(true) } // Save
            }
        }
    }
}