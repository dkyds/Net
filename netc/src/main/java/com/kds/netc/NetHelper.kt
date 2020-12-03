package com.kds.netc

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kds.netc.room.DbUtil
import com.kds.netc.room.NetData
import com.kds.netc.ui.NetListActivity
import kotlinx.coroutines.*

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
object NetHelper {


    /**
     * @param type web 还是 http
     * @see NetType 枚举类
     */
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

    /**
     * 获取一选择的url
     */
    suspend fun getUrl(context: Context): String {
        var data: NetData? = null
        data = withContext(Dispatchers.IO) {
            DbUtil.getInstance(context).netDao().getCheckData()
        }

        return if(data == null||data.url.isEmpty()){
            ""
        }else{
            "${data.httpType}${data.url}/"
        }
    }
}