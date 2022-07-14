package vn.mobifone.tts_project1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.mobifone.tts_project1.adapter.ListFolderAdapter
import vn.mobifone.tts_project1.adapter.RandomFolderAdapter
import vn.mobifone.tts_project1.api.ApiService
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Data
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var stickersAdapter: ListFolderAdapter
    lateinit var randomStickersAdapter : RandomFolderAdapter
    lateinit var gridLayoutManagerListFolder: GridLayoutManager
    lateinit var gridLayoutManagerRandomFolder: LinearLayoutManager
    lateinit var listFolderRecyclerView: RecyclerView
    lateinit var randomFolderRecyclerView: RecyclerView

    var listStickers : ArrayList<Stickers>?= null
    var startUrl : String?= null
    var prefix : String?= null

    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var count : Int = 1

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(ApiService::class.java)

    private val retrofitData = retrofitBuilder.getData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPreferences()
        initView()
        refreshCache()
    }

    private fun initView() {
        listFolderRecyclerView = findViewById(R.id.folder_recycle)
        listFolderRecyclerView.setHasFixedSize(true)
        randomFolderRecyclerView = findViewById(R.id.random_folder)
        randomFolderRecyclerView.setHasFixedSize(true)

        gridLayoutManagerListFolder = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        listFolderRecyclerView.layoutManager = gridLayoutManagerListFolder

        gridLayoutManagerRandomFolder = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        randomFolderRecyclerView.layoutManager = gridLayoutManagerRandomFolder
    }

    private fun refreshCache() {
        if (sharedPref.getInt(Constants.COUNT, count) != null) {
            if (count >= 5) {
                count = 0
                editor.putInt(Constants.COUNT, count)
                getData()
                logCache()
            } else {
                getData()
                count = count.inc()
                editor.putInt(Constants.COUNT, count)
            }
        } else {
            editor.putInt(Constants.COUNT, 0)
            getData()
            logCache()
        }
        editor.apply()
    }

    private fun logCache() {
        editor.putString(Constants.LISTSTICKERS, listStickers.toString())
        editor.putString(Constants.START_URL, startUrl)
        editor.putString(Constants.PREFIX, prefix)
        editor.apply()
    }

    private fun initPreferences() {
        sharedPref = this.getSharedPreferences(Constants.NAMEPREF, MODE_PRIVATE)
        editor = sharedPref.edit()
    }

    private fun getData() {
        retrofitData.enqueue(object : Callback<Data?> {
            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                val responseBody = response.body()!!

                listStickers = responseBody.listStickers
                startUrl = responseBody.start_url
                prefix = responseBody.prefix_

                logCache()
                recycleEvent()
            }

            override fun onFailure(call: Call<Data?>, t: Throwable) {
                e("Error", "Error when get data from API")
                Toast.makeText(this@MainActivity, "Loi khi lay data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recycleEvent() {
        stickersAdapter = ListFolderAdapter(baseContext, listStickers, startUrl, prefix, this@MainActivity)
        stickersAdapter.notifyDataSetChanged()
        listFolderRecyclerView.adapter = stickersAdapter

        randomStickersAdapter = RandomFolderAdapter(baseContext, listStickers!!, startUrl, prefix, this@MainActivity)
        randomStickersAdapter.notifyDataSetChanged()
        randomFolderRecyclerView.adapter = randomStickersAdapter
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ListImageInFolderActivity::class.java)
//        Log.e("position", "position = $position")
        val sticker = Gson().toJson(listStickers?.get(position))
        intent.putExtra(Constants.STICKER, sticker)
        intent.putExtra(Constants.START_URL, startUrl)
        intent.putExtra(Constants.PREFIX, prefix)
        startActivity(intent)
    }
}