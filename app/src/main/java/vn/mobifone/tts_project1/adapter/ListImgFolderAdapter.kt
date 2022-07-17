package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.apiInterface.ClickItem

class ListImgFolderAdapter(
    val context: Context,
    val startUrl:String?,
    val prefix: String?,
    val imageCount: Int,
    val folder:String?,
    val listener: ClickItem
):RecyclerView.Adapter<ListImgFolderAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener {
        var image: ImageView = itemView.findViewById(R.id.img_ListImg)
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
        var view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos = position.inc()
        val imageUrl:String?= startUrl + folder + "/" + folder + "_" + prefix + pos +".png"
//        e("a","data $imageUrl")
        Glide.with(context)
            .load(imageUrl)
            .override(100,100)
            .placeholder(R.drawable.loading)
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return imageCount
    }

}