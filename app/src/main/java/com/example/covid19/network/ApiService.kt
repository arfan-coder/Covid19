package com.example.covid19.network

import com.example.covid19.model.InfoNegara
import retrofit2.Call
import com.example.covid19.model.ResponseCountry
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("summary")
    fun getAllNegara(): Call<ResponseCountry>
}

interface InfoService {
    @GET fun getInfoService(@Url url: String?) : Call<List<InfoNegara>>
}
object RetrofitBuilder{
    private val okhttp= OkHttpClient().newBuilder()
        .connectTimeout(12, TimeUnit.SECONDS)
        .readTimeout(12,TimeUnit.SECONDS)
        .writeTimeout(12, TimeUnit.SECONDS)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.covid19api.com/")
        .client(okhttp)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}