package vn.mobifone.tts_project1.adapter

import android.content.Context

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.apiInterface.ClickItem
import vn.mobifone.tts_project1.model.Stickers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class MyAdapter(val context: Context,
                val stickers: List<Stickers>?,
                val start_url:String?,
                val prefix: String?,
                private var listener: ClickItem):RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var folder: TextView =itemView.findViewById(R.id.nameFolder)
        var image : ImageView=itemView.findViewById(R.id.img_Folder)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION){
                listener.onItemClick(position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_folder,parent,false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val imageURL: String? = start_url + stickers?.get(position)?.folder + "/" + stickers?.get(position)?.folder + "_" + prefix + "1" + ".png"

        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.loading)
            .into(holder.image)
        if(Locale.getDefault().language=="en"){
            holder.folder.text = stickers?.get(position)?.name_en.toString()
        }else  holder.folder.text = stickers?.get(position)?.name_vi.toString()
    }

    override fun getItemCount(): Int {
        return stickers?.size!!
    }
}