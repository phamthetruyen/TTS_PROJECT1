package vn.mobifone.tts_project1.api

import retrofit2.Call
import retrofit2.http.GET
import vn.mobifone.tts_project1.model.Data
import vn.mobifone.tts_project1.model.Stickers

interface ApiService {

    @GET("tts_project1.json")
    fun getData(): Call<Data>
}