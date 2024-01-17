package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import com.example.myapplication.CustomDialog.PhoneNumberManager.setPhoneNumber
import com.example.myapplication.db.DBHelper

class CustomDialog(context: Context) : Dialog(context) {

    private var dialogListener: CustomDialogListener? = null
    private var id: EditText? = null
    private var password: EditText? = null
    private var login_btn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.login_custom_dialog)

        window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 다이얼로그의 크기 및 위치 설정
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = 1000
        layoutParams.height = 1200
        window?.attributes = layoutParams

        id = findViewById(R.id.edit_id)
        password = findViewById(R.id.edit_password)
        login_btn = findViewById(R.id.btn_login)

        login_btn?.setOnClickListener {
            onLoginClicked()
        }
    }

    interface CustomDialogListener {
        fun onLoginClicked(id: String, password: String)
        fun onLoginSuccess()
    }

    fun setDialogListener(listener: CustomDialogListener) {
        dialogListener = listener
    }
    private fun onLoginClicked() {
        val id = id?.text.toString()
        val passwordString = password?.text.toString()
        val password = passwordString.toIntOrNull()

        if (password != null) {
            // DBHelper 인스턴스 생성

            val dbHelper=DBHelper(context)
            // 입력한 아이디와 비밀번호가 DB에 저장된 값과 일치하는지 확인
            val loginSuccess = dbHelper.login(id, password)

            if (loginSuccess) {
                // 로그인 성공
                dialogListener?.onLoginClicked(id, password.toString())
                dialogListener?.onLoginSuccess() // 로그인 성공 콜백 호출
                setPhoneNumber(id)
                dismiss()
            } else {

            }
            // DBHelper 인스턴스 리소스 해제

            dbHelper.close()
            // 다이얼로그 닫기
            dismiss()

        } else {
            // 변환 실패: 유효한 정수로 변환할 수 없음
            // 오류 처리 등 필요한 작업 수행

        }

    }
    object PhoneNumberManager {
        private var phoneNumber: String? = null

        fun setPhoneNumber(number: String) {
            phoneNumber = number
        }

        fun getPhoneNumber(): String? {
            return phoneNumber
        }
    }

}
