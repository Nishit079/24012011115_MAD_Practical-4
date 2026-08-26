package com.example.a24012011115_mad_practical_4

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class AlarmBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && context != null) {
            val str1 = intent.getStringExtra("Service1")

            if (str1 == "Start" || str1 == "Stop") {
                val intentService = Intent(context, AlarmService::class.java)
                intentService.putExtra("Service1", str1)

                if (str1 == "Start") {
                    ContextCompat.startForegroundService(context, intentService)
                } else {
                    context.stopService(intentService)
                }
            }
        }
    }
}
