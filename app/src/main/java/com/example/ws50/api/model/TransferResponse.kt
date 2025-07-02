package com.example.ws50.api.model

data class TransferResponse(
    val status: Int,
    val pesan: String,
    val data: Transfer?
)