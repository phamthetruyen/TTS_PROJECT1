package vn.mobifone.tts_project1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.util.Constan
import com.google.gson.Gson
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

        preference()

        rcvFolder = findViewById(R.id.rcvFolder)
        rcvRandom = findViewById(R.id.randomFolder)

        rcvFolder.setHasFixedSize(true)

        gridLayoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
        rcvFolder.layoutManager = gridLayoutManager
        linearLayout = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rcvRandom.layoutManager = linearLayout



        getMyData()

    }

    private fun preference() {

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

                // preference
                putData(start_url,prefix!!,responebody!!)

//                sharedPreferences = getSharedPreferences(Constan.SHAREPREF, MODE_PRIVATE)
//                preferencesEdt = sharedPreferences.edit()
//                preferencesEdt.putString()


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

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
            }
        })
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

    fun putData(start_url :String,prefix :String, respon : ArrayList<Stickers>){

        sharedPreferences = this.getSharedPreferences(Constan.SHAREPREF, MODE_PRIVATE)
        preferencesEdt = sharedPreferences.edit()

        preferencesEdt.putString(Constan.START_URL,start_url)
        preferencesEdt.putString(Constan.PREFIX,prefix)
        preferencesEdt.putString(Constan.OBJECT,respon.toString())

        preferencesEdt.apply()
        preferencesEdt.commit()

    }
    fun setFirstInstallApp(isFirst :Boolean){

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