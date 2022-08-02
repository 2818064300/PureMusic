package com.lie.puremusic.utils

import android.content.Context
import android.widget.Toast
import com.lie.puremusic.StaticData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class DownloadUtils(private val context: Context) {
    init {
        FileDownloader.setup(context)
    }

    fun download(index: Int, path: String, type: String) {
        Thread {
            val executorService = Executors.newCachedThreadPool()
            executorService.execute(
                StaticData.PlayList_now.get(index)?.getId()?.let {
                    GetMusicData(
                        StaticData.PlayList_now.get(index),
                        it
                    )
                }
            )
            executorService.shutdown()
            try {
                executorService.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
                )
                FileDownloader.getImpl().create(StaticData.PlayList_now.get(index)?.getMusic_url())
                    .setPath("$path.$type")
                    .setListener(object : FileDownloadListener() {
                        override fun pending(
                            task: BaseDownloadTask,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                        }

                        override fun progress(
                            task: BaseDownloadTask,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                        }

                        override fun completed(task: BaseDownloadTask) {
                            Toasty.success(context, "缓存成功.", Toast.LENGTH_SHORT, true).show()
                        }

                        override fun paused(
                            task: BaseDownloadTask,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                        }

                        override fun error(task: BaseDownloadTask, e: Throwable) {
                            Toasty.error(context, "网络问题,请稍后再试!", Toast.LENGTH_SHORT, true).show()
                        }

                        override fun warn(task: BaseDownloadTask) {}
                    })
                    .start()
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }.start()
    }
}