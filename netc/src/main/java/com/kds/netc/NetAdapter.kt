package com.kds.netc

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kds.netc.room.NetData

/**
 * @author kyp
 * @date 2020/11/18
 * @desc 网络切换adapter
 */
class NetAdapter(context: Context, list: MutableList<NetData>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context = context
    private var mNetList: MutableList<NetData> = mutableListOf()
    private var emptyIcon: Int? = null
    private var lastCheckData: NetData? = null
    private var emptyStr: String? = null
    private var listener: NetItemListener? = null

    init {
        mNetList.addAll(list)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(mContext).inflate(R.layout.item_netkds_empty, parent, false)
                EmptyHolder(view)
            }

            3 -> {
                val view =
                        LayoutInflater.from(mContext).inflate(R.layout.item_netkds_net_foot, parent, false)
                FootHolder(view)
            }

            else -> {
                val view =
                        LayoutInflater.from(mContext).inflate(R.layout.item_netkds_net_chenge, parent, false)
                ViewHolder(view)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyHolder -> holder.init(emptyIcon, emptyStr)
            is ViewHolder -> holder.init(position, mNetList[position])
            is FootHolder -> holder.init()
        }
    }

    override fun getItemCount(): Int {
        return mNetList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            mNetList.size == 0 -> 1 //空布局
            position > 0 && position < mNetList.size -> 2 //正常布局
            position == mNetList.size -> 3 // 添加
            else -> 4
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val netNameTv: TextView = view.findViewById(R.id.netNameTv)
        private val deleteTv: TextView = view.findViewById(R.id.deleteTv)
        private val modifyTv: TextView = view.findViewById(R.id.modifyTv)
        private val netCl: ConstraintLayout = view.findViewById(R.id.netCl)
        private val checkIv: ImageView = view.findViewById(R.id.checkIv)


        @SuppressLint("SetTextI18n")
        fun init(position: Int, data: NetData) {
            if (data.isCheck) {
                lastCheckData = data
                checkIv.isVisible = true
            } else {
                checkIv.isVisible = false
            }
            netNameTv.text = if (data.description.isNullOrEmpty()) "http://${data.url}:${data.port}/" else data.description
            deleteTv.setOnClickListener {
                listener?.deleteItem(position, data)
            }

            modifyTv.setOnClickListener {
                listener?.onModifyItem(adapterPosition, data)
            }

            netCl.setOnClickListener {
                check(data)
            }
        }


        private fun check(data: NetData) {
            data.isCheck = true
            if (lastCheckData != null) {
                lastCheckData?.isCheck = false
            }
            listener?.onCheckItem(adapterPosition, data, lastCheckData)
            lastCheckData = data
            notifyDataSetChanged()
        }
    }


    inner class EmptyHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val emptyTv: TextView = view.findViewById(R.id.emptyTv)
        private val emptyIv: ImageView = view.findViewById(R.id.emptyIv)
        private val addBt: Button = view.findViewById(R.id.addBt)


        fun init(emptyIcon: Int?, emptyStr: String?) {
            if (emptyIcon != null) {
                emptyIv.setImageResource(emptyIcon)
            }

            if (!emptyStr.isNullOrEmpty()) {
                emptyTv.text = emptyStr
            }

            addBt.setOnClickListener {
                listener?.addItem()
            }
        }
    }


    inner class FootHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val addTv: TextView = view.findViewById(R.id.addTv)


        fun init() {
            addTv.setOnClickListener {
                listener?.addItem()
            }
        }
    }

    /**
     * 清空数据
     */
    fun clearNetList() {
        notifyItemRangeRemoved(0, mNetList.size)
        mNetList.clear()
    }

    /**
     * 添加数据
     */
    fun addNetItem(data: NetData) {
        notifyItemRangeInserted(0, 1)
        mNetList.add(0, data)
    }

    /**
     * 删除数据
     */
    fun deleteItem(data: NetData) {
        val index = mNetList.indexOf(data)
        notifyItemRangeRemoved(index, 1)
        mNetList.remove(data)
    }

    fun updateItem(position: Int, data: NetData) {
        mNetList[position] = data
        notifyItemRangeChanged(position, 1)
    }

    /**
     * 设置空数据时 显示
     * @param image 空布局图片
     * @param content 空布局文字
     */
    fun setEmptyUi(image: Int? = null, content: String? = null) {
        emptyIcon = image
        emptyStr = content
    }


    interface NetItemListener {

        fun onModifyItem(position: Int, data: NetData)


        fun onCheckItem(position: Int, data: NetData, lastData: NetData?)


        fun addItem()

        fun deleteItem(position: Int, data: NetData)
    }


    fun setListener(listener: NetItemListener) {
        this.listener = listener
    }


}