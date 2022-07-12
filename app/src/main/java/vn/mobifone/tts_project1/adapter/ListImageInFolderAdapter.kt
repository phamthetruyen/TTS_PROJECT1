package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.util.Log.e
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R

class ListImageInFolderAdapter(
    val context: Context,
    val startUpUrl: String,
    val prefix: String,
    val imageCount: Int,
    val folder: String
) : RecyclerView.Adapter<ListImageInFolderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.list_img_folder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view =
            LayoutInflater.from(context).inflate(R.layout.list_image_folder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        for (i in 1..imageCount) {
        val pos = position.inc()
        val imageURL: String? =
            startUpUrl + folder + "/" + folder + "_" + prefix + pos + ".png"
        e("a", "dada $imageURL")
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.image)
//        }
    }

    override fun getItemCount(): Int {
        return imageCount
    }
}