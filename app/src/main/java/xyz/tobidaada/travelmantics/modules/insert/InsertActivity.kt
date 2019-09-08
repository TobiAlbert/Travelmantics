package xyz.tobidaada.travelmantics.modules.insert

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_insert.*
import xyz.tobidaada.travelmantics.R
import xyz.tobidaada.travelmantics.modules.deals.DealActivity
import xyz.tobidaada.travelmantics.shared.models.TravelDeal
import xyz.tobidaada.travelmantics.shared.utils.FirebaseUtil
import xyz.tobidaada.travelmantics.shared.utils.makeToast
import xyz.tobidaada.travelmantics.shared.utils.showToast

class InsertActivity : AppCompatActivity(), FirebaseUtil.ShowMenuListener {

    private lateinit var mFirebaseDatabase: FirebaseDatabase
    private lateinit var mDatabaseReference: DatabaseReference
    private lateinit var mDeal: TravelDeal

    companion object {

        const val TRAVEL_DEAL_EXTRA = "travel_deal_extra"
        const val UPLOAD_IMAGE_REQUEST_CODE = 12345

        fun getStartIntent(context: Context, deal: TravelDeal? = null): Intent {
            return Intent(context, InsertActivity::class.java).apply {
                if (deal != null) putExtra(TRAVEL_DEAL_EXTRA, deal)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert)

        FirebaseUtil.openFbReference("traveldeals", this)

        mFirebaseDatabase = FirebaseUtil.mFirebaseDatabase
        mDatabaseReference = FirebaseUtil.mDatabaseReference

        if (intent.hasExtra(TRAVEL_DEAL_EXTRA)) {
            mDeal = intent.getParcelableExtra(TRAVEL_DEAL_EXTRA) ?: return
            titleEt.setText(mDeal.title)
            descriptionEt.setText(mDeal.description)
            priceEt.setText(mDeal.price)
            showImage(mDeal.imageUrl)
        } else {
            mDeal = TravelDeal()
        }

        uploadImageBtn.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "image/*"
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                startActivityForResult(Intent.createChooser(this, "Insert Picture"), UPLOAD_IMAGE_REQUEST_CODE)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.insert_menu, menu)

        menu.findItem(R.id.menu_delete).isVisible = FirebaseUtil.isAdmin
        menu.findItem(R.id.menu_save).isVisible = FirebaseUtil.isAdmin
        enableEditText(FirebaseUtil.isAdmin)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPLOAD_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data ?: return

                val imageUri = data.data
                val ref = FirebaseUtil.mStorageRef.child(imageUri?.lastPathSegment ?: return)
                ref.putFile(imageUri).addOnCompleteListener {
                    if (!it.isSuccessful) {
                        makeToast("Unable to upload image.")
                        return@addOnCompleteListener
                    }

                    //TODO: Ensure file isn't saved before getting downloadable image
                    // uri
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        mDeal.imageUrl = "$uri"
                        showImage(uri.toString())
                        Log.i("InsertActivity", "Upload URI: $uri")
                    }
                }
            }
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

    override fun showMenu() {
        invalidateOptionsMenu()
    }

    fun enableEditText(isEnabled: Boolean) {
        priceEt.isEnabled = isEnabled
        descriptionEt.isEnabled = isEnabled
        titleEt.isEnabled = isEnabled
    }

    private fun showImage(url: String) {

        if (url.isEmpty()) return

        val width =  Resources.getSystem().displayMetrics.widthPixels
        Picasso.get().load(url).resize(width, width * 2 / 3).centerCrop().into(dealImage)
    }

}
