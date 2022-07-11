package vn.mobifone.tts_project1.model

data class Stickers(
    val folder: String,
    val icon: String,
    val locked: Boolean,
    val name_en: String,
    val name_vi: String,
    val size_free: Int,
    val totalImage: Int
)