package xyz.tobidaada.travelmantics.modules.deals

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_item_travel_deal.view.*
import xyz.tobidaada.travelmantics.shared.models.TravelDeal

class DealViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(travelDeal: TravelDeal, clickCallback: (TravelDeal) -> Unit) {
        itemView.apply {
            dealsTxt.text = travelDeal.title
            descriptionTxt.text = travelDeal.description
            priceTxt.text = travelDeal.price
            loadImageFromUrl(travelDeal.imageUrl, dealsImage)

            setOnClickListener { clickCallback(travelDeal) }
        }
    }

    private fun loadImageFromUrl(url: String, imgView: ImageView) {
        if (url.isEmpty()) return

        Picasso.get().load(url).into(imgView)
    }
}

