package com.example.anwarshahriar.openup

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
import com.example.anwarshahriar.openup.qrscanner.QRScannerActivity
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity(), MainView {

    val JSON = MediaType.parse("application/json; charset=utf-8")
    lateinit var presenter: MainPresenter
    private var url: String? = null
    var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        val container = findViewById<LinearLayout>(R.id.container)
        container.removeAllViews()

        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val devices = prefs.getStringSet("devices", null)
        devices?.forEach {
            val ipPort = TextView(this)
            ipPort.setText(it)
            ipPort.setTextSize(22f)
            ipPort.setOnClickListener {
                openInDevice(ipPort.text.toString())
            }
            container.addView(ipPort)
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
