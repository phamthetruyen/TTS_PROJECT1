package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

const val BASE_URL: String = "https://mystoragetm.s3.ap-southeast-1.amazonaws.com/"
class MainActivity : AppCompatActivity(), OnItemClickListener {

    lateinit var stickersAdapter: StickersAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

    var listStickers : ArrayList<Stickers>?= null
    var start_url : String?= null
    var prefix : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.folder_recycle)
        recyclerView.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        getData()
    }

    private fun getData() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiService::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<Data?> {
            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                listStickers = response.body()?.listStickers
                start_url = response.body()?.start_url
                prefix = response.body()?.prefix_

                stickersAdapter = StickersAdapter(baseContext, listStickers, start_url, prefix, this@MainActivity)
                stickersAdapter.notifyDataSetChanged()
                recyclerView.adapter = stickersAdapter
            }

            override fun onFailure(call: Call<Data?>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Loi khi lay data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ListImageInFolderActivity::class.java)
//        Log.e("position", "position = $position")
        val sticker = Gson().toJson(listStickers?.get(position))
        intent.putExtra("sticker", sticker)
        intent.putExtra("baseurl", start_url)
        intent.putExtra("prefix", prefix)
        startActivity(intent)
    }
}