package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.activity.PlayerActivity
import com.lie.puremusic.pojo.Record
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.utils.GetLyricData
import com.lie.puremusic.utils.GetMusicData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MyRecyclerAdapter(private val context: Context, List: MutableList<Song?>) :
    RecyclerView.Adapter<MyRecyclerAdapter.InnerHolder>() {
    private var List: MutableList<Song?> = ArrayList()

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        FileDownloader.setup(parent.context)
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return InnerHolder(view)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tv1.setText(List[position]?.getName())
        holder.pop.setText(List[position]?.getPop().toString())
        var SingerName = ""
        for (i in 0 until List[position]?.getSingers()?.size!!) {
            if (i == 0) {
                SingerName += List[position]?.getSingers()?.get(i)?.getName()
            } else {
                SingerName += "/" + List[position]?.getSingers()?.get(i)?.getName()
            }
        }
        holder.tv2.text = SingerName
        if (List[position]?.getFee() == 1) {
            holder.tv3.text = "VIP 歌曲"
        }
        if (List[position]?.getFee() == 4) {
            holder.tv3.text = "数字专辑"
        }
        if (List[position]?.getFee() == 8) {
            holder.tv3.text = "无损歌曲"
        }
        if (List[position]?.getFee() == 9) {
            holder.tv3.text = "QQ 音乐"
        }
        if (List[position]?.isMark() == true) {
            holder.mark.setImageDrawable(
                ContextCompat.getDrawable(
                    context.applicationContext,
                    R.drawable.mark_pressed
                )
            )
        } else {
            holder.mark.setImageDrawable(
                ContextCompat.getDrawable(
                    context.applicationContext,
                    R.drawable.mark
                )
            )
        }
        val url: String? = List[position]?.getCover_url()
        Glide.with(context)
            .load(url)
            .into(holder.avatar)
        holder.avatar.setOnLongClickListener {
            Toasty.info(
                context,
                "正在寻找类似歌曲  " + "\"" + List[position]?.getName() + "\"",
                Toast.LENGTH_SHORT,
                true
            ).show()

            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","Search")
            StaticData.KeyWords = List[position]?.getName()
            StaticData.Records.add(Record(StaticData.SelectID, List[position]?.getName()))
            if (List[position]?.getStyle().equals("网易云音乐")) {
                StaticData.Root = "网易云音乐"
            } else {
                StaticData.Root = "QQ音乐"
            }
            context.startActivity(intent)
            false
        }
        holder.ibtn.setOnClickListener {
            if (List[position]?.getId() != null) {
                val path =
                    Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + List[position]?.getStyle() + "/" + List[position]?.getId()
                val file = File("$path.flac")
                val file2 = File("$path.mp3")
                if (!file.exists() && !file2.exists()) {
                    Toasty.info(context, "开始缓存歌曲.", Toast.LENGTH_SHORT, true).show()
                    Thread {
                        val executorService =
                            Executors.newCachedThreadPool()
                        executorService.execute(
                            GetMusicData(
                                List[position], List[position]?.getId(), "flac",
                                context
                            )
                        )
                        executorService.shutdown()
                        try {
                            executorService.awaitTermination(
                                Long.MAX_VALUE,
                                TimeUnit.NANOSECONDS
                            )
                            FileDownloader.getImpl().create(List[position]?.getMusic_url())
                                .setPath(path + "." + List[position]?.getType())
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
                                            context,
                                            "缓存成功.",
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

                                    override fun error(
                                        task: BaseDownloadTask,
                                        e: Throwable
                                    ) {
                                        Toasty.error(
                                            context,
                                            "网络问题,请稍后再试!",
                                            Toast.LENGTH_SHORT,
                                            true
                                        ).show()
                                    }

                                    override fun warn(task: BaseDownloadTask) {}
                                })
                                .start()
                        } catch (e: InterruptedException) {
                            e.printStackTrace()
                        }
                    }.start()
                }
                Thread {
                    StaticData.Songs = List[position]
                    val executorService = Executors.newCachedThreadPool()
                    executorService.execute(
                        GetLyricData(
                            StaticData.Songs,
                            StaticData.Songs?.getId()
                        )
                    )
                    executorService.execute {
                        val p: Palette? =
                            getBitmapGlide(StaticData.Songs?.getCover_url())?.let { it1 ->
                                Palette.from(it1)
                                    .generate()
                            }
                        StaticData.Songs?.setVibrant(p?.getVibrantSwatch())
                        StaticData.Songs?.setVibrantLight(p?.getLightVibrantSwatch())
                        StaticData.Songs?.setVibrantDark(p?.getDarkVibrantSwatch())
                        StaticData.Songs?.setMuted(p?.getMutedSwatch())
                    }
                    executorService.shutdown()
                    try {
                        executorService.awaitTermination(
                            Long.MAX_VALUE,
                            TimeUnit.NANOSECONDS
                        )
                        StaticData.isFirstPlay = true
                        context.startActivity(Intent(context, PlayerActivity::class.java))
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                }.start()
            }
        }
        holder.mark.setOnClickListener {
            if (List[position]?.isMark() == false) {
                List[position]?.setMark(true)
                Toasty.custom(
                    context,
                    "收藏歌曲",
                    R.mipmap.ic_launcher_round,
                    R.color.material_red_300,
                    750,
                    false,
                    true
                ).show()
                holder.mark.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext,
                        R.drawable.mark_pressed
                    )
                )
            } else {
                List[position]?.setMark(false)
                Toasty.custom(
                    context,
                    "取消收藏",
                    R.mipmap.ic_launcher_round,
                    R.color.material_red_300,
                    750,
                    false,
                    true
                ).show()
                holder.mark.setImageDrawable(
                    ContextCompat.getDrawable(
                        context.applicationContext,
                        R.drawable.mark
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        if(List.size<5){
            return 5
        }
        return List.size
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var avatar: CircleImageView
        var tv1: TextView
        var tv2: TextView
        var mark: ImageButton
        var ibtn: ImageButton
        var pop: TextView
        var tv3: TextView

        init {
            avatar = view.findViewById(R.id.avatar)
            tv1 = view.findViewById(R.id.tv1)
            tv2 = view.findViewById(R.id.tv2)
            tv3 = view.findViewById(R.id.tv3)
            mark = view.findViewById(R.id.mark)
            ibtn = view.findViewById(R.id.ibtn)
            pop = view.findViewById(R.id.pop)
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
}