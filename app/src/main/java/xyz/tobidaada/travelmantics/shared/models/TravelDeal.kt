package xyz.tobidaada.travelmantics.shared.models

import android.os.Parcelable
import com.google.firebase.database.PropertyName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TravelDeal(

    @get:PropertyName("id")
    @set:PropertyName("id")
    var id: String,

    @get:PropertyName("title")
    @set:PropertyName("title")
    var title: String,

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String,

    @get:PropertyName("price")
    @set:PropertyName("price")
    var price: String,

    @get:PropertyName("image_url")
    @set:PropertyName("image_url")
    var imageUrl: String,

    @get:PropertyName("image_name")
    @set:PropertyName("image_name")
    var imageName: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "")
}