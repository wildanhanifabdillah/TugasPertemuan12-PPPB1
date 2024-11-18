package com.example.tugaspertemuan12

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class DislikeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Increment dislike count
        NotificationHelper.dislikeCount++
        // Broadcast the updated count
        val likeIntent = Intent("UPDATE_LIKE_DISLIKE_COUNT")
        likeIntent.putExtra("likeCount", NotificationHelper.likeCount)
        likeIntent.putExtra("dislikeCount", NotificationHelper.dislikeCount)
        LocalBroadcastManager.getInstance(context).sendBroadcast(likeIntent)
    }
}