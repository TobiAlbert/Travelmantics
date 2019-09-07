package xyz.tobidaada.travelmantics.shared.utils

import android.app.Activity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

object FirebaseUtil {

    val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var mFirebaseAuth: FirebaseAuth
    lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    lateinit var mStorage: FirebaseStorage
    lateinit var mStorageRef: StorageReference
    lateinit var showMenu: () -> Unit
    var isAdmin: Boolean = false

    const val RC_SIGN_IN = 123

    fun openFbReference(ref: String, activity: Activity) {
        mDatabaseReference = mFirebaseDatabase.reference.child(ref)

        mFirebaseAuth = FirebaseAuth.getInstance()

        val signIn = {
            // Choose authentication providers
            val providers = arrayListOf(
                AuthUI.IdpConfig.EmailBuilder().build(),
                AuthUI.IdpConfig.GoogleBuilder().build()
            )

            // create launch sign-in intent
            activity.startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                RC_SIGN_IN
            )
        }

        showMenu = {
            (activity as ShowMenuListener).showMenu()
        }

        mAuthListener = FirebaseAuth.AuthStateListener {
            if (it.currentUser == null) signIn.invoke()
            else  checkAdmin(it.uid ?: "")
        }

        connectStorage()
    }

    fun logUserOut(activity: Activity) {
        AuthUI.getInstance()
            .signOut(activity)
            .addOnCompleteListener { attachListener() }
    }

    val checkAdmin = { uid: String ->
        isAdmin = false
        val ref = mFirebaseDatabase.reference.child("administrators")
            .child(uid)
        val listener = object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {}

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {}

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                isAdmin = true
                showMenu.invoke()
            }

            override fun onChildRemoved(p0: DataSnapshot) {}
        }
        ref.addChildEventListener(listener)
    }

    fun attachListener() {
        mFirebaseAuth.addAuthStateListener(mAuthListener)
    }

    fun detachListener() {
        mFirebaseAuth.removeAuthStateListener(mAuthListener)
    }

    fun connectStorage() {
        mStorage = FirebaseStorage.getInstance()
        mStorageRef = mStorage.reference.child("deals_pictures")
    }

    interface ShowMenuListener {
        fun showMenu()
    }

}