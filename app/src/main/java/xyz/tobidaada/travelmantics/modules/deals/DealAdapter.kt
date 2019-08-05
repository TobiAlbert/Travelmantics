package xyz.tobidaada.travelmantics.modules.deals

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import xyz.tobidaada.travelmantics.R
import xyz.tobidaada.travelmantics.shared.models.TravelDeal

class DealAdapter(
    private val clickCallback: (TravelDeal) -> Unit
) : RecyclerView.Adapter<DealViewHolder>() {

    private val deals = mutableListOf<TravelDeal>()

    fun addDeal(deal: TravelDeal) {
        deals.add(deal)
        notifyDataSetChanged()
    }

    fun updateDeal(deal: TravelDeal) {
       try {
            // get the position of the child that changed
            val localDeal = deals.first{ deal.id == it.id }

           val index = deals.indexOf(localDeal)
           deals.removeAt(index)
           deals.add(index, deal)

           // notify the adapter of said position
           notifyItemChanged(index)
       } catch (ex: NoSuchElementException) {}
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(R.layout.list_item_travel_deal, parent, false)
        return DealViewHolder(rootView)
    }

    override fun getItemCount(): Int = deals.size

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) =
            holder.bind(deals[position], clickCallback)
}
