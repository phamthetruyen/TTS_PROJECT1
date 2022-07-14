package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.interfaces.ClickItem
import vn.mobifone.tts_project1.model.Stickers


class RecyclerAdapter(
    val context: Context,
    val listStickers: ArrayList<Stickers>,
    val start_url: String,
    val prefix: String,
    var listener: ClickItem
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameFolder: TextView
        var img: ImageView

        init {
            nameFolder = itemView.findViewById(R.id.tvFolder)
            img = itemView.findViewById(R.id.imv)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapter.ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_folder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        var stt : Int=1
        val imageURL: String? =
            start_url + listStickers?.get(position)?.folder + "/" + listStickers?.get(position)?.folder + "_" + prefix + stt + ".png"
        Log.e("imageURL", "String" + imageURL)
        holder.nameFolder.text = listStickers[position].folder
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.load)
            .into(holder.img)
        holder.img.setOnClickListener {
            listener.onItemClick(position)
        }

    }


    override fun getItemCount(): Int {
        return listStickers.size
    }




}



