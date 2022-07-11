package vn.mobifone.tts_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.e
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
import vn.mobifone.tts_project1.model.Data
import vn.mobifone.tts_project1.model.Stickers
import kotlin.math.log

const val BASE_URL: String = "https://mystoragetm.s3.ap-southeast-1.amazonaws.com/"
class MainActivity : AppCompatActivity() {

    lateinit var stickersAdapter: StickersAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerView: RecyclerView

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
                var responseObj = response.body()?.listStickers

                stickersAdapter = StickersAdapter(baseContext, responseObj)
                stickersAdapter.notifyDataSetChanged()
                recyclerView.adapter = stickersAdapter
            }

            override fun onFailure(call: Call<Data?>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}