package vn.mobifone.tts_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import vn.mobifone.tts_project1.model.Stickers

class MainActivity3 : AppCompatActivity() {
    lateinit var img : ImageView
    lateinit var text : TextView
    var stickerItem :String ?=null
    var prefix : String ? =null
    var start_url : String ? = null
    var folder : String ? = null
    var position : Int ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        initView()
        getData()

    }

    private fun initView() {
        img = findViewById(R.id.imgv)
        text = findViewById(R.id.tv_index)
    }

    private fun getData() {
        val intent = intent
        stickerItem = intent.getStringExtra("item")
        val stickerOb = Gson().fromJson(stickerItem, Stickers::class.java)
        prefix = intent.getStringExtra("prefix")
        start_url = intent.getStringExtra("start_url")
        folder = intent.getStringExtra("folder")
        position = intent.getIntExtra("position",0)

        text.text= position.toString()
        val imageURL: String? =
            start_url + stickerOb.folder + "/" + stickerOb.folder + "_" + prefix + position + ".png"

        Glide.with(this)
            .load(imageURL)
            .override(200, 200)
            .placeholder(R.drawable.load)
            .into(img)
    }
}