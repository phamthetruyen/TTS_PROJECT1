package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.w3c.dom.Text
import vn.mobifone.tts_project1.adapter.ListImageInFolderAdapter
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants

class ListImageInFolderActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var recyclerView: RecyclerView
    lateinit var listImageAdapter : ListImageInFolderAdapter
    lateinit var gridLayoutManager: LinearLayoutManager
    lateinit var textViewFolderName : TextView
    lateinit var textViewTotalImage : TextView
    lateinit var imageFolder : ImageView

    var sticker : String ?= null
    var startURL : String ?= null
    var prefix : String ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_image_in_folder)
        initView()
        getData()
    }

    fun initView() {
        recyclerView = findViewById(R.id.list_img_folder_recycle)
        recyclerView.setHasFixedSize(true)
        gridLayoutManager = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        textViewFolderName = findViewById(R.id.list_img_folder_name)
        textViewTotalImage = findViewById(R.id.list_img_folder_total_image)
        imageFolder = findViewById(R.id.list_img_folder_folder_img)
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

        textViewFolderName.text = stickerObj.name_en.toString()
        textViewTotalImage.text = "${stickerObj.totalImage.toString()} images"
        val imageURL: String? =
            startURL + stickerObj.folder + "/" + stickerObj.folder + "_" + prefix + "1" + ".png"
        Log.e("a", "dada $imageURL")
        Glide.with(this)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(imageFolder)
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