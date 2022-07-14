package vn.mobifone.tts_project1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vn.mobifone.tts_project1.adapter.MyAdapter
import vn.mobifone.tts_project1.adapter.MyAdapterRandom
import vn.mobifone.tts_project1.apiInterface.APIInterface
import vn.mobifone.tts_project1.apiInterface.ClickItem
import vn.mobifone.tts_project1.model.MyData
import vn.mobifone.tts_project1.model.Stickers
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://mystoragetm.s3.ap-southeast-1.amazonaws.com/"
class MainActivity : AppCompatActivity(), ClickItem {

    lateinit var myAdapter: MyAdapter
    lateinit var myAdapterRandom: MyAdapterRandom
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var gridLayoutManagerRandom: GridLayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewRandom: RecyclerView

    var listStickers: ArrayList<Stickers>?=null
    var start_url:String?=null
    var prefix: String?= null

    private lateinit var sharePreferences :SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewRandom =findViewById(R.id.rv_folder_random)
        gridLayoutManagerRandom = GridLayoutManager(this,3, RecyclerView.VERTICAL,false)
        recyclerViewRandom.setHasFixedSize(true)
        recyclerViewRandom.layoutManager = gridLayoutManagerRandom

        recyclerView = findViewById(R.id.rv_folder)
        gridLayoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = gridLayoutManager

        getMyData()
    }
    //sharedpreference
//    private fun prefer(){
//        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)
//        editor = sharePreferences.edit()
//    }

    private fun getMyData() {

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(APIInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse( call: Call<MyData?>,response: Response<MyData?>) {
                listStickers = response.body()!!.listStickers
                start_url = response.body()!!.start_url
                prefix = response.body()!!.prefix_

                myAdapter = MyAdapter(baseContext,listStickers,start_url,prefix,this@MainActivity)
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = myAdapter

                myAdapterRandom = MyAdapterRandom(baseContext,listStickers,start_url,prefix,this@MainActivity)
                myAdapterRandom.notifyDataSetChanged()
                recyclerViewRandom.adapter = myAdapterRandom

            }
            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Toast.makeText(this@MainActivity,"get data error",Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ListImgFolderActivity::class.java)
        val sticker = Gson().toJson(listStickers?.get(position))
        intent.putExtra("sticker",sticker)
        intent.putExtra("baseurl",start_url)
        intent.putExtra("prefix",prefix)
        startActivity(intent)
    }
}