package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class FirstActivity : AppCompatActivity() {

    private val delayMillis: Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@FirstActivity, SigningUpActivity::class.java)
            startActivity(intent)
            finish()
        }, delayMillis)
    }
}
