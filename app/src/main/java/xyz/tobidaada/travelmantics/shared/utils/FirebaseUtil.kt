package xyz.tobidaada.travelmantics.shared.utils

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

object FirebaseUtil {


    val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var mDatabaseReference: DatabaseReference

    fun openFbReference(ref: String) {
        mDatabaseReference = mFirebaseDatabase.reference.child(ref)
    }
}