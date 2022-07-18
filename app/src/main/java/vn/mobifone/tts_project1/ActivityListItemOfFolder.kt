package vn.mobifone.tts_project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofit.util.Constan
import com.google.gson.Gson
import vn.mobifone.tts_project1.adapter.RecyclerAdapterImg
import vn.mobifone.tts_project1.interfaces.ClickItem
import vn.mobifone.tts_project1.model.Stickers

class ActivityListItemOfFolder : AppCompatActivity(), ClickItem {
    lateinit var rcv : RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var recyclerAdapterImg: RecyclerAdapterImg
    var ob :String?=null
    var prefix : String?=null
    var start_url : String?=null
    var folder : String?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_item_of_folder)
        rcv = findViewById(R.id.rcv2)
        rcv.setHasFixedSize(true)

        linearLayoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL,false)
        rcv.layoutManager = linearLayoutManager

        getData()

    }

    private fun getData() {

        val intent = getIntent()

        ob = intent.getStringExtra(Constan.OBJECT)
        val icon = Gson().fromJson(ob, Stickers::class.java)

        prefix = intent.getStringExtra(Constan.PREFIX)
        start_url = intent.getStringExtra(Constan.START_URL)
        folder = intent.getStringExtra(Constan.FOLDER)

        recyclerAdapterImg = RecyclerAdapterImg(this,start_url!!,prefix!!,icon.folder,icon.totalImage,icon.name_en,icon.name_vi,this)
        recyclerAdapterImg.notifyDataSetChanged()
        rcv.adapter = recyclerAdapterImg
    }

    override fun onItemClick(position: Int) {

        val intent = Intent(this,ActivityListDetailItem::class.java)
        intent.putExtra(Constan.OBJECT,ob)
        intent.putExtra(Constan.PREFIX,prefix)
        intent.putExtra(Constan.START_URL,start_url)
        intent.putExtra(Constan.FOLDER,folder)
        intent.putExtra(Constan.POSITION,position.inc())
//        Log.e("bb", "onItemClick: ${position.inc()}", )
        startActivity(intent)

    }
}