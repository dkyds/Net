package com.kds.netc.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
@Database(entities = [NetData::class], version = 1)
abstract class NetDatabase : RoomDatabase(){
    abstract fun netDao(): NetDao
}