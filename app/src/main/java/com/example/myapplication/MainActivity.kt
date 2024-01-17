package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.db.DBLoader
import java.util.Calendar
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.location.LocationListener
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.myapplication.ui.realhome.RealHomeFragment


class MainActivity : AppCompatActivity(), SensorEventListener,LocationListener{


    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private var gyroSensor: Sensor? = null
    private var isFalling = 0
    var cnt = 1
    var emr_cnt=0
    var startcalendar: Calendar = Calendar.getInstance() // 현재 시간 사용
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private val MIN_TIME_BETWEEN_UPDATES: Long = 60000 // 60초 (1분)
    private val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 0f
    val hour: Int = startcalendar.get(Calendar.HOUR_OF_DAY)// hour 값을 가져옴
    private val PERMISSION_REQUEST_CODE = 100 //위치 권한

    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val fragment = RealHomeFragment.newInstance(hour)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationListener = this


        // 위치 권한 확인 및 요청
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // 위치 권한이 있는 경우 위치 업데이트 요청
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                MIN_TIME_BETWEEN_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES,
                locationListener
            )
        } else {
            // 위치 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE
            )
        }
        // 넘어짐 감지 기능 수행
        sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)

    }


    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val longitude = location.longitude
        // 새로운 위치를 사용하여 원하는 작업 수행
    }

    override fun onResume() {
        super.onResume()
        //sensorManager.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        super.onPause()
        //sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not used
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val gForce = Math.sqrt((x * x + y * y + z * z).toDouble())
        if(gForce>20)
        {
            isFalling = 1 //넘어짐 1 안넘어짐 0
        }
        if (gForce > 20 && isFalling==1 && emr_cnt == 0 ) {
            emr_cnt = 1
            showNotification()
            isFalling = 0
        }
    }

    //응급상황 눌렀을때 화면
    private fun showNewLayout() {

        val newDialogView = LayoutInflater.from(this).inflate(R.layout.emergency, null)
        val closeButton: Button = newDialogView.findViewById(R.id.closeButton)

        val phoneNumber = "01073100140" // 전화번호를 입력해야 합니다.
        val message = "CalmDawn 앱 사용자가 쓰러져 의식이 없는것 같습니다. " // 보낼 문자 메시지의 내용을 입력해야 합니다.

        // 위치 권한 확인
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 위치 권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {

            // 위치 권한이 있는 경우 위치 정보 가져오기
            try {
                val location: Location? = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    // 위치 정보를 사용하여 원하는 작업 수행
                    val message = "CalmDawn 앱 사용자가 쓰러져 의식이 없는것 같습니다. " +"위치정보 : 위도:" +latitude+"경도:"+longitude // 보낼 문자 메시지의 내용을 입력해야 합니다.
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage(phoneNumber, null, message, null, null)
                } else {
                    // 위치 정보를 가져올 수 없는 경우 처리
                }
            } catch (e: SecurityException) {
                // 위치 권한이 있지만 SecurityException 발생
                e.printStackTrace()
            }

        } else {
            // 위치 권한이 없는 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSION_REQUEST_CODE)
        }

        val newBuilder = AlertDialog.Builder(this)
        newBuilder.setView(newDialogView)
        val newAlertDialog = newBuilder.create()
        newAlertDialog.setCancelable(false)
        newAlertDialog.show()

        closeButton.setOnClickListener {

            emergencymemo(cnt, startcalendar)
//            saveHourToDB()
            cnt++
            emr_cnt = 0
            newAlertDialog.dismiss()
            Toast.makeText(this, "새로운 레이아웃을 닫았습니다.", Toast.LENGTH_SHORT).show()
            // Additional actions, if any
        }
    }

    //넘어졌을때 알림
    private fun showNotification() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.danger_dialog_layout, null)
        val closeButton: Button = dialogView.findViewById(R.id.closeButton)
        val button2: Button = dialogView.findViewById(R.id.button2)
        startcalendar = Calendar.getInstance() // 현재 시간 사용
//        saveHourToDB()
        val notificationId = 1 // 알림의 고유 ID
        val channelId = "my_channel"

        // 알림 채널 생성 (Android 8.0 이상에서 필요)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "My Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.baseline_emergency_24)
            .setContentTitle("긴급! 넘어짐 감지")
            .setContentText("자세한 내용과 조치를 받으시려면 알림을 누르세요!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // 사용자가 알림을 탭하면 자동으로 알림이 사라지도록 설정

        val notificationManager = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(notificationId, notificationBuilder.build())
        //////////////////////////////////////////////////////////
        val countDownTimer = object : CountDownTimer(5000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                button2.text = secondsRemaining.toString()
            }

            override fun onFinish() {
                button2.text = "응급상황"
                // 여기에서 추가로 수행할 작업을 처리할 수 있습니다.
            }
        }

        countDownTimer.start()
        // Set your custom image to the ImageView
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        alertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()

        val handler = Handler()
        val delayInMillis: Long = 5000 // 5초
        val count = 0

        handler.postDelayed({showNewLayout()
            button2.setOnClickListener{
                showNewLayout()

            }}, delayInMillis)//5초 기다리기

        closeButton.setOnClickListener {
            handler.removeCallbacksAndMessages(null) // 예약된 작업 제거
            countDownTimer.cancel()
            alertDialog.dismiss()
            Toast.makeText(this, "알림이 지워졌습니다.", Toast.LENGTH_SHORT).show()
            isFalling = 0
            emr_cnt = 0
        }
    }

    //자동 발작 메모 추가하기
    private fun emergencymemo(int: Int, int2: Calendar) {

        val title = int.toString()+"번째 자동생성된 발작알림"
        val memo = int.toString()+"번째 알림입니다.\n" + "발작 시작 시간 :" + int2.time +"\n최초발견자 :\n" +"발작 상황 :\n"+"발작 유형 :\n"+"발작 지속시간 :\n" + "유발요인 : \n"
        val calendar: Calendar = Calendar.getInstance() // 현재 시간 사용

        val dbLoader = DBLoader(applicationContext)
        dbLoader.save(title, memo, calendar)
        Toast.makeText(this, "저장됨", Toast.LENGTH_SHORT).show()
    }


}

