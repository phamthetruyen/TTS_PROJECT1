package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.mobifone.tts_project1.adapter.ListImageInFolderAdapter
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Stickers

class ListImageInFolderActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var listImageAdapter : ListImageInFolderAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    var sticker : String ?= null
    var baseurl : String ?= null
    var prefix : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_image_folder)
        initView()
        getData()
    }

    fun initView() {
        recyclerView = findViewById(R.id.list_img_folder_recycle)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
    }

    fun getData() {
        val intent = intent
        sticker = intent.getStringExtra("sticker")
        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        baseurl = intent.getStringExtra("baseurl")
        prefix = intent.getStringExtra("prefix")

        listImageAdapter = ListImageInFolderAdapter(this, baseurl!!, prefix!!, stickerObj.totalImage, stickerObj.folder, this@ListImageInFolderActivity)
        listImageAdapter.notifyDataSetChanged()
        recyclerView.adapter = listImageAdapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ImageDetailsActivity::class.java)
        intent.putExtra("sticker", sticker)
        intent.putExtra("startURL", baseurl)
        intent.putExtra("prefix", prefix)
        intent.putExtra("position", position.inc())
        startActivity(intent)
    }
}