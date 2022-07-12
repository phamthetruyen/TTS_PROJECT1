package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import vn.mobifone.tts_project1.adapter.ListImageInFolderAdapter
import vn.mobifone.tts_project1.model.Stickers

class ListImageInFolderActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var listImageAdapter : ListImageInFolderAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_image_folder)
        recyclerView = findViewById(R.id.list_img_folder_recycle)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        val intent = getIntent()
        val sticker : String = intent.getStringExtra("sticker")!!
        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        val baseurl : String = intent.getStringExtra("baseurl")!!
        val prefix : String = intent.getStringExtra("prefix")!!

        listImageAdapter = ListImageInFolderAdapter(this, baseurl, prefix, stickerObj.totalImage, stickerObj.folder)
        listImageAdapter.notifyDataSetChanged()
        recyclerView.adapter = listImageAdapter
    }

}