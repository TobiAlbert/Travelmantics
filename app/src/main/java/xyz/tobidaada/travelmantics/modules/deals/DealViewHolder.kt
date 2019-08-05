package xyz.tobidaada.travelmantics.modules.deals

import android.view.View
import androidx.recyclerview.widget.RecyclerView
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
            setOnClickListener { clickCallback(travelDeal) }
        }
    }
}

