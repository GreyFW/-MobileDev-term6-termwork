package com.example.workup.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.workup.worker.StreakCheckWorker
import java.util.concurrent.TimeUnit

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val workRequest = PeriodicWorkRequestBuilder<StreakCheckWorker>(1, TimeUnit.DAYS)
                .build() // добавить ограничения потом

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "StreakCheck",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}