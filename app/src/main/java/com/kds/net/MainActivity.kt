package com.kds.net

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.kds.netc.NetHelper
import com.kds.netc.NetType

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text = findViewById<TextView>(R.id.text)
        text.setOnClickListener {
            NetHelper.startActivity(this, NetType.HTTP.code) {
                text.text = it
            }
        }
    }
}