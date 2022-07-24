package vn.mobifone.tts_project1.apiInterface

import retrofit2.Call
import retrofit2.http.GET
import vn.mobifone.tts_project1.model.MyData

interface APIInterface {
    @GET(value = "tts_project1.json")
    fun getData():Call<MyData>
}