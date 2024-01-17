package com.example.myapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.myapplication.db.DBHelper
import com.google.android.material.checkbox.MaterialCheckBox



class JoinActivity : AppCompatActivity() {

    private var rectangle1: View? = null
    private var rectangle2: View? = null
    private var rectangle3: View? = null
    private var rectangle4: View? = null
    private var rectangle5: View? = null
    private var rectangle6: View? = null
    private var etPhoneNum: EditText? = null
    private var etAge: EditText? = null
    private var etEmPhone: EditText? = null
    private var etName: EditText? = null
    private var etPassword: EditText?=null
    private var btnregister: Button? = null
    private var manCheckBox: MaterialCheckBox? = null
    private var womanCheckBox: MaterialCheckBox? = null
    private var validateButton: Button?=null
    private var dbHelper: DBHelper? = null


    fun EditText.hasText(): Boolean {
        return text.toString().isNotEmpty()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        dbHelper = DBHelper(this)

        rectangle1 = findViewById(R.id.rectangle_1)
        rectangle2 = findViewById(R.id.rectangle_2)
        rectangle3 = findViewById(R.id.rectangle_3)
        rectangle4 = findViewById(R.id.rectangle_4)
        rectangle5 = findViewById(R.id.rectangle_5)
        rectangle6 = findViewById(R.id.rectangle_6)
        etPhoneNum = findViewById(R.id.et_phonenum)
        etAge = findViewById(R.id.et_age)
        etEmPhone = findViewById(R.id.et_emphone)
        etName = findViewById(R.id.et_name)
        etPassword = findViewById(R.id.et_password)
        manCheckBox = findViewById(R.id.man_checkbox)
        womanCheckBox = findViewById(R.id.woman_checkbox)
        btnregister = findViewById(R.id.btn_register)
        validateButton = findViewById(R.id.validateButton)

        validateButton?.visibility = View.GONE
        etPhoneNum?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
//            if (hasFocus) {
//                rectangle1?.background = ContextCompat.getDrawable(
//                    this@LoginActivity,
//                    R.drawable.rectangle_login_editview_focused
//                )
//            }
            if (etPhoneNum?.hasText() == true || hasFocus) {
                validateButton?.visibility = View.VISIBLE
                rectangle1?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview_focused

                )
            } else {
//                validateButton?.visibility = View.GONE
                rectangle1?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview

                )
            }
        }

        etName?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (etName?.hasText() == true || hasFocus) {
                rectangle2?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview_focused
                )
            } else {
                rectangle2?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }
        etAge?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (etAge?.hasText() == true || hasFocus) {
                rectangle3?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview_focused
                )
            } else {
                rectangle3?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }
        etEmPhone?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (etEmPhone?.hasText() == true || hasFocus) {
                rectangle5?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview_focused
                )
            } else {
                rectangle5?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }

        etPassword?.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (etPassword?.hasText() == true || hasFocus) {
                rectangle6?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview_focused
                )
            } else {
                rectangle6?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }
        manCheckBox?.setOnCheckedChangeListener { _, isChecked ->

            // EditText들의 포커스 해제
            etName?.clearFocus()
            etPhoneNum?.clearFocus()
            etAge?.clearFocus()
            etEmPhone?.clearFocus()
            etPassword?.clearFocus()
            // 키보드 내리기
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

            if (isChecked) {
                womanCheckBox?.isChecked = false
                if (!womanCheckBox?.isChecked!!) { // womanCheckbox가 체크되어 있지 않을 때에만 변경
                    rectangle4?.background = ContextCompat.getDrawable(
                        this@JoinActivity,
                        R.drawable.rectangle_login_editview_focused
                    )
                }
            } else {
                rectangle4?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }

        womanCheckBox?.setOnCheckedChangeListener { _, isChecked ->

            etName?.clearFocus()
            etPhoneNum?.clearFocus()
            etAge?.clearFocus()
            etEmPhone?.clearFocus()
            etPassword?.clearFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)

            if (isChecked) {
                etName?.clearFocus()
                manCheckBox?.isChecked = false
                if (!manCheckBox?.isChecked!!) { // manCheckbox가 체크되어 있지 않을 때에만 변경
                    rectangle4?.background = ContextCompat.getDrawable(
                        this@JoinActivity,
                        R.drawable.rectangle_login_editview_focused
                    )

                }
            } else {
                rectangle4?.background = ContextCompat.getDrawable(
                    this@JoinActivity,
                    R.drawable.rectangle_login_editview
                )
            }
        }
        validateButton?.setOnClickListener{
            val phoneNumber = etPhoneNum?.text.toString()
            val dbHelper = DBHelper(this)


            if(phoneNumber.isEmpty()){
                Toast.makeText(this,"전화번호를 입력해주세요", Toast.LENGTH_SHORT).show()
            }
            else{
                if (dbHelper.isPhoneNumberExists(phoneNumber)) {
                    // 중복된 전화번호가 존재하는 경우 처리할 로직 작성
                    // 예: Toast 메시지 출력, 에러 처리 등
                    showAlertDialog(getString(R.string.validateButton_fail))
                } else {
                    Toast.makeText(this, getString(R.string.validateButton_suc), Toast.LENGTH_SHORT).show()
                    validateButton?.visibility = View.GONE
                    etPhoneNum?.isEnabled = false
                    etPhoneNum?.isFocusable = false
                    etPhoneNum?.isClickable = false
                    etPhoneNum?.isCursorVisible = false
                    // 중복된 전화번호가 존재하지 않는 경우 처리할 로직 작성
                    // 예: DB에 데이터 저장, 다음 단계로 이동 등
                    validateButton?.visibility = View.GONE
                }
            }

        }
        btnregister?.setOnClickListener {
            val name = etName?.text.toString().trim()
            val phoneNumber = etPhoneNum?.text.toString().trim()
            val ageText = etAge?.text.toString().trim()
            val emergencyPhone = etEmPhone?.text.toString().trim()
            val gender = if (manCheckBox?.isChecked == true) "Man" else "Woman"
            val passwordText = etPassword?.text.toString().trim()

            if (phoneNumber.isEmpty()) {
                val message = getString(R.string.et_phonenum) + getString(R.string.Login_add)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty()) {
                val message = getString(R.string.et_name) + getString(R.string.Login_add)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (ageText.isEmpty()) {
                val message = getString(R.string.et_age) + getString(R.string.Login_add)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null || age <= 0) {
                val message = getString(R.string.et_age) + getString(R.string.Login_add)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val password = passwordText.toIntOrNull()
            if (password ==null || password <=0){
                val message = getString(R.string.et_password) + getString(R.string.Login_add)
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dbHelper = DBHelper(this)
            val userId = dbHelper.insertUser(name, phoneNumber, age, emergencyPhone, gender, password)

            if (userId != -1L) {
                // 저장 성공
                Toast.makeText(this, getString(R.string.Login_suc), Toast.LENGTH_SHORT).show()
                val intent = Intent(this@JoinActivity, SigningUpActivity::class.java)
                startActivity(intent)
                finish()

            } else {
                // 저장 실패
                Toast.makeText(this, getString(R.string.Login_fail), Toast.LENGTH_SHORT).show()

            }
        }
    }
    private fun showAlertDialog(message: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton("Login") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this@JoinActivity, SigningUpActivity::class.java)
                startActivity(intent)
                finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
                // Cancel 버튼을 클릭했을 때 수행할 동작을 여기에 추가하세요.
            }
        val alert = dialogBuilder.create()
        alert.setTitle("알림")
        alert.show()
    }
//    private fun showCustomDialog() {
//        val customDialog = CustomDialog(this)
//        customDialog.show()
//    }
}