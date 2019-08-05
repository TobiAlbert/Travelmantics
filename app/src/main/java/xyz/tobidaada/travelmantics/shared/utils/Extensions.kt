package xyz.tobidaada.travelmantics.shared.utils

import android.content.Context
import android.widget.Toast

// Context Extensions
fun Context.showToast(message: String) {
    this.makeToast(message).show()
}

fun Context.makeToast(message: String): Toast =
    Toast.makeText(this, message, Toast.LENGTH_SHORT)
