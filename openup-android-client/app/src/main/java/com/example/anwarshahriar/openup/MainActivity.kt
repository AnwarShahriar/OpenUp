package com.example.anwarshahriar.openup

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity(), MainView {
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter = MainPresenterImpl()

        findViewById<Button>(R.id.button_add_new).setOnClickListener {
            presenter.addNewDevice()
        }
    }

    override fun openQRScanner() {
        Toast.makeText(this, "Opening QR scanner", Toast.LENGTH_LONG).show()
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
