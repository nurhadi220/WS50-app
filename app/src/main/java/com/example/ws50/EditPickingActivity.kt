package com.example.ws50

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ws50.api.ApiClient
import com.example.ws50.api.model.Transfer
import com.example.ws50.api.model.TransferResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPickingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_picking)

        // Ambil data dari intent
        val transferData = intent.getParcelableExtra<Transfer>("transfer_data")

        // Inisialisasi view
        val sLocToNumberToItem: TextView = findViewById(R.id.sLoc_toNumber_toItem)
        val materialTextView: TextView = findViewById(R.id.textmaterial)
        val sBinTextView: TextView = findViewById(R.id.textsBin)
        val qtyEditText: EditText = findViewById(R.id.qty)
        val pickButton: Button = findViewById(R.id.submit)

        // Cek apakah transferData ada
        if (transferData != null && !transferData.toNumber.isNullOrBlank()) {
            // Isi view dengan datanya
            fetchTransferDetails(transferData.warehouse, transferData.toNumber, transferData.toItem)
        } else {
            Toast.makeText(this, "Gagal Mendapatkan Data", Toast.LENGTH_SHORT).show()
        }

        // Set onClickListener untuk tombol Pick
        pickButton.setOnClickListener {
            val qtyInput = qtyEditText.text.toString()

            if (qtyInput.isNotEmpty()) {
                // Panggil API untuk update picking
                val qtyPick = qtyInput.toDoubleOrNull()

                if (qtyPick != null && transferData != null && !transferData.toNumber.isNullOrBlank() && !transferData.toItem.isNullOrBlank() && !transferData.warehouse.isNullOrBlank()) {
                    // Panggil API dengan parameter warehouse, toNumber, toItem, dan qtyPick
                    ApiClient.transferServices.updatePickByFields(transferData.warehouse!!, transferData.toNumber!!, transferData.toItem!!, qtyPick)
                        .enqueue(object : Callback<TransferResponse> {
                            override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                                if (response.isSuccessful) {
                                    val transfer = response.body()?.data
                                    Log.d("API_RESPONSE", "Transfer: $transfer")
                                    Toast.makeText(this@EditPickingActivity, "Berhasil Menambahkan Data", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Log.d("API_RESPONSE", "Failed with status: ${response.code()}")
                                }
                            }

                            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                                Log.e("API_RESPONSE", "Error: ${t.message}")
                            }
                        })
                } else {
                    Toast.makeText(this, "Data atau Quantity tidak valid", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Harap masukkan Qty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchTransferDetails(warehouse: String?, toNumber: String?, toItem: String?) {
        if (warehouse.isNullOrBlank() || toNumber.isNullOrBlank() || toItem.isNullOrBlank()) {
            Toast.makeText(this, "Data is invalid", Toast.LENGTH_SHORT).show()
            return
        }

        ApiClient.transferServices.getTransferByFields(warehouse, toNumber, toItem)
            .enqueue(object : Callback<TransferResponse> {
                override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                    if (response.isSuccessful) {
                        val transfer = response.body()?.data
                        transfer?.let {
                            // Update TextViews dengan data dari API
                            val sLocToNumberToItem: TextView = findViewById(R.id.sLoc_toNumber_toItem)
                            val materialTextView: TextView = findViewById(R.id.textmaterial)
                            val sBinTextView: TextView = findViewById(R.id.textsBin)

                            // Hilangkan huruf pertama dari storageLocation jika ada
                            val storageLocation = it.storageLocation?.drop(1) ?: ""

                            // Hilangkan angka 0 di depan dari toItem, tapi pertahankan angka 1
                            val toItem = it.toItem?.replaceFirst("^0+(?!$)".toRegex(), "") ?: ""

                            sLocToNumberToItem.text = "${storageLocation}${it.toNumber ?: ""}/ $toItem"
                            materialTextView.text = it.material
                            sBinTextView.text = it.storageBin
                        }
                    } else {
                        Log.d("API_RESPONSE", "Failed to fetch details with status: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                    Log.e("API_RESPONSE", "Error: ${t.message}")
                }
            })
    }
}
