package com.example.myapplication

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class CustomDialog_join(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.join_custom_dialog)

        window?.setBackgroundDrawableResource(android.R.color.transparent)

        // 다이얼로그의 크기 및 위치 설정
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(window?.attributes)
        layoutParams.width = 1000
        layoutParams.height = 1200
        window?.attributes = layoutParams
    }

}
