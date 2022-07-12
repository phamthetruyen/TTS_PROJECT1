package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.onItemClickInterface.OnItemClickListener
import vn.mobifone.tts_project1.model.Stickers

class StickersAdapter(
    val context: Context,
    val listStickers: List<Stickers>?,
    val start_url: String?,
    val prefix_: String?,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StickersAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var image: ImageView = itemView.findViewById(R.id.folder_img)
        var foldername: TextView = itemView.findViewById(R.id.folder_name)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.folder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageURL: String? =
            start_url + listStickers?.get(position)?.folder + "/" + listStickers?.get(position)?.folder + "_" + prefix_ + "1" + ".png"
//        Log.e("imageURL", "String" + imageURL)
        holder.foldername.text = listStickers?.get(position)?.folder.toString()
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return listStickers?.size!!
    }
}

