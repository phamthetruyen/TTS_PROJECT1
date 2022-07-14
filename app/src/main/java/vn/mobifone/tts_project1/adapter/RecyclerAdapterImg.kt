package vn.mobifone.tts_project1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import vn.mobifone.tts_project1.MainActivity2
import vn.mobifone.tts_project1.R
import vn.mobifone.tts_project1.interfaces.ClickItem
import java.util.*


class RecyclerAdapterImg(
    val context: Context,
    val start_url: String,
    val prefix: String,
    val folder: String,
    val imgCount: Int,
    val nameE: String,
    val nameV: String,
    val listener: ClickItem
) : RecyclerView.Adapter<RecyclerAdapterImg.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterImg.ViewHolder {
        var itemView1 = LayoutInflater.from(context).inflate(R.layout.item_img, parent, false)
        return ViewHolder(itemView1)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgIcon: ImageView
        var nameIcon: TextView
        //       var img : ImageView
        //        var ngonNgu : TextView

        init {
            imgIcon = itemView.findViewById(R.id.img_icon)
            nameIcon = itemView.findViewById(R.id.nameimg)
            //   img = itemView.findViewById(R.id.imgv)
//            ngonNgu = itemView.findViewById(R.id.NgonNgu)
        }
    }

    override fun onBindViewHolder(holder: RecyclerAdapterImg.ViewHolder, position: Int) {

        val positions = position
        val url: String? =
            start_url + folder + "/" + folder + "_" + prefix + (positions + 1) + ".png"
        Glide.with(context)
            .load(url)
            .override(100, 100)
            .placeholder(R.drawable.load)
            .into(holder.imgIcon)
        if (Locale.getDefault().language == "en") {
            holder.nameIcon.text = nameE
        } else {
            holder.nameIcon.text = nameV
        }
//        holder.nameIcon.text=
        holder.imgIcon.setOnClickListener {
            listener.onItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return imgCount
    }

}