package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.database.DataSetObserver
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.interfaces.ClickItem
import vn.mobifone.tts_project1.model.Stickers

class AdapterRandom(val context: Context,
                    val listStickers: ArrayList<Stickers>,
                    val start_url: String,
                    val prefix: String,
                    var listener: ClickItem
): RecyclerView.Adapter<AdapterRandom.ViewHolder>(), Adapter {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameFolder: TextView
        var img: ImageView

        init {
            nameFolder = itemView.findViewById(R.id.tvFolderrd)
            img = itemView.findViewById(R.id.imvrd)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):AdapterRandom.ViewHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_random_folder, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: AdapterRandom.ViewHolder, position: Int) {
        val rnds = (1 until  listStickers.size).random()
        Log.e("vvv", "onBindViewHolder: $rnds" )
        val imageURL: String? =
            start_url + listStickers?.get(rnds)?.folder + "/" + listStickers?.get(rnds)?.folder + "_" + prefix + "1" + ".png"
        holder.nameFolder.text = listStickers[rnds].folder
        Glide.with(context)
            .load(imageURL)
            .override(100, 100)
            .placeholder(R.drawable.load)
            .into(holder.img)
        holder.img.setOnClickListener {
            listener.onItemClick(rnds)
        }
    }

    override fun getItemCount(): Int {
        return 5
    }

    override fun registerDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun unregisterDataSetObserver(p0: DataSetObserver?) {
        TODO("Not yet implemented")
    }

    override fun getCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getItem(p0: Int): Any {
        TODO("Not yet implemented")
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        TODO("Not yet implemented")
    }

    override fun getViewTypeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun isEmpty(): Boolean {
        TODO("Not yet implemented")
    }


}