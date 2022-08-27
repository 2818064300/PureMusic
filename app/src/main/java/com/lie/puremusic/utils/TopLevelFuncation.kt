package com.lie.puremusic.utils

import android.os.Handler
import android.os.Looper

/**
 * Kotlin 顶层函数
 */


/**
 * 运行在主线程，更新 UI
 */
fun runOnMainThread(runnable: Runnable) {
    Handler(Looper.getMainLooper()).post(runnable)
}

/**
 * 获取系统当前时间
 */
fun getCurrentTime() : Long {
    return System.currentTimeMillis()
}

/**
 * 毫秒转日期
 */
fun msTimeToFormatDate(msTime: Long): String {
    return TimeUtil.msTimeToFormatDate(msTime)
}


var lastClickTime = 0L
fun singleClick(during: Long = 200L, callBack: () -> Unit) {
    if (getCurrentTime() - lastClickTime > during) {
        callBack()
    }
    lastClickTime = getCurrentTime()
}