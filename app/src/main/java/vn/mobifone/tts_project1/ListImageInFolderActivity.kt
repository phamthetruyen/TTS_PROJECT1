package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.mobifone.tts_project1.adapter.ListImageInFolderAdapter
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants

class ListImageInFolderActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var listImageAdapter : ListImageInFolderAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    var sticker : String ?= null
    var startURL : String ?= null
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
        sticker = intent.getStringExtra(Constants.STICKER)
        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        startURL = intent.getStringExtra(Constants.START_URL)
        prefix = intent.getStringExtra(Constants.PREFIX)

        listImageAdapter = ListImageInFolderAdapter(this, startURL!!, prefix!!, stickerObj.totalImage, stickerObj.folder, this@ListImageInFolderActivity)
        listImageAdapter.notifyDataSetChanged()
        recyclerView.adapter = listImageAdapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ImageDetailsActivity::class.java)
        intent.putExtra(Constants.STICKER, sticker)
        intent.putExtra(Constants.START_URL, startURL)
        intent.putExtra(Constants.PREFIX, prefix)
        intent.putExtra(Constants.POSITION, position.inc())
        startActivity(intent)
    }
}