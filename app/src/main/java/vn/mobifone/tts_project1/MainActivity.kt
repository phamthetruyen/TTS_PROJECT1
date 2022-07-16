package vn.mobifone.tts_project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.util.Constan
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vn.mobifone.tts_project1.adapter.AdapterRandom
import vn.mobifone.tts_project1.adapter.RecyclerAdapter
import vn.mobifone.tts_project1.api.ApiInterFace
import vn.mobifone.tts_project1.interfaces.ClickItem
import vn.mobifone.tts_project1.model.MyData
import vn.mobifone.tts_project1.model.Stickers
import java.lang.reflect.Type


const val BASE_URL = "https://mystoragetm.s3.ap-southeast-1.amazonaws.com/"
class MainActivity : AppCompatActivity(),ClickItem {
    lateinit var adapter: RecyclerAdapter
    lateinit var adapterRandom: AdapterRandom
    lateinit var gridLayoutManager: GridLayoutManager
    lateinit var linearLayout: LinearLayoutManager
    lateinit var rcvFolder: RecyclerView
    lateinit var rcvRandom: RecyclerView
    lateinit var imgFolder: ImageView

    lateinit var sharedPreferences: SharedPreferences
    //lateinit var data :
    lateinit var preferencesEdt: SharedPreferences.Editor
    var countOpenApp :Int = 0

    var responebody: ArrayList<Stickers>? = null
    var prefix: String? = null
    var start_url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rcvFolder = findViewById(R.id.rcvFolder)
        rcvRandom = findViewById(R.id.randomFolder)

        rcvFolder.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rcvFolder.layoutManager = gridLayoutManager
        linearLayout = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rcvRandom.layoutManager = linearLayout


//        if(hasNetwork(this) == true){
//            getMyData()
//        }else{
//            println("aaaaaaaaa")
//            adapterView(getData().third!!,getData().first!!,getData().second!!)
//        }




        if(countOpenApp == 0 && hasNetwork(this) == true){
           getMyData()
            Log.d("aaaaaaaaaa", "onCreate: $countOpenApp")
           // println(hasNetwork(this))
        }
        else if(getDataCount()!! < 5 && hasNetwork(this) == false){
            adapterView(getData().third!!,getData().first!!,getData().second!!)
            var count1:Int = getDataCount()!!
            count1++
            Log.d("bbbbbbbbbb", "onCreate: ${getDataCount()}" )
            Log.d("bbbbbbbbbb", "onCreate: $count1" )
            putDataCount(count1)
        }else if(getDataCount()!! >= 5){
            countOpenApp = 0
            putDataCount(countOpenApp)
            Log.d("cccc", "clear ")
            clearData()
        }

      //  getMyData()

    }



    private fun getMyData() {


        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            // cache
            //.client(okHttpClient)

            .baseUrl(BASE_URL)
            .build()
            .create(ApiInterFace::class.java)


        val retrofitData = retrofitBuilder.getData()
        retrofitData.enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {

                responebody = response.body()!!.listStickers
                start_url = response.body()!!.start_url
                prefix = response.body()!!.prefix_

                countOpenApp++
                putData(start_url,prefix!!,responebody!!,countOpenApp)

                adapterView(getData().third!!,getData().first!!,getData().second!!)
//                if(countOpenApp == 0){
//                    putData(start_url,prefix!!,responebody!!)
//                    countOpenApp++
//                    adapterView(getData().third!!,getData().first!!,getData().second!!)
//                }
//                else if(countOpenApp < 5){
//                    adapterView(getData().third!!,getData().first!!,getData().second!!)
//                    countOpenApp++
//                }else{
//                    countOpenApp = 0
//                    clearData()
//                }

            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
            }
        })
    }

    fun behavior(){

    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, MainActivity2::class.java)

        val ob = Gson().toJson(responebody?.get(position))
        intent.putExtra("object", ob)

        intent.putExtra("folder", responebody)
        intent.putExtra("start_url", start_url)
        intent.putExtra("prefix", prefix)
        intent.putExtra("folder", responebody?.get(position)?.folder)

        startActivity(intent)
    }

    fun putData(start_url :String,prefix :String, respon : ArrayList<Stickers>, count : Int){

        sharedPreferences = this.getSharedPreferences(Constan.SHARE_PREFERENCE, MODE_PRIVATE)
        preferencesEdt = sharedPreferences.edit()

        preferencesEdt.putString(Constan.START_URL,start_url)
        preferencesEdt.putString(Constan.PREFIX,prefix)
        preferencesEdt.putInt(Constan.COUNT,count)

        val json : String =Gson().toJson(respon)
        preferencesEdt.putString(Constan.OBJECT, json)
     //   preferencesEdt.putString(Constan.OBJECT,respon.toString())

        preferencesEdt.apply()
        preferencesEdt.commit()

    }

    fun putDataCount(count : Int){

        sharedPreferences = this.getSharedPreferences(Constan.SHARE_PREFERENCE, MODE_PRIVATE)
        preferencesEdt = sharedPreferences.edit()

        preferencesEdt.putInt(Constan.COUNT,count)


        preferencesEdt.apply()
        preferencesEdt.commit()

    }
    fun getData() : Triple<String?,String?,ArrayList<Stickers>?>{
        sharedPreferences = this.getSharedPreferences(Constan.SHARE_PREFERENCE, MODE_PRIVATE)

        val start_url1 :String? = sharedPreferences.getString(Constan.START_URL,"")
        val prefix1:String? =   sharedPreferences.getString(Constan.PREFIX,"")


        val listItem :String? = sharedPreferences.getString(Constan.OBJECT,"")
        val stickers = Gson().fromJson<Stickers>(listItem!!)
        return Triple(start_url1,prefix1,stickers)
    }
    fun getDataCount() :Int?{
        sharedPreferences = this.getSharedPreferences(Constan.SHARE_PREFERENCE, MODE_PRIVATE)

        var count:Int = sharedPreferences.getInt(Constan.COUNT,0)

        return count
    }


    inline fun <reified T> Gson.fromJson(json: String) = fromJson<ArrayList<Stickers>>(json, object: TypeToken<ArrayList<Stickers>>() {}.type)

    fun adapterView(responebody: ArrayList<Stickers>, start_url: String, prefix: String){
        adapter = RecyclerAdapter(
            this@MainActivity,
            responebody!!, start_url, prefix!!, this@MainActivity
        )
        adapterRandom = AdapterRandom(
            this@MainActivity,
            responebody!!,
            start_url,
            prefix!!,
            this@MainActivity
        )
        adapter.notifyDataSetChanged()
        adapterRandom.notifyDataSetChanged()

        rcvFolder.adapter = adapter
        rcvRandom.adapter = adapterRandom
    }

    fun clearData(){
        sharedPreferences = this.getSharedPreferences(Constan.SHARE_PREFERENCE, MODE_PRIVATE)
        preferencesEdt = sharedPreferences.edit()

        preferencesEdt.clear()
    }

    fun hasNetwork(context: Context): Boolean? {
        var isConnected: Boolean? = false // Initial Value
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }


}