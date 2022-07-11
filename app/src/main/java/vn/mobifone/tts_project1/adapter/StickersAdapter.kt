package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.model.Data
import vn.mobifone.tts_project1.model.Stickers

class StickersAdapter(val context: Context, val listStickers: List<Stickers>?) : RecyclerView.Adapter<StickersAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.folder_img)
        var foldername: TextView = itemView.findViewById(R.id.folder_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.foldername.text = listStickers?.get(position)?.folder.toString()
    }

    override fun getItemCount(): Int {
        return listStickers?.size!!
    }
}

