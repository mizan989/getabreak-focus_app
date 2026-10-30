package com.getabreak.app.system_integration

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.getabreak.app.util.TrustedClock

/**
 * Keeps [TrustedClock] in step with the phone: a reboot anchors it anew before the date settings
 * can be reached, and a change to the date or time is taken out the moment it is made.
 */
class ClockChangedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        TrustedClock.sync()
    }
}
