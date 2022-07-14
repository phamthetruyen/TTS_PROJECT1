package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.mobifone.tts_project1.adapter.ListImgFolderAdapter
import vn.mobifone.tts_project1.apiInterface.ClickItem
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants
import com.google.gson.Gson

class ListImgFolderActivity : AppCompatActivity(), ClickItem {

    lateinit var recyclerView: RecyclerView
    lateinit var listImgFolderAdapter: ListImgFolderAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var imgFolderView: ImageView
    lateinit var txtFolder: TextView

    var sticker: String? = null
    var baseurl: String? = null
    var prefix: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_img_folder)
        initView()
        getData()
    }

    private fun initView() {
        recyclerView = findViewById(R.id.rv_listImgFolder)
        recyclerView.setHasFixedSize(true)
        gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
    }

    private fun getData() {
        val intent = getIntent()
        sticker = intent.getStringExtra("sticker")
        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        baseurl = intent.getStringExtra("baseurl")
        prefix = intent.getStringExtra("prefix")

        listImgFolderAdapter =
            ListImgFolderAdapter(
                this,
                baseurl!!,
                prefix!!,
                stickerObj.totalImage,
                stickerObj.folder,
                this@ListImgFolderActivity
            )
        listImgFolderAdapter.notifyDataSetChanged()
        recyclerView.adapter = listImgFolderAdapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailImgActivity::class.java)
        intent.putExtra(Constants.STICKER, sticker)
        intent.putExtra(Constants.STARTURL, baseurl)
        intent.putExtra(Constants.PREFIX, prefix)
        intent.putExtra(Constants.POSITION, position.inc())
        startActivity(intent)
    }
}