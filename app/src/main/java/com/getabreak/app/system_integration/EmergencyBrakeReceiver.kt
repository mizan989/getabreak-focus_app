package com.getabreak.app.system_integration

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.getabreak.app.features.EmergencyBrakeFeature
import timber.log.Timber

/**
 * Takes a brake back from the notification, for as long as [EmergencyBrakeFeature.canUndo].
 */
class EmergencyBrakeReceiver : BroadcastReceiver() {
    companion object {
        private const val ACTION_UNDO = "com.getabreak.app.ACTION_UNDO_BRAKE"

        fun undoIntent(context: Context): PendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, EmergencyBrakeReceiver::class.java).setAction(ACTION_UNDO),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != ACTION_UNDO) return
        val undone = EmergencyBrakeFeature.undo(context)
        Timber.d("Undo of the brake from the notification: $undone")
    }
}
