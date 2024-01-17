package com.example.myapplication.ui.timer

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentTimerBinding
import java.util.*

class TimerFragment : Fragment() {
//
//    private lateinit var adapter: MemoAdapter
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTimerBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        sharedPreferences = requireContext().getSharedPreferences(M_SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)

        initOnOffButton()
        initChangeAlarmTimeButton()
        renderView(fetchDataFromSharedPreferences())

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cancelAlarm(requireContext(), getPendingIntent())
        _binding = null
    }

    private fun fetchDataFromSharedPreferences(): AlarmDisplayModel {
        val timeDBValue = sharedPreferences.getString(M_ALARM_KEY, "09:30") ?: "09:30"
        val onOffDBValue = sharedPreferences.getBoolean(M_ONOFF_KEY, false)

        val alarmData = timeDBValue.split(":")
        val alarmModel = AlarmDisplayModel(alarmData[0].toInt(), alarmData[1].toInt(), onOffDBValue)

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            M_ALARM_REQUEST_CODE,
            Intent(requireContext(), AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_NO_CREATE
        )

        if (pendingIntent != null) {
            if (!alarmModel.onOff) {
                // Cancel the alarm only if it is turned off
                cancelAlarm(requireContext(), pendingIntent)
                Log.d("AlarmLog", "Alarm canceled")
            }
        } else {
            if (alarmModel.onOff) {
                // If the alarm is turned on but there is no pendingIntent
                alarmModel.onOff = false
                Log.d("AlarmLog", "Alarm turned off")
            }
        }

        return alarmModel
    }

    private fun initChangeAlarmTimeButton() {
        val changeAlarmButton = binding.changeAlarmTimeButton
        changeAlarmButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val timePickerDialog = TimePickerDialog(
                requireContext(),
                TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                    val model = saveAlarmModel(hour, minute, false)
                    renderView(model)
                    cancelAlarm(requireContext(), getPendingIntent())

                    val context = requireContext()
                    val calendar = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, model.hour)
                        set(Calendar.MINUTE, model.minute)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)

                        val currentTime = Calendar.getInstance()
                        if (before(currentTime)) {
                            add(Calendar.DATE, 1)
                        }
                    }

                    // Reset the alarm
                    val alarmManager =
                        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                    val intent = Intent(context, AlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        M_ALARM_REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    if (model.onOff) {
                        // Add a log message
                        Log.d("TimerFragment", "Alarm time set: ${calendar.time}")
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                        )
                    } else {
                        cancelAlarm(context, pendingIntent)
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            )
            timePickerDialog.show()
        }
    }

    private fun initOnOffButton() {
        val onOffButton = binding.onOffButton
        onOffButton.setOnClickListener {
            val model = it.tag as? AlarmDisplayModel ?: return@setOnClickListener
            val newModel = saveAlarmModel(model.hour, model.minute, !model.onOff)
            renderView(newModel)

            val context = requireContext()
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, newModel.hour)
                set(Calendar.MINUTE, newModel.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                val currentTime = Calendar.getInstance()
                if (before(currentTime)) {
                    add(Calendar.DATE, 1)
                }
            }

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, AlarmReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                M_ALARM_REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            if (newModel.onOff) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
                // Add a log message
                Log.d("TimerFragment", "Alarm activated.")
            } else {
                cancelAlarm(context, pendingIntent)
                // Add a log message
                Log.d("TimerFragment", "Alarm deactivated.")
            }
        }
    }

    private fun renderView(model: AlarmDisplayModel) {
        binding.ampmTextView.text = model.ampmText
        binding.timeTextView.text = model.timeText
        binding.onOffButton.text = model.onOffText
        binding.onOffButton.tag = model
    }

    private fun cancelAlarm(context: Context, pendingIntent: PendingIntent) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun saveAlarmModel(hour: Int, minute: Int, onOff: Boolean): AlarmDisplayModel {
        val model = AlarmDisplayModel(hour, minute, onOff)

        with(sharedPreferences.edit()) {
            putString(M_ALARM_KEY, model.makeDataForDB())
            putBoolean(M_ONOFF_KEY, model.onOff)
            apply()
        }

        return model
    }

    private fun getPendingIntent(): PendingIntent {
        val intent = Intent(requireContext(), AlarmReceiver::class.java)
        return PendingIntent.getBroadcast(
            requireContext(),
            M_ALARM_REQUEST_CODE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        private const val M_SHARED_PREFERENCE_NAME = "time"
        private const val M_ALARM_KEY = "alarm"
        private const val M_ONOFF_KEY = "onOff"
        private const val M_ALARM_REQUEST_CODE = 1000
    }
}
