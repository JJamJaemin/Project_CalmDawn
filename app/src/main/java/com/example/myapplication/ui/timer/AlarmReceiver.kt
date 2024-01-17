package com.example.myapplication.ui.timer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R

class AlarmReceiver : BroadcastReceiver() {


    companion object {
        const val NOTIFICATION_CHANNEL_ID = "1000"
        const val NOTIFICATION_ID = 100
    }

    override fun onReceive(context: Context, intent: Intent) {

        // 채널 생성
        createNotificationChannel(context)
        // 알림
        notifyNotification(context)

    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                ContextCompat.getSystemService(context, NotificationManager::class.java)

            // 권한 확인
            if (notificationManager?.areNotificationsEnabled() == true) {
                val notificationChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "약복용 알람.",
                    NotificationManager.IMPORTANCE_HIGH
                )

                notificationManager.createNotificationChannel(notificationChannel)
                Log.d("AlarmReceiver", "Notification channel created")
            } else {
                // 권한이 없는 경우 처리할 로직 추가
                // 예: Toast 메시지를 표시하여 사용자에게 알림 권한이 필요하다고 알려줌
                Toast.makeText(context, "알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun notifyNotification(context: Context) {
        with(NotificationManagerCompat.from(context)) {
            val build = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("알람")
                .setContentText("약을 드실 시간입니다.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.ic_launcher_foreground)

            // 알림 권한 확인
            if (areNotificationsEnabled()) {
                notify(NOTIFICATION_ID, build.build())
                Log.d("AlarmReceiver", "Notification sent")
            } else {
                // 권한이 없는 경우 처리할 로직 추가
                // 예: Toast 메시지를 표시하여 사용자에게 알림 권한이 필요하다고 알려줌
                Toast.makeText(context, "알림 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }



}
