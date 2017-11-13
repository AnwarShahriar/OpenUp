package com.example.anwarshahriar.openup.qrscanner

import android.arch.persistence.room.Room
import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.example.anwarshahriar.openup.R
import com.example.anwarshahriar.openup.persistance.AppDatabase
import com.example.anwarshahriar.openup.persistance.Device
import kotlinx.android.synthetic.main.activity_qrscanner.*

class QRScannerActivity : AppCompatActivity(), QRCodeReaderView.OnQRCodeReadListener {
    lateinit var qrCodeReaderView: QRCodeReaderView
    lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrscanner)

        db = Room.databaseBuilder(this, AppDatabase::class.java, "openup_db")
                .allowMainThreadQueries().build()

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
        val nameField = EditText(this)
        nameField.hint = "Device name"
        AlertDialog.Builder(this)
                .setView(nameField)
                .setTitle("Save Device")
                .setPositiveButton("Save", {
                    dialogInterface, i ->
                        val name = nameField.text.toString().trim()
                        if (name.length > 0) {
                            saveDevice("$ip:3000", name)
                        } else {
                            Toast.makeText(this, "You must give the device a name", Toast.LENGTH_LONG).show()
                        }
                        finish()
                })
                .setNegativeButton("Cancel", {
                    dialogInterface, i -> finish()
                })
                .create()
                .show()
    }

    private fun saveDevice(ipPort: String, name: String) {
        db.deviceDao().insertDevice(Device(ipPort, name))
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
