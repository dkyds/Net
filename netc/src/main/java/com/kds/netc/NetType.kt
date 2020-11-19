package com.kds.netc

/**
 * @author kyp
 * @date 2020/11/18
 * @desc 网络切换
 */
enum class NetType(val netName: String, val code: String) {
    HTTP("网络数据", "http"),
    WEB("web数据", "web")

}