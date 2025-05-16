package com.luis.caiza.cazarpatos

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            // Abrir MainActivity al hacer clic
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
