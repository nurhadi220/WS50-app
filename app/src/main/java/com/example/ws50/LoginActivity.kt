package com.example.ws50

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inisialisasi view
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)

        // TextWatcher ke kedua EditText
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Cek apakah kedua field sudah terisi
                val username = usernameEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    performLogin(username, password)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        // Pasangkan TextWatcher pada EditText
        usernameEditText.addTextChangedListener(textWatcher)
        passwordEditText.addTextChangedListener(textWatcher)
    }

    // Fungsi untuk melakukan login
    private fun performLogin(username: String, password: String) {
        if (username == "admin" && password == "admin") {
            // Jika username dan password benar, arahkan ke MainActivity
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish() // Tutup LoginActivity agar tidak bisa kembali dengan tombol back
        } else {
            // Tampilkan pesan kesalahan jika login gagal
            Toast.makeText(this@LoginActivity, "Username atau Password salah!", Toast.LENGTH_SHORT).show()
        }
    }
}
