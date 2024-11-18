package com.example.tugaspertemuan12

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.tugaspertemuan12.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val channelId = "TEST_NOTIF"
    private val notifId = 90

    private val updateReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Get the updated counts from the broadcast
            val likeCount = intent.getIntExtra("likeCount", 0)
            val dislikeCount = intent.getIntExtra("dislikeCount", 0)

            // Update the TextViews
            binding.likeCountTextView.text = "Likes: $likeCount"
            binding.dislikeCountTextView.text = "Dislikes: $dislikeCount"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notifManager = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

        // Register the broadcast receiver to listen for updates
        LocalBroadcastManager.getInstance(this).registerReceiver(
            updateReceiver, IntentFilter("UPDATE_LIKE_DISLIKE_COUNT")
        )

        binding.btnNotif.setOnClickListener {
            val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.FLAG_IMMUTABLE
            }
            else {
                0
            }

            val likeIntent = Intent(this, LikeReceiver::class.java)
            val likePendingIntent = PendingIntent.getBroadcast(this, 0, likeIntent, flag)

            val dislikeIntent = Intent(this, DislikeReceiver::class.java)
            val dislikePendingIntent = PendingIntent.getBroadcast(this, 0, dislikeIntent, flag)

            val intent = Intent(this,
                NotifReceiver::class.java).putExtra("MESSAGE", "Baca selengkapnya ...")
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                flag
            )
            val notifImage = BitmapFactory.decodeResource(resources,
                R.drawable.img)
            val builder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle("Counter")
                .setContentText("HAI KAWANN") // Isi pesan bebas
                .setStyle(
                    NotificationCompat.BigPictureStyle()
                        .bigPicture(notifImage)
                )
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(
                    R.drawable.ic_like, // Replace with your "like" icon
                    "Like",
                    likePendingIntent
                ) // Add the like button action
                .addAction(
                    R.drawable.ic_dislike, // Replace with your "dislike" icon
                    "Dislike",
                    dislikePendingIntent
                ) // Add the dislike button action

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notifChannel = NotificationChannel(
                    channelId, // Id channel
                    "counter", // Nama channel notifikasi
                    NotificationManager.IMPORTANCE_DEFAULT
                )
                with(notifManager) {
                    createNotificationChannel(notifChannel)
                    notify(notifId, builder.build())
                }
            }
            else {
                notifManager.notify(notifId, builder.build())
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(updateReceiver)
    }
}