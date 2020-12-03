package com.kds.netc.room

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
@Database(entities = [NetData::class], version = 1)
abstract class NetDatabase : RoomDatabase() {
    abstract fun netDao(): NetDao


    companion object {
        val Callback = object : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                /**
                 * 正式环境
                 */
                val values1 = ContentValues()
                values1.put("url", "app.newhope.cn")
                values1.put("description", "正式环境")
                values1.put("inserTime", 1L)
                values1.put("isCheck", false)
                values1.put("type", "http")
                values1.put("httpType", "https://")
                db.insert("NetData", SQLiteDatabase.CONFLICT_NONE, values1)

                /**
                 * uat环境
                 */
                val values3 = ContentValues()
                values3.put("url", "uat.open.api.newhope.cn")
                values3.put("description", "uat环境")
                values3.put("inserTime", 3L)
                values3.put("isCheck", false)
                values3.put("type", "http")
                values3.put("httpType", "http://")
                db.insert("NetData", SQLiteDatabase.CONFLICT_NONE, values3)


                /**
                 * 测试环境
                 */
                val values2 = ContentValues()
                values2.put("url", "192.168.1.77:7001")
                values2.put("description", "测试环境")
                values2.put("inserTime", 2L)
                values2.put("isCheck", false)
                values2.put("type", "http")
                values2.put("httpType", "http://")
                db.insert("NetData", SQLiteDatabase.CONFLICT_NONE, values2)

            }


            override fun onOpen(db: SupportSQLiteDatabase) {
                Log.i("TAG", "=================onOpen=================")
            }
        }
    }
}