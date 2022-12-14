package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.service.ServiceSongUrl
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.standard.data.StandardSongDataEx
import com.lie.puremusic.ui.activity.MainActivity
import com.lie.puremusic.ui.activity.PlayerActivity
import com.lie.puremusic.utils.parse
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors

class MyRecyclerGridAdapter3(
    private val context: Context,
    private val newSongDataResult: ArrayList<StandardSongData>,
) : RecyclerView.Adapter<MyRecyclerGridAdapter3.InnerHolder>() {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCover: ImageView = view.findViewById(R.id.ivCover_bg)
        val Card_tv1: TextView = view.findViewById(R.id.Card_tv1)
        val Card_tv2: TextView = view.findViewById(R.id.Card_tv2)
        var Vibrant: Palette.Swatch? = null
        var VibrantLight: Palette.Swatch? = null
        var VibrantDark: Palette.Swatch? = null
        var Muted: Palette.Swatch? = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        FileDownloader.setup(parent.context)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item3, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        with(holder) {
            val song = newSongDataResult[position]
            Glide.with(context)
                .load(song.imageUrl)
                .centerCrop()
                .into(ivCover)
            Card_tv1.text = song.name
            Card_tv2.text = song?.artists?.parse()
            ivCover.setOnClickListener {
                if (song.id != null) {
                    val path =
                        Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + "Netease" + "/" + song.id
                    if (!File("$path.flac").exists() && !File("$path.mp3").exists()) {
                        Toasty.info(
                            MainActivity.context,
                            "??????????????????.",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                        Thread {
                            val executorService =
                                Executors.newCachedThreadPool()
                            executorService.execute {
                                ServiceSongUrl.getUrl(song) {
                                    StaticData.SongUrl = it
                                    FileDownloader.getImpl().create(StaticData.SongUrl?.url)
                                        .setPath(path + "." + StaticData.SongUrl?.type)
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
                                                Toasty.success(
                                                    MainActivity.context,
                                                    "????????????",
                                                    Toast.LENGTH_SHORT,
                                                    true
                                                ).show()
                                            }

                                            override fun paused(
                                                task: BaseDownloadTask,
                                                soFarBytes: Int,
                                                totalBytes: Int
                                            ) {
                                            }

                                            override fun error(task: BaseDownloadTask, e: Throwable) {
                                                Toasty.error(
                                                    MainActivity.context,
                                                    "????????????,???????????????!",
                                                    Toast.LENGTH_SHORT,
                                                    true
                                                ).show()
                                            }

                                            override fun warn(task: BaseDownloadTask) {}
                                        })
                                        .start()
                                }
                            }
                            executorService.shutdown()
                        }.start()
                    }
                    Thread {
                        StaticData.Songs = song
                        val executorService = Executors.newCachedThreadPool()
                        executorService.execute {
                            val p: Palette? =
                                getBitmapGlide(StaticData.Songs?.imageUrl)?.let { it1 ->
                                    Palette.from(it1)
                                        .generate()
                                }
                            Vibrant = p?.getVibrantSwatch()
                            VibrantLight = p?.getLightVibrantSwatch()
                            VibrantDark = p?.getDarkVibrantSwatch()
                            Muted = p?.getMutedSwatch()
                            StaticData.PlayDataEx = StandardSongDataEx(
                                song.id,
                                Vibrant,
                                VibrantLight,
                                VibrantDark,
                                Muted
                            )
                            ServiceSongUrl.getLyric(song) {
                                StaticData.SongLrc = it
                                StaticData.isFirstPlay = true
                                StaticData.Position = position
                                StaticData.Style = "TopList"
                                context.startActivity(Intent(context, PlayerActivity::class.java))
                            }
                        }
                        executorService.shutdown()
                    }.start()
                }
            }
        }
    }

    fun getBitmapGlide(url: String?): Bitmap? {
        try {
            return Glide.with(context.applicationContext)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: ExecutionException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return null
    }

    override fun getItemCount(): Int {
        return newSongDataResult.size
    }
}