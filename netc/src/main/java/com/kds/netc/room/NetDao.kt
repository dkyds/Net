package com.kds.netc.room

import androidx.room.*

/**
 * @author kyp
 * @date 2020/11/18
 * @desc
 */
@Dao
interface NetDao {

    /**
     * 新增
     */
    @Insert
    fun insertNet(data: NetData)

    /**
     * 根据type 删除数据
     */
    @Query("delete from NetData where type =:type")
    fun deleteForType(type: String)

    /**
     * 更新数据
     */
    @Update
    fun updateNet(data: NetData)

    /**
     * 更新数据
     */
    @Update
    fun upDateNets(list: MutableList<NetData>)

    /**
     * 查询数据
     */
    @Query("select * from NetData where type =:type")
    fun getNetDataList(type: String): MutableList<NetData>


    /**
     * 删除数据
     */
    @Delete
    fun deleteData(data: NetData): Int


}