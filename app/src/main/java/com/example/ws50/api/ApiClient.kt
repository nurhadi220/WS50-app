package com.example.ws50.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.ws50.api.services.TransferOrder

object ApiClient {

    private const val BASE_URL = "http://192.168.54.176:5000/warehouse-api/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val transferServices: TransferOrder by lazy {
        retrofit.create(TransferOrder::class.java)
    }

}
