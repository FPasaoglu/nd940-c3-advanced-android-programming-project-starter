package com.udacity

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    private lateinit var DOWNLOAD_OPTION : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        createChannel(getString(R.string.channel_id), getString(R.string.channel_name))

        custom_button.setOnClickListener {
            getSelectedRadioButton()?.let {
                download(it)
                custom_button.setState(ButtonState.Loading)
            }
        }
    }

    private fun getSelectedRadioButton(): String? {
        val radioId = radioGroup.checkedRadioButtonId

        when (radioId) {
            radioButton1.id -> {
                DOWNLOAD_OPTION = getString(R.string.radio_button_1_text)
                return "https://github.com/square/retrofit"
            }
            radioButton2.id -> {
                DOWNLOAD_OPTION = getString(R.string.radio_button_2_text)
                return "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
            }
            radioButton3.id -> {
                DOWNLOAD_OPTION = getString(R.string.radio_button_3_text)
                return "https://github.com/square/retrofit"
            }
            -1 -> Toast.makeText(this, getString(R.string.option_no_selected), Toast.LENGTH_SHORT)
                .show()
        }
        return null
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent!!.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val query = DownloadManager.Query().setFilterById(id)
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        notificationManager.sendNotification(this@MainActivity, DOWNLOAD_OPTION, DownloadState.SUCCESS)
                    }
                    DownloadManager.STATUS_FAILED -> {
                        notificationManager.sendNotification(this@MainActivity, DOWNLOAD_OPTION, DownloadState.FAIL)
                    }
                }
            }
        }
    }

    private fun download(URL: String) {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setNotificationVisibility(View.VISIBLE)
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)

            notificationChannel.enableVibration(true)
            notificationChannel.description = getString(R.string.notification_description)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE

            notificationManager = this.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}
