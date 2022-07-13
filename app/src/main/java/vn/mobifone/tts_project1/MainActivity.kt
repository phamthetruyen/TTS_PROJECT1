package vn.mobifone.tts_project1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.mobifone.tts_project1.adapter.StickersAdapter
import vn.mobifone.tts_project1.api.ApiService
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Data
import vn.mobifone.tts_project1.model.Stickers
import vn.mobifone.tts_project1.util.Constants

class MainActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var stickersAdapter: StickersAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    var listStickers : ArrayList<Stickers>?= null
    var startUrl : String?= null
    var prefix : String?= null

    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var count : Int = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPreferences()

        recyclerView = findViewById(R.id.folder_recycle)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager

        getData()
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
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(ApiService::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<Data?> {
            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                val responseBody = response.body()!!

                listStickers = responseBody.listStickers
                startUrl = responseBody.start_url
                prefix = responseBody.prefix_

                if (sharedPref.getInt(Constants.COUNT, count) != null) {
                    if (count >= 5) {
                        count = 0
                        editor.putInt(Constants.COUNT, count)
                        logCache()
                    } else {
                        editor.putInt(Constants.COUNT, count.inc())
                    }
                } else {
                    editor.putInt(Constants.COUNT, count)
                }
                editor.apply()

                stickersAdapter = StickersAdapter(baseContext, listStickers, startUrl, prefix, this@MainActivity)
                stickersAdapter.notifyDataSetChanged()
                recyclerView.adapter = stickersAdapter
            }

            override fun onFailure(call: Call<Data?>, t: Throwable) {
                e("Error", "Error when get data from API")
                Toast.makeText(this@MainActivity, "Loi khi lay data", Toast.LENGTH_SHORT).show()
            }
        })
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