package com.example.ws50.api.services

import com.example.ws50.api.model.TransferResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface TransferOrder {

    @GET("get-transfer-by-fields")
    fun getTransferByFields(
        @Query("warehouse") warehouse: String,
        @Query("toNumber") toNumber: String,
        @Query("toItem") toItem: String
    ): Call<TransferResponse>

    @PUT("update-pick")
    fun updatePickByFields(
        @Query("warehouse") warehouse: String,
        @Query("toNumber") toNumber: String,
        @Query("toItem") toItem: String,
        @Body qtyPick: Double?
    ): Call<TransferResponse>

    @PUT("update-confirm")
    fun updateConfirmByFields(
        @Query("warehouse") warehouse: String,
        @Query("toNumber") toNumber: String,
        @Query("toItem") toItem: String,
        @Body qtyConfirm: Double?
    ): Call<TransferResponse>
}
