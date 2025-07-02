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

class EditConfirmActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_confirm)

        // Ambil data dari intent
        val transferData = intent.getParcelableExtra<Transfer>("transfer_data")

        // Inisialisasi view
        val sLocToNumberToItem: TextView = findViewById(R.id.sLoc_toNumber_toItem)
        val sBinTextView: TextView = findViewById(R.id.textsBin)
        val qtyEditText: EditText = findViewById(R.id.qty)
        val confirmButton: Button = findViewById(R.id.submit)

        // Cek apakah transferData ada
        if (transferData != null && !transferData.toNumber.isNullOrBlank()) {
            // Isi view dengan datanya
            updateViewWithTransferData(transferData)
        } else {
            Toast.makeText(this, "Gagal Mendapatkan Data", Toast.LENGTH_SHORT).show()
        }

        // Set onClickListener untuk tombol Confirm
        confirmButton.setOnClickListener {
            val qtyInput = qtyEditText.text.toString()

            if (qtyInput.isNotEmpty()) {
                // Panggil API untuk update confirm
                val qtyConfirm = qtyInput.toDoubleOrNull()

                if (qtyConfirm != null && transferData != null && !transferData.toNumber.isNullOrBlank() && !transferData.toItem.isNullOrBlank()) {
                    // Panggil API dengan parameter warehouse, toNumber, toItem, dan qtyConfirm
                    ApiClient.transferServices.updateConfirmByFields(transferData.warehouse!!, transferData.toNumber!!, transferData.toItem!!, qtyConfirm)
                        .enqueue(object : Callback<TransferResponse> {
                            override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                                if (response.isSuccessful) {
                                    val transfer = response.body()?.data
                                    Log.d("API_RESPONSE", "Transfer: $transfer")
                                    Toast.makeText(this@EditConfirmActivity, "Data Telah di Konfirmasi", Toast.LENGTH_SHORT).show()
                                    finish() // Kembali ke aktivitas sebelumnya setelah berhasil
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

    private fun updateViewWithTransferData(transferData: Transfer) {
        val sLocToNumberToItem: TextView = findViewById(R.id.sLoc_toNumber_toItem)
        val sBinTextView: TextView = findViewById(R.id.textsBin)

        // Hapus karakter pertama dari storageLocation
        val storageLocation = transferData.storageLocation?.drop(1) ?: ""

        // Hapus angka nol di depan toItem, kecuali jika hanya angka "1"
        val toItem = transferData.toItem?.let {
            if (it.startsWith("0") && it.length > 1) {
                it.trimStart('0')
            } else {
                it
            }
        } ?: ""

        sLocToNumberToItem.text = "$storageLocation${transferData.toNumber ?: ""}/$toItem"
        sBinTextView.text = transferData.storageBin
    }
}
