package com.lie.puremusic.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.*
import com.lie.puremusic.activity.*
import com.lie.puremusic.adapter.MyRecyclerGridAdapter
import com.lie.puremusic.databinding.FragmentHomeBinding
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.utils.SpacesItemDecoration
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private var count = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        Thread {
            while (true) {
                count++
                Thread.sleep(10)
            }
        }.start()
        binding.Avatar.setOnClickListener {
            startActivity(Intent(activity, InfoActivity::class.java))
        }
        binding.Search.setOnClickListener {
            startActivity(
                Intent(activity, PreSearchActivity::class.java)
            )
        }
        binding.CircleImageView1.setOnClickListener {
            StaticData.Singer_ID = 0
            StaticData.Style = "SingerList"
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getId()
                )
            ) {
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList =
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
                intent = Intent(activity, SingerActivity::class.java)
            }
            startActivity(intent)
        }
        binding.CircleImageView2.setOnClickListener {
            StaticData.Singer_ID = 1
            StaticData.Style = "SingerList"
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getId()
                )
            ) {
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList =
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
                intent = Intent(activity, SingerActivity::class.java)
            }
            startActivity(intent)
        }
        binding.CircleImageView3.setOnClickListener {
            StaticData.Singer_ID = 2
            StaticData.Style = "SingerList"
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getId()
                )
            ) {
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList =
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
                intent = Intent(activity, SingerActivity::class.java)
            }
            startActivity(intent)
        }
        binding.CircleImageView4.setOnClickListener {
            StaticData.Singer_ID = 3
            StaticData.Style = "SingerList"
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getId()
                )
            ) {
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList =
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
                intent = Intent(activity, SingerActivity::class.java)
            }
            startActivity(intent)
        }
        binding.CircleImageView5.setOnClickListener {
            StaticData.Singer_ID = 4
            StaticData.Style = "SingerList"
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getId()
                )
            ) {
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList =
                    StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
                intent = Intent(activity, SingerActivity::class.java)
            }
            startActivity(intent)
        }
        binding.Card7Iv.setOnClickListener {
            StaticData.Songs = StaticData.Cloud.get(0)
            ClickLocalMusic()
        }
        binding.Card8Iv.setOnClickListener {
            StaticData.Songs = StaticData.Cloud.get(1)
            ClickLocalMusic()
        }
        binding.Card9Iv.setOnClickListener {
            StaticData.Songs = StaticData.Cloud.get(2)
            ClickLocalMusic()
        }
        binding.Card10Iv.setOnClickListener {
            StaticData.Songs = StaticData.Cloud.get(3)
            ClickLocalMusic()
        }
        binding.ViewAll.setOnClickListener {
            StaticData.Style = "Square"
            if (!StaticData.Containr.contains("歌单广场")) {
                val intent = Intent(activity, LoadingActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(activity, SquareActivity::class.java)
                startActivity(intent)
            }
        }
        val SongList: MutableList<SongList?> = ArrayList()
        SongList.add(StaticData.Home.getSongsLists()?.get(0))
        SongList.add(StaticData.Home.getSongsLists()?.get(1))
        SongList.add(StaticData.Home.getSongsLists()?.get(2))
        SongList.add(StaticData.Home.getSongsLists()?.get(3))
        SongList.add(StaticData.Home.getSongsLists()?.get(4))
        SongList.add(StaticData.Home.getSongsLists()?.get(5))
        val adapter = MyRecyclerGridAdapter(requireContext(), SongList, 1)
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.CardGroup.adapter = AlphaInAnimationAdapter(alphaAdapter)
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.CardGroup.layoutManager = layoutManager
        binding.CardGroup.addItemDecoration(SpacesItemDecoration())
        binding.RefreshLayout.setOnRefreshListener { refreshLayout ->
            if (StaticData.offset < StaticData.Home.getSongsLists()?.size?.div(6)?.minus(1)!!) {
                StaticData.offset += 1
            } else {
                StaticData.offset = 0
            }
            SongList.set(0, StaticData.Home.getSongsLists()?.get(0 + StaticData.offset * 6))
            SongList.set(1, StaticData.Home.getSongsLists()?.get(1 + StaticData.offset * 6))
            SongList.set(2, StaticData.Home.getSongsLists()?.get(2 + StaticData.offset * 6))
            SongList.set(3, StaticData.Home.getSongsLists()?.get(3 + StaticData.offset * 6))
            SongList.set(4, StaticData.Home.getSongsLists()?.get(4 + StaticData.offset * 6))
            SongList.set(5, StaticData.Home.getSongsLists()?.get(5 + StaticData.offset * 6))
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            Thread {
                val request: Request = Request.Builder()
                    .url("https://v1.hitokoto.cn")
                    .build()
                val response = OkHttpClient().newCall(request).execute()
                val jsonObject = response.body?.string()?.let { JSONObject(it) }
                StaticData.hitokoto.setHitokoto(jsonObject?.getString("hitokoto"))
                StaticData.hitokoto.setFrom(jsonObject?.getString("from"))
                StaticData.hitokoto.setFrom_who(jsonObject?.getString("from_who"))
            }.start()
            refreshLayout.finishRefresh()
        }
        binding.Label.setOnClickListener { }
    }

    private fun ClickLocalMusic() {
        StaticData.PlayList = StaticData.Cloud
        val path =
            Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + StaticData.Songs?.getStyle() + "/" + StaticData.Songs?.getId()
        if (!File("$path.mp3").exists()) {
            Thread {
                FileDownloader.getImpl().create(StaticData.Songs?.getMusic_url())
                    .setPath(path + "." + StaticData.Songs?.getType())
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
                            Toasty.success(context!!, "缓存成功.", Toast.LENGTH_SHORT, true).show()
                        }

                        override fun paused(
                            task: BaseDownloadTask,
                            soFarBytes: Int,
                            totalBytes: Int
                        ) {
                        }

                        override fun error(task: BaseDownloadTask, e: Throwable) {
                            Toasty.error(context!!, "网络问题,请稍后再试!", Toast.LENGTH_SHORT, true).show()
                        }

                        override fun warn(task: BaseDownloadTask) {}
                    })
                    .start()
            }.start()
        }
        val intent = Intent(activity, PlayerActivity::class.java)
        startActivity(intent)
    }

    private fun init() {
        Glide.with(this)
            .load(StaticData.Home.getSingers()?.get(0)?.getCover_url() + "?param=200y200")
            .into(binding.CircleImageView1)
        Glide.with(this)
            .load(StaticData.Home.getSingers()?.get(1)?.getCover_url() + "?param=200y200")
            .into(binding.CircleImageView2)
        Glide.with(this)
            .load(StaticData.Home.getSingers()?.get(2)?.getCover_url() + "?param=200y200")
            .into(binding.CircleImageView3)
        Glide.with(this)
            .load(StaticData.Home.getSingers()?.get(3)?.getCover_url() + "?param=200y200")
            .into(binding.CircleImageView4)
        Glide.with(this)
            .load(StaticData.Home.getSingers()?.get(4)?.getCover_url() + "?param=200y200")
            .into(binding.CircleImageView5)
        Glide.with(this)
            .load(StaticData.user?.getAvatarUrl() + "?param=200y200")
            .into(binding.Avatar)
        binding.CircleImageView1TextView.setText(StaticData.Home.getSingers()?.get(0)?.getName())
        binding.CircleImageView2TextView.setText(StaticData.Home.getSingers()?.get(1)?.getName())
        binding.CircleImageView3TextView.setText(StaticData.Home.getSingers()?.get(2)?.getName())
        binding.CircleImageView4TextView.setText(StaticData.Home.getSingers()?.get(3)?.getName())
        binding.CircleImageView5TextView.setText(StaticData.Home.getSingers()?.get(4)?.getName())
    }

    override fun onResume() {
        super.onResume()
        if (count > 4500) {
            count = 0
            binding.RefreshLayout.autoRefresh()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}