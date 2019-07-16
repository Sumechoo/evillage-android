package nl.worth.clangnotifications.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import nl.worth.clangnotifications.R
import nl.worth.clangnotifications.ui.ClangActivity
import kotlin.random.Random

open class ClangFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        p0?.data?.let { data ->
            when (data["type"]) {
                "clang" -> {
                    if (handleClangNotification(data)) return
                }
                else -> super.onMessageReceived(p0)
            }
        }
        super.onMessageReceived(p0)
    }

    private fun handleClangNotification(data: Map<String, String>): Boolean {
        val productTitle = data["clangTitle"]
        val productContent = data["clangMessage"]

        val intent = Intent(this, ClangActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)


        val notificationManager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "channel-01"
        val channelName = "Channel Name"
        val importance = NotificationManager.IMPORTANCE_HIGH

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val mChannel = NotificationChannel(
                channelId, channelName, importance
            )
            notificationManager.createNotificationChannel(mChannel)
        }

        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(productTitle)
            .setContentText(productContent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        //FIXME: side effects!!!!!!
        addActions(data, builder)

        with(NotificationManagerCompat.from(this)) {
            notify(Random.nextInt(0, 100), builder.build())
        }
        return true
    }

    private fun addActions(data: Map<String, String>, notificationBuilder: NotificationCompat.Builder) {
        val productId = data["id"]
        for (i in 1..3) {
            val actionId = data["action${i}Id"]
            val actionTitle = data["action${i}Title"]

            actionId?.let {
                actionTitle.let {
                    val pendingIntent =
                        PendingIntent.getService(this, 0, Intent(this, ClangIntentService::class.java).apply {
                            this.putExtra("productId", productId)
                            this.putExtra("actionId", actionId)
                        }, 0)

                    notificationBuilder.addAction(
                        android.R.drawable.ic_notification_overlay,
                        actionTitle,
                        pendingIntent
                    )
                }
            }
        }
    }
}