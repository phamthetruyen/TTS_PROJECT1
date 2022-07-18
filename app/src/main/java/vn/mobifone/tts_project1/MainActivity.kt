package vn.mobifone.tts_project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    lateinit var gridLayoutManagerRandomFolder: GridLayoutManager
    lateinit var listFolderRecyclerView: RecyclerView
    lateinit var randomFolderRecyclerView: RecyclerView

    var listStickers : ArrayList<Stickers>?= null
    var startUrl : String?= null
    var prefix : String?= null

    private lateinit var sharedPref : SharedPreferences
    private lateinit var editor : SharedPreferences.Editor
    private var count : Int = 0

    private val retrofitBuilder = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Constants.BASE_URL)
        .build()
        .create(ApiService::class.java)

    private val retrofitData = retrofitBuilder.getData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = this.getSharedPreferences(Constants.NAMEPREF, MODE_PRIVATE)
        editor = sharedPref.edit()

        initView()
        refreshCache()
    }

    private fun initView() {
        listFolderRecyclerView = findViewById(R.id.list_folder_recycle)
        listFolderRecyclerView.setHasFixedSize(true)

        randomFolderRecyclerView = findViewById(R.id.random_folder_recycle)
        randomFolderRecyclerView.setHasFixedSize(true)

        gridLayoutManagerListFolder = GridLayoutManager(this, 1, RecyclerView.HORIZONTAL, false)
        listFolderRecyclerView.layoutManager = gridLayoutManagerListFolder

        gridLayoutManagerRandomFolder = GridLayoutManager(this, 3, RecyclerView.VERTICAL, false)
        randomFolderRecyclerView.layoutManager = gridLayoutManagerRandomFolder
    }

    private fun refreshCache() {
        if (count == 0 && hasNetwork(this) == true) {
            getData()
        }
        else if (sharedPref.getInt(Constants.COUNT, 0) < 5 && hasNetwork(this) == false) {
            recycleEvent(getItemFromCache().first, getItemFromCache().second!!, getItemFromCache().third!!)
            var tmpCount = sharedPref.getInt(Constants.COUNT, 0)
            tmpCount++
            editor.putInt(Constants.COUNT, tmpCount)
        } else if (sharedPref.getInt(Constants.COUNT, 0) >= 5) {
            count = 0;
            editor.putInt(Constants.COUNT, count)
            editor.clear()
        }
        editor.apply()
    }

    private fun logCache(list_Sticker_Cache : ArrayList<Stickers>, start_url_cache : String, prefix_cache : String, count_cache : Int) {
        val listStickerToJson = Gson().toJson(list_Sticker_Cache)
        editor.putString(Constants.LISTSTICKERS, listStickerToJson)
        editor.putString(Constants.START_URL, start_url_cache)
        editor.putString(Constants.PREFIX, prefix_cache)
        editor.putInt(Constants.COUNT, count_cache)
        editor.apply()
    }

    private fun getItemFromCache() : Triple<ArrayList<Stickers>, String?, String?> {
        val stickersFromCache = sharedPref.getString(Constants.LISTSTICKERS, "")
        val startUrlFromCache = sharedPref.getString(Constants.START_URL, "")
        val prefixFromCache = sharedPref.getString(Constants.PREFIX, "")
        val stickers = Gson().fromJson<Stickers>(stickersFromCache!!)
        return Triple(stickers, startUrlFromCache, prefixFromCache)
    }

    private fun getData() {
        retrofitData.enqueue(object : Callback<Data?> {
            override fun onResponse(call: Call<Data?>, response: Response<Data?>) {
                val responseBody = response.body()!!

                listStickers = responseBody.listStickers
                startUrl = responseBody.start_url
                prefix = responseBody.prefix_

                count++
                logCache(listStickers!!, startUrl!!, prefix!!, count)
                recycleEvent(getItemFromCache().first, getItemFromCache().second!!, getItemFromCache().third!!)
            }

            override fun onFailure(call: Call<Data?>, t: Throwable) {
                e("Error", "Error when get data from API")
                Toast.makeText(this@MainActivity, "Loi khi lay data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun recycleEvent(response_List_Stickers : ArrayList<Stickers>, response_start_url : String, response_prefix : String) {
        stickersAdapter = ListFolderAdapter(baseContext, response_List_Stickers, response_start_url, response_prefix, this@MainActivity)
        stickersAdapter.notifyDataSetChanged()
        listFolderRecyclerView.adapter = stickersAdapter

        randomStickersAdapter = RandomFolderAdapter(baseContext, response_List_Stickers!!, response_start_url, response_prefix, this@MainActivity)
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

    private fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }

    inline fun <reified T> Gson.fromJson(json: String) = fromJson<ArrayList<Stickers>>(json, object : TypeToken<ArrayList<Stickers>>(){}.type)
}