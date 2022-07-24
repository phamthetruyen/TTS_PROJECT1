package vn.mobifone.tts_project1

import android.app.ActionBar
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.adapter.ListImgFolderAdapter
import vn.mobifone.tts_project1.apiInterface.ClickItem
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants
import com.google.gson.Gson
import java.util.*

class ListImgFolderActivity : AppCompatActivity(), ClickItem {

    lateinit var recyclerView: RecyclerView
    lateinit var listImgFolderAdapter: ListImgFolderAdapter
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var btnBack: Button

    lateinit var textViewFolderName: TextView
    lateinit var textViewTotalImage: TextView
    lateinit var imageFolder: ImageView


    var sticker: String? = null
    var starturl: String? = null
    var prefix: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_img_folder)
        initView()
        getData()
    }

    private fun initView() {
        btnBack = findViewById(R.id.btn_back1)
        btnBack.setOnClickListener { finish() }
        recyclerView = findViewById(R.id.rv_listImgFolder)
        recyclerView.setHasFixedSize(true)
        gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager

        //
        textViewFolderName = findViewById(R.id.txt_nameFolderList)
        textViewTotalImage = findViewById(R.id.txt_totalImg)
        imageFolder = findViewById(R.id.img_ListFolder)

    }

    private fun getData() {
        val intent = getIntent()
        sticker = intent.getStringExtra(Constants.STICKER)
        val stickerObj = Gson().fromJson(sticker, Stickers::class.java)
        starturl = intent.getStringExtra(Constants.STARTURL)
        prefix = intent.getStringExtra(Constants.PREFIX)

        listImgFolderAdapter =
            ListImgFolderAdapter(
                this,
                starturl!!,
                prefix!!,
                stickerObj.totalImage,
                stickerObj.folder,
                this@ListImgFolderActivity
            )
        listImgFolderAdapter.notifyDataSetChanged()
        recyclerView.adapter = listImgFolderAdapter

        //imgFolder
        if (Locale.getDefault().language == "en") {
            textViewFolderName.text = stickerObj.name_en
        } else {
            textViewFolderName.text = stickerObj.name_vi
        }
        textViewTotalImage.text = "${stickerObj.totalImage.toString()} image"
        val imgUrl: String? =
            starturl + stickerObj.folder + "/" + stickerObj.folder + "_" + prefix + "1" + ".png"
        Glide.with(this)
            .load(imgUrl)
            .override(100, 100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageFolder)
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(this, DetailImgActivity::class.java)

        intent.putExtra(Constants.STICKER, sticker)
        intent.putExtra(Constants.STARTURL, starturl)
        intent.putExtra(Constants.PREFIX, prefix)
        intent.putExtra(Constants.POSITION, position.inc())
        startActivity(intent)
    }
}