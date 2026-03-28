package com.artspire.mobile

import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)

        val btnCreate = findViewById<Button>(R.id.btnCreateAccount)
        val btnBack = findViewById<Button>(R.id.btnBackLogin)

        btnCreate.setOnClickListener {
            Toast.makeText(this, "Account created (placeholder)", Toast.LENGTH_SHORT).show()
        }
        btnBack.setOnClickListener {
            finish()
        }
    }
}