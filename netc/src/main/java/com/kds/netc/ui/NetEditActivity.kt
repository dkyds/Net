package com.kds.netc.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.kds.netc.NetType
import com.kds.netc.R
import com.kds.netc.room.DbUtil
import com.kds.netc.room.NetData
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.*

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
class NetEditActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private var typeStr: String = "http"
    private val typeList = listOf("网络数据", "web数据")
    private var data: NetData? = null
    private var flag = false //是否是 修改

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        data = intent.getParcelableExtra("data")

        if (data == null) {
            data = NetData(0, "", "", "", false)
        } else {
            initView(data!!)
        }

        backIv.setOnClickListener {
            finish()
        }

        httpEv.addTextChangedListener {
            data?.url = it?.toString() ?: ""
        }

        portEv.addTextChangedListener {
            data?.port = it?.toString() ?: ""
        }


        confirmBt.setOnClickListener {
            submit()
        }
        initSpinnerAdapter()

    }


    private fun initSpinnerAdapter() {
        val adapter =
            ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, typeList)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                typeStr = NetType.values()[position].code
                data?.type = typeStr
                Toast.makeText(this@NetEditActivity, typeStr, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        }
    }

    private fun initView(data: NetData) {
        flag = true
        httpEv.setText(data.url)
        portEv.setText(data.port)
        var position = 0
        kotlin.run {
            NetType.values().forEachIndexed { index, netType ->
                if (netType.code == data.type) {
                    position = index
                    return@run
                }
            }
        }
        typeStr = data.type
        spinner.setSelection(position)
    }


    private fun submit() {
        launch {
            if (data == null) {
                Toast.makeText(this@NetEditActivity, "数据错误", Toast.LENGTH_SHORT).show()
                return@launch
            }

            if (data!!.port.isEmpty()) {
                Toast.makeText(this@NetEditActivity, "端口号未填写", Toast.LENGTH_SHORT).show()
                return@launch
            }


            if (data!!.url.isEmpty()) {
                Toast.makeText(this@NetEditActivity, "url地址未填写", Toast.LENGTH_SHORT).show()
                return@launch
            }


            val resultCode = if (flag) {
                0x12
            } else {
                0x11
            }
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(resultCode, intent)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}