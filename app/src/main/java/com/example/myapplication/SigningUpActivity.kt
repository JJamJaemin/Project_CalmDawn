package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SigningUpActivity : AppCompatActivity(), CustomDialog.CustomDialogListener  {

    private var login: Button?=null
    private var join: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signingup)
        login = findViewById(R.id.login)
        join = findViewById(R.id.join)

        login?.setOnClickListener {
            showCustomDialog()
        }
        join?.setOnClickListener {
            val intent = Intent(this@SigningUpActivity, JoinActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
    override fun onLoginClicked(id: String, password: String) {
        val intent = Intent(this@SigningUpActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onLoginSuccess() {
    }

    private fun showCustomDialog() {
        val customDialog = CustomDialog(this)
        customDialog.setDialogListener(this)
        customDialog.show()
    }
}
