package com.kds.netc.room

import androidx.room.*

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
@Dao
interface NetDao {

    @Insert
    fun insertNet(data: NetData)

    @Query("delete from NetData where type =:type")
    fun deleteForType(type: String)

    @Update
    fun updateNet(data: NetData)

    @Update
    fun upDateNets(list: MutableList<NetData>)

    @Query("select * from NetData where type =:type")
    fun getNetDataList(type: String): MutableList<NetData>


    @Delete
    fun deleteData(data: NetData): Int


}