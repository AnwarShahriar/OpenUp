package com.example.anwarshahriar.openup.qrscanner

import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.anwarshahriar.openup.R
import kotlinx.android.synthetic.main.activity_qrscanner.*

class QRScannerActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    lateinit var qrCodeReaderView: QRCodeReaderView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        qrCodeReaderView = findViewById<QRCodeReaderView>(R.id.qrdecoderview)
        qrCodeReaderView.setOnQRCodeReadListener(this)

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true)

        // Use this function to change the autofocus interval
        qrCodeReaderView.setAutofocusInterval(2000L)

        // Use this function to enable/disable Torch
        qrCodeReaderView.setTorchEnabled(true)

        // Use this function to set back camera preview
        qrCodeReaderView.setBackCamera();
    }

    override fun onQRCodeRead(ip: String?, points: Array<out PointF>?) {
        Toast.makeText(this, ip, Toast.LENGTH_LONG).show()
        saveDevice("$ip:3000")
        finish()
    }

    private fun saveDevice(ipPort: String) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext)
        val devices = prefs.getStringSet("devices", HashSet<String>())
        devices.add(ipPort)
        prefs.edit().putStringSet("devices", devices).commit()
    }

    override fun onResume() {
        super.onResume()
        qrCodeReaderView.startCamera()
    }

    override fun onPause() {
        super.onPause()
        qrCodeReaderView.stopCamera()
    }

}
