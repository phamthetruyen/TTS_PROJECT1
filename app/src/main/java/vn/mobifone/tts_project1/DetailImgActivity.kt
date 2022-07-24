package vn.mobifone.tts_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants
import com.google.gson.Gson
import vn.mobifone.tts_project1.apiInterface.ClickItem

class DetailImgActivity : AppCompatActivity() {

    lateinit var imageView: ImageView
    lateinit var textView: TextView
    lateinit var btnBack: Button

    var sticker: String? = null
    var startUrl: String? = null
    var prefix: String? = null
    var position: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_img)
        initView()
        getData()

        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        val imageUrl: String? = startUrl + stickerObj.folder +
                "/" + stickerObj.folder + "_" + prefix + position + ".png"

        textView.text = position.toString()

        Glide.with(this@DetailImgActivity)
            .load(imageUrl)
            .override(200, 200)
            .placeholder(R.drawable.loading)
            .into(imageView)
    }

    private fun getData() {
        val intent = intent
        sticker = intent.getStringExtra(Constants.STICKER)
        startUrl = intent.getStringExtra(Constants.STARTURL)
        prefix = intent.getStringExtra(Constants.PREFIX)
        position = intent.getIntExtra(Constants.POSITION, 0)
    }

    private fun initView() {
        btnBack =findViewById(R.id.btn_back2)
        btnBack.setOnClickListener { finish() }
        imageView = findViewById(R.id.detaiImg)
        textView = findViewById(R.id.detailNameImg)
    }
}