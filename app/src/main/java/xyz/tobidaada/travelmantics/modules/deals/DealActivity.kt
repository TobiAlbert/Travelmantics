package xyz.tobidaada.travelmantics.modules.deals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_deal.*
import xyz.tobidaada.travelmantics.R
import xyz.tobidaada.travelmantics.modules.insert.InsertActivity
import xyz.tobidaada.travelmantics.shared.models.TravelDeal
import xyz.tobidaada.travelmantics.shared.utils.FirebaseUtil

class DealActivity : AppCompatActivity() {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference

    private lateinit var mAdapter: DealAdapter

    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {}

        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
            try {
                val travelDeal = snapshot.getValue(TravelDeal::class.java)
                travelDeal?.let {
                    travelDeal.id = snapshot.key ?: return@let
                    mAdapter.updateDeal(it)
                }
            } catch(exception: DatabaseException) {

            }
        }

        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            try {
                val travelDeal = snapshot.getValue(TravelDeal::class.java)
                travelDeal?.let {
                    travelDeal.id = snapshot.key ?: return@let
                    mAdapter.addDeal(it)
                }
            } catch(exception: DatabaseException) {

            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deal)

        mAdapter = DealAdapter(::openInsertActivity)

        rvDeals.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(this@DealActivity)
            setHasFixedSize(true)
        }

        FirebaseUtil.openFbReference("traveldeals")

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference
        mDatabaseReference.addChildEventListener(childEventListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_insert -> {
                startActivity(InsertActivity.getStartIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openInsertActivity(travelDeal: TravelDeal) {
        startActivity(InsertActivity.getStartIntent(this, travelDeal))
    }
}
