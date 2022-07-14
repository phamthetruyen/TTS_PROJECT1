package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.apiInterface.ClickItem
import vn.mobifone.tts_project1.model.Stickers


class MyAdapterRandom(val context: Context,
                      val stickers: List<Stickers>?,
                      val start_url:String?,
                      val prefix: String?,
                      private var listener: ClickItem):RecyclerView.Adapter<MyAdapterRandom.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var folderRd: TextView =itemView.findViewById(R.id.txt_Random)
        var imageRd : ImageView=itemView.findViewById(R.id.img_FolderRandom)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var itemViewRd = LayoutInflater.from(context).inflate(R.layout.item_folder_random,parent,false)
        return ViewHolder(itemViewRd)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tmpRandom = (1 until stickers!!.size).random()
        val imageURL: String? = start_url + stickers?.get(tmpRandom)?.folder+ "/" + stickers?.get(tmpRandom)?.folder + "_" + prefix + "1" + ".png"
        Log.e("imageURL", "String" + imageURL)
        holder.folderRd.text = stickers?.get(tmpRandom)?.folder.toString()
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.loading)
            .into(holder.imageRd)
        holder.imageRd.setOnClickListener { listener.onItemClick(tmpRandom) }
    }

    override fun getItemCount(): Int {
        return 9
    }
}