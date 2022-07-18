package vn.mobifone.tts_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.google.gson.Gson
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants

class ImageDetailsActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var actionBar: ActionBar

    var sticker : String ?= null
    var startURL : String ?= null
    var prefix : String ?= null
    var position : Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.image_detail)

        initView()
        getData()

        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        val imageURL: String? =
            startURL + stickerObj.folder + "/" + stickerObj.folder + "_" + prefix + position + ".png"

        textView.text = position.toString()
        Glide.with(this)
            .load(imageURL)
            .override(300, 300)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    private fun initView() {
        imageView = findViewById(R.id.img_detail)
        textView = findViewById(R.id.img_detail_index)
        actionBar = supportActionBar!!
        actionBar.setTitle("Image Detail")
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun getData() {
        val intent = intent
        sticker = intent.getStringExtra(Constants.STICKER)
        startURL = intent.getStringExtra(Constants.START_URL)
        prefix = intent.getStringExtra(Constants.PREFIX)
        position = intent.getIntExtra(Constants.POSITION, 0)
//        e("position", "position $position")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}