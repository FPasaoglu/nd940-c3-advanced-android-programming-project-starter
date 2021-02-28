package com.udacity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    val BUNDLE_STATUS_DOWNLOAD = "download.status"
    val BUNDLE_RADIO_OPTION = "download.option"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val downloadStatus = intent.getSerializableExtra(BUNDLE_STATUS_DOWNLOAD)
        val downloadOption = intent.getStringExtra(BUNDLE_RADIO_OPTION)

        download_option.text = downloadOption

        when(downloadStatus) {
            DownloadState.SUCCESS -> {
                download_status.text = getString(R.string.success)
                download_status.setTextColor(getColor(R.color.dark_green))
            }
            DownloadState.FAIL -> {
                download_status.text = getString(R.string.fail)
                download_status.setTextColor(Color.RED)
            }
        }

        fab.setOnClickListener {
            this.onBackPressed()
        }
    }

}