package vn.mobifone.tts_project1.api


import vn.mobifone.tts_project1.model.MyData
import vn.mobifone.tts_project1.model.Stickers
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterFace {

    @GET("tts_project1.json")
    fun getData(): Call<MyData>
}