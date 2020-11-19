package com.kds.netc.room

import android.content.Context
import androidx.room.Room

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
class DbUtil {

    companion object {
        @Volatile
        private var INSTANCE: NetDatabase? = null

        fun getInstance(context: Context): NetDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: getRoom(context).also { INSTANCE = it }
            }


        private fun getRoom(context: Context): NetDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                NetDatabase::class.java,
                "netRoom"
            ).build()
        }

    }
}