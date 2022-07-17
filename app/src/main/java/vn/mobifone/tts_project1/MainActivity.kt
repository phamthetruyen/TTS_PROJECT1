package vn.mobifone.tts_project1

import android.content.Intent
import android.content.SharedPreferences
import android.net.IpPrefix
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
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.mobifone.tts_project1.util.Constants
import vn.mobifone.tts_project1.util.Constants.BASE_URL


class MainActivity : AppCompatActivity(), ClickItem {

    lateinit var myAdapter: MyAdapter
    lateinit var myAdapterRandom: MyAdapterRandom
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var gridLayoutManagerRandom: GridLayoutManager
    lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewRandom: RecyclerView

    var listStickers: ArrayList<Stickers>? = null
    var start_url: String? = null
    var prefix: String? = null


    private lateinit var sharePreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerViewRandom = findViewById(R.id.rv_folder_random)
        gridLayoutManagerRandom = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        recyclerViewRandom.setHasFixedSize(true)
        recyclerViewRandom.layoutManager = gridLayoutManagerRandom

        recyclerView = findViewById(R.id.rv_folder)
        gridLayoutManager = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = gridLayoutManager

        getMyData()
//        if(count == 0){
//            getMyData()
//        }else if(getDataCount()!! < 5 ){
//            var countData:Int = getDataCount()!!
//            countData++
//            putDataCount(countData)
//        }else if(getDataCount()!! >= 5){
//            count =0
//            putDataCount(count)
//            clearData()
//        }
    }

    private fun getMyData() {

        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(APIInterface::class.java)

        val retrofitData = retrofitBuilder.getData()

        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                listStickers = response.body()!!.listStickers
                start_url = response.body()!!.start_url
                prefix = response.body()!!.prefix_

                count++
                putData(start_url!!,prefix!!,listStickers!!,count)

                myAdapter =
                    MyAdapter(baseContext, listStickers, start_url, prefix, this@MainActivity)
                myAdapter.notifyDataSetChanged()
                recyclerView.adapter = myAdapter

                myAdapterRandom = MyAdapterRandom(baseContext, listStickers, start_url, prefix, this@MainActivity)
                myAdapterRandom.notifyDataSetChanged()
                recyclerViewRandom.adapter = myAdapterRandom
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
//                Toast.makeText(this@MainActivity,"get data error",Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun putData(
        start_url: String,
        prefix: String,
        respon: ArrayList<Stickers>,
        count: Int
    ) {
        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)
        editor = sharePreferences.edit()
        editor.putString(Constants.STARTURL, start_url)
        editor.putString(Constants.PREFIX, prefix)
        editor.putInt(Constants.COUNT, count)

        val json: String = Gson().toJson(respon)
        editor.putString(Constants.OBJECT, json)

        editor.apply()
        editor.commit()
    }

    private fun putDataCount(count: Int) {
        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)
        editor = sharePreferences.edit()

        editor.putInt(Constants.COUNT, count)

        editor.apply()
        editor.commit()
    }
    private fun clearData(){
        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)
        editor = sharePreferences.edit()
        editor.clear()
    }
    private fun getData():Triple<String?,String?,ArrayList<Stickers>?>{
        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)

        val start_urldata :String? = sharePreferences.getString(Constants.STARTURL,"")
        val prefixdata:String? = sharePreferences.getString(Constants.PREFIX,"")
        return Triple(start_urldata,prefixdata,listStickers)
    }

    private fun getDataCount():Int?{
        sharePreferences = this.getSharedPreferences(Constants.SHAREDPREFER, MODE_PRIVATE)
        var count:Int = sharePreferences.getInt(Constants.COUNT,0)
        return  count
    }
    inline fun <reified T> Gson.fromJson(json:String) = fromJson<ArrayList<Stickers>>(json,object :TypeToken<ArrayList<Stickers>>(){}.type)

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ListImgFolderActivity::class.java)
        val sticker = Gson().toJson(listStickers?.get(position))
        intent.putExtra(Constants.STICKER, sticker)
        intent.putExtra(Constants.STARTURL, start_url)
        intent.putExtra(Constants.PREFIX, prefix)
        startActivity(intent)
    }
}