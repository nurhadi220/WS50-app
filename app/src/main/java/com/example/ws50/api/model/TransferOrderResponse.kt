package com.example.ws50.api.model

import java.math.BigDecimal

data class TransferOrderResponse(
    val toNumber: String,
    val material: String,
    val sBin: String,
    val quantity: BigDecimal,
    val isSuccessful: Boolean
)