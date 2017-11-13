package com.example.anwarshahriar.openup

import android.arch.persistence.room.Room
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.preference.PreferenceManager
import android.util.Log
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.anwarshahriar.openup.persistance.AppDatabase
import com.example.anwarshahriar.openup.qrscanner.QRScannerActivity
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), MainView {

    val JSON = MediaType.parse("application/json; charset=utf-8")
    lateinit var presenter: MainPresenter
    lateinit var db: AppDatabase
    private var url: String? = null
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "openup_db")
                .allowMainThreadQueries().build()

        presenter = MainPresenterImpl()

        findViewById<Button>(R.id.button_add_new).setOnClickListener {
            presenter.addNewDevice()
        }

        handleUrl(intent)
    }

    override fun openQRScanner() {
        Toast.makeText(this, "Opening QR scanner", Toast.LENGTH_LONG).show()
        startActivity(Intent(this, QRScannerActivity::class.java))
    }

    override fun loadDevices() {
        val devices = db.deviceDao().getAllDevices()
        val container = findViewById<LinearLayout>(R.id.container)
        devices.forEach { device ->
            val deviceName = TextView(this)
            deviceName.setText(device.name)
            deviceName.setTextSize(22f)
            deviceName.setOnClickListener {
                openInDevice(device.ipPort)
            }
            container.addView(deviceName)
        }
    }

    private fun handleUrl(intent: Intent) {
        val action = intent.action
        if (Intent.ACTION_SEND.equals(action)) {
            url = intent.getStringExtra(Intent.EXTRA_TEXT)
            Toast.makeText(this, "Select device to open the url", Toast.LENGTH_LONG).show()
        } else if (Intent.ACTION_VIEW.equals(action)) {
            url = intent.data.toString()
            Toast.makeText(this, "Select device to open the url", Toast.LENGTH_LONG).show()
        }
    }

    private fun openInDevice(ipPort: String) {
        if (url != null) {
            val body = RequestBody.create(JSON, "{ \"url\": \"$url\" }")
            val request = Request.Builder()
                    .url("http://$ipPort/openup")
                    .post(body)
                    .build()
            client.newCall(request).enqueue(ResponseCallback(application))

        } else {
            Toast.makeText(this, "No url is found to open in the selected device", Toast.LENGTH_LONG).show()
        }
    }

    class ResponseCallback(val context: Context) : Callback {
        override fun onResponse(call: Call?, response: Response?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "The url is open", Toast.LENGTH_LONG).show()
            }
        }

        override fun onFailure(call: Call?, e: IOException?) {
            Handler(Looper.getMainLooper()).post {
                Toast.makeText(context, "Couldn't open the url", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.setView(this)
    }

    override fun onStop() {
        super.onStop()
        presenter.clearView()
    }
}
