package com.udacity

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat

private val BUNDLE_STATUS_DOWNLOAD = "download.status"
private val BUNDLE_RADIO_OPTION = "download.option"

private val NOTIFICATION_ID = 0
private val requestCode = 0

fun NotificationManager.sendNotification(context: Context, downloadOption: String, downloadStatus: DownloadState) {

    val intent = Intent(context, DetailActivity::class.java)

    intent.putExtra(BUNDLE_STATUS_DOWNLOAD, downloadStatus)
    intent.putExtra(BUNDLE_RADIO_OPTION, downloadOption)

    val pendingIntent = PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    val cloud_done_bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.digigeek)
    val pictureStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(cloud_done_bitmap)
        .bigLargeIcon(null)

    val builder = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

        .setStyle(pictureStyle)
        .setLargeIcon(cloud_done_bitmap)

        .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        .addAction(R.drawable.ic_assistant_black_24dp, context.getString(R.string.notification_button), pendingIntent)
        .setAutoCancel(true)

    notify(NOTIFICATION_ID, builder.build())
}