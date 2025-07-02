package com.example.ws50

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ws50.api.ApiClient
import com.example.ws50.api.model.TransferResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inisialisasi view
        val toFieldInput: EditText = findViewById(R.id.toFieldInput)

        // Tambahkan TextWatcher ke input field
        toFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val toField = s.toString()

                if (toField.isNotEmpty()) {
                    // Misalkan format input adalah "CKG2005/010"
                    val regex = Regex("([A-Za-z]+)(\\d+)/(\\d+)")
                    val matchResult = regex.matchEntire(toField)

                    if (matchResult != null) {
                        // Pisahkan input menjadi warehouse, toNumber, dan toItem
                        val (warehouse, toNumber, toItem) = matchResult.destructured

                        // Memanggil API dengan field yang dipisahkan
                        ApiClient.transferServices.getTransferByFields(warehouse, toNumber, toItem).enqueue(object : Callback<TransferResponse> {
                            override fun onResponse(call: Call<TransferResponse>, response: Response<TransferResponse>) {
                                if (response.isSuccessful && response.body()?.status == 200) {
                                    val transferData = response.body()?.data
                                    if (transferData != null) {
                                        when {
                                            transferData.qtyConfirm != null && transferData.qtyConfirm > 0 && transferData.confirm == true -> {
                                                // Tampilkan AlertDialog kustom jika qtyConfirm lebih dari 0 dan confirm bernilai true
                                                showCustomAlertDialog()
                                            }
                                            transferData.qtyPick != null && transferData.qtyPick > 0 && transferData.pick == true -> {
                                                val intent = Intent(this@MainActivity, EditConfirmActivity::class.java)
                                                intent.putExtra("transfer_data", transferData)
                                                startActivity(intent)
                                            }
                                            else -> {
                                                val intent = Intent(this@MainActivity, EditPickingActivity::class.java)
                                                intent.putExtra("transfer_data", transferData)
                                                startActivity(intent)
                                            }
                                        }
                                    }
                                } else {
                                    // Data tidak ditemukan
                                    showShorterToast("Data tidak ditemukan", 500)
                                }
                            }

                            override fun onFailure(call: Call<TransferResponse>, t: Throwable) {
                                // Gagal menghubungi server
                                showShorterToast("Gagal menghubungi server", 500)
                            }
                        })
                    } else {
                        // Format input salah
                        showShorterToast("Format input salah", 500)
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Tidak perlu digunakan
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Tidak perlu digunakan
            }
        })
    }

    // Fungsi untuk menampilkan Toast dengan durasi yang disesuaikan
    private fun showShorterToast(message: String, durationInMillis: Long) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.show()

        // Menggunakan Handler untuk membatalkan Toast setelah durasi yang disesuaikan
        Handler(Looper.getMainLooper()).postDelayed({
            toast.cancel()
        }, durationInMillis)
    }

    private fun showCustomAlertDialog() {
        // Inflate layout XML untuk AlertDialog
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_success, null)

        // Buat AlertDialog
        val alertDialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Temukan Button di dalam layout dan tambahkan action
        dialogView.findViewById<Button>(R.id.oke).setOnClickListener {

            findViewById<EditText>(R.id.toFieldInput).text.clear()
            alertDialog.dismiss()
        }

        // Tampilkan dialog
        alertDialog.show()
    }
}
