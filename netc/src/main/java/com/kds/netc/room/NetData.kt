package com.kds.netc.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author kyp
 * @date 2020/11/18
 * @desc 网络切换
 */
@Entity
class NetData(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var url: String,
    var port: String,
    var type: String,
    var isCheck: Boolean
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(url)
        parcel.writeString(port)
        parcel.writeString(type)
        parcel.writeByte(if (isCheck) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NetData> {
        override fun createFromParcel(parcel: Parcel): NetData {
            return NetData(parcel)
        }

        override fun newArray(size: Int): Array<NetData?> {
            return arrayOfNulls(size)
        }
    }


}