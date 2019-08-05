package xyz.tobidaada.travelmantics.modules.insert

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_insert.*
import xyz.tobidaada.travelmantics.R
import xyz.tobidaada.travelmantics.modules.deals.DealActivity
import xyz.tobidaada.travelmantics.shared.models.TravelDeal
import xyz.tobidaada.travelmantics.shared.utils.showToast
import xyz.tobidaada.travelmantics.shared.utils.FirebaseUtil

class InsertActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDeal: TravelDeal

    companion object {

        const val TRAVEL_DEAL_EXTRA = "travel_deal_extra"

        fun getStartIntent(context: Context, deal: TravelDeal? = null): Intent {
            return Intent(context, InsertActivity::class.java).apply {
                putExtra(TRAVEL_DEAL_EXTRA, deal)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        FirebaseUtil.openFbReference("traveldeals")

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference

        if (intent.hasExtra(TRAVEL_DEAL_EXTRA)) {
            mDeal = intent.getParcelableExtra(TRAVEL_DEAL_EXTRA) ?: return
            titleEt.setText(mDeal.title)
            descriptionEt.setText(mDeal.description)
            priceEt.setText(mDeal.price)
        } else {
            mDeal = TravelDeal()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.insert_menu, menu)
        return true
    }

    //TODO: Implement startActivity for result, instead of
    //  explicitly calling `backToList()`. Also consider making this
    //  activity a child of `DealActivity.`
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_save -> {
                saveDeal()
                showToast("Deal Saved.")
                backToList()
                true
            }
            R.id.menu_delete -> {
                deleteDeal()
                backToList()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveDeal() {

        mDeal.apply {
            title = titleEt.text.toString()
            price = priceEt.text.toString()
            description = descriptionEt.text.toString()
        }

        if (mDeal.id.isNullOrEmpty()) mDatabaseReference.push().setValue(mDeal)
        else mDatabaseReference.child(mDeal.id).setValue(mDeal)
    }

    private fun deleteDeal() {
        // check if deal exists
        if (mDeal.id.isNullOrEmpty()) {
            showToast("Deal does not exist.")
            return
        }

        mDatabaseReference.child(mDeal.id).removeValue()
    }

    private fun backToList() {
        startActivity(Intent(this, DealActivity::class.java))
    }

}
