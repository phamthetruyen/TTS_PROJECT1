package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.util.Log.e
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
import kotlin.random.Random

class RandomFolderAdapter(
    val context: Context,
    val listStickers: ArrayList<Stickers>,
    val start_url: String?,
    val prefix_: String?,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RandomFolderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var image: ImageView = itemView.findViewById(R.id.random_folder_img)
        var foldername: TextView = itemView.findViewById(R.id.random_folder_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.random_folder_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        var tmpValue : Int = Random.nextInt(listStickers!!.size - 1) + 1
        var tmpValue : Int = (1 until listStickers!!.size).random()
        e("tmp valie", " $tmpValue")
        val imageURL: String? =
            start_url + listStickers?.get(tmpValue)?.folder + "/" + listStickers?.get(tmpValue)?.folder + "_" + prefix_ + "1" + ".png"
        holder.foldername.text = listStickers?.get(tmpValue)?.folder.toString()
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.ic_launcher_background)
            .into(holder.image)
        holder.image.setOnClickListener {
            listener.onItemClick(tmpValue)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}

