package vn.mobifone.tts_project1.model

import android.os.Parcelable

data class Data(
    val listStickers: ArrayList<Stickers>,
    val prefix_: String,
    val start_url: String,
    val time_ads: Int
)