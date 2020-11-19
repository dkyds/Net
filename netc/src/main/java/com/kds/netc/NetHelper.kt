package com.kds.netc

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kds.netc.room.NetData
import com.kds.netc.ui.NetListActivity

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
object NetHelper {




    fun startActivity(activity: AppCompatActivity, type: String, result: (http: String) -> Unit) {
        val intent = Intent(activity, NetListActivity::class.java)
        intent.putExtra("type", type)
        activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val resultData = it.data?.getStringExtra("http")
                if (!resultData.isNullOrEmpty()) {
                    result(resultData)
                }

            }
        }.launch(intent)
    }

}