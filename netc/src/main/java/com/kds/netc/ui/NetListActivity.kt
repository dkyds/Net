package com.kds.netc.ui

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.kds.netc.NetAdapter
import com.kds.netc.NetHelper
import com.kds.netc.R
import com.kds.netc.room.DbUtil
import com.kds.netc.room.NetData
import kotlinx.android.synthetic.main.activity_netkds_list.*
import kotlinx.coroutines.*

/**
 * @author kyp
 * @date 2020/11/18
 * @desc NetList 界面
 */
class NetListActivity : AppCompatActivity(), CoroutineScope by MainScope() {


    private var type: String? = null
    private var adapter: NetAdapter? = null
    private var http: String? = null
    private var upDataList = mutableListOf<NetData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_netkds_list)
        type = intent.getStringExtra("type")
        backIv.setOnClickListener {
            finish()
        }

//        Log.i("TAG", NetHelper.getUrl(this))

        rightTv.setOnClickListener {


            launch {

                if (upDataList.isNotEmpty()) {
                    withContext(Dispatchers.IO) {
                        DbUtil.getInstance(this@NetListActivity).netDao().upDateNets(upDataList)
                    }
                }

                val intent = Intent()
                intent.putExtra("http", http)
                setResult(RESULT_OK, intent)
                finish()
            }

        }
        getHistory()

    }

    /**
     * 获取数据库数据
     */
    private fun getHistory() {
        launch {
            val list = withContext(Dispatchers.IO) {
                DbUtil.getInstance(this@NetListActivity).netDao().getNetDataList(type ?: "")
            }
            initAdapter(list)

        }
    }


    private fun initAdapter(list: MutableList<NetData>) {
        if (adapter == null) {
            adapter = NetAdapter(this, list)
            adapter?.setListener(object : NetAdapter.NetItemListener {
                override fun onModifyItem(position: Int, data: NetData) {
                    startActivityForResult(data, position)
                }

                override fun onCheckItem(position: Int, data: NetData, lastData: NetData?) {
                    http = "${data.httpType}${data.url}/"
                    val list = mutableListOf<NetData>()
                    if (lastData != null) {
                        list.add(lastData)
                    }
                    list.add(data)
                    upDataList = list
                }


                override fun addItem() {
                    startActivityForResult()
                }

                override fun deleteItem(position: Int, data: NetData) {
                    launch {
                        val x = withContext(Dispatchers.IO) {
                            DbUtil.getInstance(this@NetListActivity).netDao().deleteData(data)
                        }

                        showDialog(data)


                    }
                }

            })
            netRv.layoutManager = LinearLayoutManager(this)
            netRv.adapter = adapter
        }
    }

    /**
     * 回调
     */
    private fun startActivityForResult(data: NetData? = null, position: Int? = null) {
        val intent = Intent(this, NetEditActivity::class.java)
        if (data != null) {
            intent.putExtra("data", data)
        }
        if (!type.isNullOrEmpty()) {
            intent.putExtra("type", type)
        }
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val mData =
                it.data?.getParcelableExtra<NetData>("data") ?: return@registerForActivityResult
            when (it.resultCode) {
                0x11 -> {
                    //新增
                    launch {
                        withContext(Dispatchers.IO) {
                            DbUtil.getInstance(this@NetListActivity).netDao().insertNet(mData)
                        }
                        adapter?.addNetItem(mData)
                    }
                }
                0x12 -> {
                    //修改
                    launch {
                        withContext(Dispatchers.IO) {
                            DbUtil.getInstance(this@NetListActivity).netDao().updateNet(mData)
                        }
                        adapter?.updateItem(position ?: 0, mData)
                    }
                }
            }
        }.launch(intent)
    }


    private fun showDialog(data: NetData) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("提示")
            .setMessage("是否要删除${data.description}")
            .setPositiveButton(
                "取消"
            ) { dialog, _ -> dialog.dismiss() }
            .setNegativeButton(
                "确定"
            ) { dialog, _ ->
                dialog.dismiss()
                adapter?.deleteItem(data)
            }
            .create()
        dialog.show()
    }
}