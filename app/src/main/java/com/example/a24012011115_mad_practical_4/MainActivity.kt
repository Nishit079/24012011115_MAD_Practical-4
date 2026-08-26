package com.example.a24012011115_mad_practical_4

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var btnCreateAlarm: MaterialButton
    private lateinit var cardCancelAlarm: MaterialCardView
    private lateinit var tvSetAlarmTime: TextView
    private lateinit var btnCancelAlarm: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request notification permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 101)
            }
        }

        // Initialize UI components
        btnCreateAlarm = findViewById(R.id.btnCreateAlarm)
        cardCancelAlarm = findViewById(R.id.cardCancelAlarm)
        tvSetAlarmTime = findViewById(R.id.tvSetAlarmTime)
        btnCancelAlarm = findViewById(R.id.btnCancelAlarm)

        // Step 1: When "Create Alarm" is clicked, show Time Picker
        btnCreateAlarm.setOnClickListener {
            showTimePicker()
        }

        // Step 4: When "Cancel Alarm" is clicked, hide the bottom card and stop alarm
        btnCancelAlarm.setOnClickListener {
            stopAlarm()
            cardCancelAlarm.visibility = View.GONE
        }
    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", "Start")

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (alarmManager.canScheduleExactAlarms()) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                } else {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
                }
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            }
            Toast.makeText(this, "Alarm set!", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            // Fallback for unexpected security exceptions
            alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            Toast.makeText(this, "Exact alarm permission missing. Setting inexact alarm.", Toast.LENGTH_LONG).show()
        }
    }

    private fun stopAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        intent.putExtra("Service1", "Stop")

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager.cancel(pendingIntent)
        sendBroadcast(intent) // Send directly to stop service immediately
    }

    /** Opens the TimePickerDialog (Step 2) */
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                // Step 3: Once time is selected, update UI and show bottom card
                updateAlarmUI(selectedHour, selectedMinute)
            },
            hour,
            minute,
            false // 12-hour format with AM/PM
        )
        timePickerDialog.show()
    }

    /** Updates the text on the bottom card and makes it visible */
    private fun updateAlarmUI(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // If time is in the past, set for tomorrow
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }

        val sdf = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        val formattedTime = sdf.format(calendar.time)

        tvSetAlarmTime.text = formattedTime
        cardCancelAlarm.visibility = View.VISIBLE

        setAlarm(calendar.timeInMillis)
    }
}
