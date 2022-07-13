package vn.mobifone.tts_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants

class ImageDetailsActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var textView: TextView

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
            .override(200, 200)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageView)
    }

    private fun initView() {
        imageView = findViewById(R.id.img_detail)
        textView = findViewById(R.id.img_detail_index)
    }

    private fun getData() {
        val intent = intent
        sticker = intent.getStringExtra(Constants.STICKER)
        startURL = intent.getStringExtra(Constants.START_URL)
        prefix = intent.getStringExtra(Constants.PREFIX)
        position = intent.getIntExtra(Constants.POSITION, 0)
//        e("position", "position $position")
    }
}