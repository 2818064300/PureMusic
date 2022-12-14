package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.os.Vibrator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.pojo.Record
import com.lie.puremusic.service.ServiceSongUrl
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.standard.data.StandardSongDataEx
import com.lie.puremusic.ui.activity.LoadingActivity
import com.lie.puremusic.ui.activity.MainActivity
import com.lie.puremusic.ui.activity.PlayerActivity
import com.lie.puremusic.utils.parse
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import de.hdodenhof.circleimageview.CircleImageView
import es.dmoral.toasty.Toasty
import java.io.File
import java.lang.Exception
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executors


class MyRecyclerAdapter(
    private val context: Context,
    private val List: ArrayList<StandardSongData>?,
    private val Style: String
) :
    RecyclerView.Adapter<MyRecyclerAdapter.InnerHolder>() {

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivCover: CircleImageView
        var tvTitle: TextView
        var tvSub: TextView
        var mark: ImageButton
        var ibtn: ImageButton
        var pop: TextView
        var tv3: TextView
        var Vibrant: Palette.Swatch? = null
        var VibrantLight: Palette.Swatch? = null
        var VibrantDark: Palette.Swatch? = null
        var Muted: Palette.Swatch? = null

        init {
            ivCover = view.findViewById(R.id.ivCover)
            tvTitle = view.findViewById(R.id.tvTitle)
            tvSub = view.findViewById(R.id.tvSub)
            tv3 = view.findViewById(R.id.tv3)
            mark = view.findViewById(R.id.mark)
            ibtn = view.findViewById(R.id.ibtn)
            pop = view.findViewById(R.id.pop)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        FileDownloader.setup(parent.context)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return InnerHolder(view)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        with(holder) {
            setAnimation(holder.itemView, position)
            val song = List?.get(position)
            tvTitle.setText(song?.name)
            pop.setText(song?.neteaseInfo?.pop.toString())
            if (song?.neteaseInfo?.fee == 1) {
                holder.tv3.text = "VIP ??????"
            }
            if (song?.neteaseInfo?.fee == 4) {
                holder.tv3.text = "????????????"
            }
            if (song?.neteaseInfo?.maxbr == 8) {
                holder.tv3.text = "????????????"
            }
            Glide.with(context)
                .load(song?.imageUrl)
                .into(holder.ivCover)
            ivCover.setOnLongClickListener {
                if(StaticData.mmkv.decodeBool("QuickSearch")) {
                    Toasty.info(
                        MainActivity.context,
                        "????????????????????????  " + "\"" + song?.name + "\"",
                        Toast.LENGTH_SHORT,
                        true
                    ).show()

                    val intent = Intent(context, LoadingActivity::class.java)
                    intent.putExtra("style", "Search")
                    StaticData.KeyWords = song?.name
                    StaticData.Records.add(Record(StaticData.SelectID, song?.name))
                    StaticData.Root = "???????????????"
                    //??????
                    getSystemService(context, Vibrator::class.java)?.vibrate(100)
                    context.startActivity(intent)
                }
                true
            }
            tvSub.text = song?.artists?.parse()
            ibtn.setOnClickListener {
                if (song?.id != null && !song.imageUrl.equals("https://p2.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg")) {
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
                            val executorService = Executors.newCachedThreadPool()
                            executorService.execute {
                                ServiceSongUrl.getUrl(song) {
                                    StaticData.SongUrl = it
                                    println("????????? " + it?.br)
                                    println("???????????? " + it?.level)
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
                                if (Style.equals("SongList")) {
                                    StaticData.Style = "SongList"
                                }
                                if (Style.equals("SingerList")) {
                                    StaticData.Style = "SingerList"
                                }
                                if (Style.equals("Search")) {
                                    StaticData.Style = "Search"
                                }
                                if (Style.equals("DailyRecommend")) {
                                    StaticData.Style = "DailyRecommend"
                                }
                                if (Style.equals("UserCloud")) {
                                    StaticData.Style = "UserCloud"
                                }
                                context.startActivity(Intent(context, PlayerActivity::class.java))
                            }
                        }
                        executorService.shutdown()
                    }.start()
                }
            }
//            if (song.mark == true) {
//                holder.mark.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        context.applicationContext,
//                        R.drawable.mark_pressed
//                    )
//                )
//            } else {
//                holder.mark.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        context.applicationContext,
//                        R.drawable.mark
//                    )
//                )
//            }
        }
//        holder.mark.setOnClickListener {
//            if (List[position]?.isMark() == false) {
//                List[position]?.setMark(true)
//                Toasty.custom(
//                    context,
//                    "????????????",
//                    R.mipmap.ic_launcher_round,
//                    R.color.material_red_300,
//                    750,
//                    false,
//                    true
//                ).show()
//                holder.mark.setImageDrawable(
//                    ContextCompat.getDrawable(
//                        context.applicationContext,
//                        R.drawable.mark_pressed
//                    )
//                )
//            } else {
//                List[position]?.setMark(false)
//                Toasty.custom(
//                    context,
//                    "????????????",
//                    R.mipmap.ic_launcher_round,
//                    R.color.material_red_300,
//                    750,
//                    false,
//                    true
//                ).show()
        holder.mark.setImageDrawable(
            ContextCompat.getDrawable(
                context.applicationContext,
                R.drawable.mark
            )
        )
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        // If the bound view wasn't previously displayed on screen, it's animated
        val animation: Animation =
            AnimationUtils.loadAnimation(viewToAnimate.context, R.anim.anim_recycle_item)
        viewToAnimate.startAnimation(animation)
    }

    override fun getItemCount(): Int {
        return List?.size!!
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