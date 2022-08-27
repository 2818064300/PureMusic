package com.lie.puremusic.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MyRecyclerGridAdapter
import com.lie.puremusic.adapter.MyRecyclerGridAdapter2
import com.lie.puremusic.databinding.FragmentHomeBinding
import com.lie.puremusic.music.netease.*
import com.lie.puremusic.music.netease.data.SongUrlData
import com.lie.puremusic.standard.data.StandardPlaylistData
import com.lie.puremusic.ui.activity.*
import com.lie.puremusic.ui.base.BaseFragment
import com.lie.puremusic.utils.MagicHttp.runOnMainThread
import com.lie.puremusic.utils.SpacesItemDecoration
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import java.io.File

class HomeFragment : BaseFragment() {

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

    override fun initView() {
        FileDownloader.setup(context)
        update()
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
        binding.Card7Iv.setOnClickListener {
            StaticData.Position = 0
            StaticData.Songs = StaticData.Cloud.get(0)
            StaticData.SongUrl = SongUrlData.UrlData(
                -1,
                "http://puremusic.com.cn/Cloud/Music/music" + 1 + ".mp3",
                -1,
                -1,
                "mp3"
            )
            ClickLocalMusic()
        }
        binding.Card8Iv.setOnClickListener {
            StaticData.Position = 1
            StaticData.Songs = StaticData.Cloud.get(1)
            StaticData.SongUrl = SongUrlData.UrlData(
                -1,
                "http://puremusic.com.cn/Cloud/Music/music" + 2 + ".mp3",
                -1,
                -1,
                "mp3"
            )
            ClickLocalMusic()
        }
        binding.Card9Iv.setOnClickListener {
            StaticData.Position = 2
            StaticData.Songs = StaticData.Cloud.get(2)
            StaticData.SongUrl = SongUrlData.UrlData(
                -1,
                "http://puremusic.com.cn/Cloud/Music/music" + 3 + ".mp3",
                -1,
                -1,
                "mp3"
            )
            ClickLocalMusic()
        }
        binding.Card10Iv.setOnClickListener {
            StaticData.Position = 3
            StaticData.Songs = StaticData.Cloud.get(3)
            StaticData.SongUrl = SongUrlData.UrlData(
                -1,
                "http://puremusic.com.cn/Cloud/Music/music" + 4 + ".mp3",
                -1,
                -1,
                "mp3"
            )
            ClickLocalMusic()
        }
        binding.ViewAll.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style", "Square")
            startActivity(intent)
        }
        Glide.with(this)
            .load(StaticData.user?.getAvatarUrl())
            .into(binding.Avatar)
    }

    private fun update() {
        // 推荐歌单
        refreshPlaylistRecommend()
        // 推荐歌手
        refreshSingerRecommend()
    }


    private fun refreshPlaylistRecommend() {
        runOnMainThread {
            val PlaylistResult = StaticData.PlaylistRecommend!!
            val SongList: ArrayList<PlaylistRecommend.PlaylistRecommendDataResult> = ArrayList()
            for (i in 0..5) {
                SongList.add(PlaylistResult.get(i))
            }
            val adapter = MyRecyclerGridAdapter(requireContext(), SongList)
            binding.CardGroup.adapter = adapter
            val layoutManager = GridLayoutManager(context, 3)
            layoutManager.orientation = RecyclerView.VERTICAL
            binding.CardGroup.layoutManager = layoutManager
            binding.CardGroup.addItemDecoration(SpacesItemDecoration())

            binding.RefreshLayout.setOnRefreshListener { refreshLayout ->
                if (StaticData.offset < PlaylistResult.size / 6 - 1) {
                    StaticData.offset += 1
                } else {
                    StaticData.offset = 0
                }
                SongList.set(0, PlaylistResult.get(0 + StaticData.offset * 6))
                SongList.set(1, PlaylistResult.get(1 + StaticData.offset * 6))
                SongList.set(2, PlaylistResult.get(2 + StaticData.offset * 6))
                SongList.set(3, PlaylistResult.get(3 + StaticData.offset * 6))
                SongList.set(4, PlaylistResult.get(4 + StaticData.offset * 6))
                SongList.set(5, PlaylistResult.get(5 + StaticData.offset * 6))
                for (i in 0 until adapter.itemCount) {
                    adapter.notifyItemChanged(i)
                }
                refreshLayout.finishRefresh()
            }
        }
    }

    private fun refreshSingerRecommend() {
        SingerRecommend.getSingerRecommend(requireContext()) {
            runOnMainThread {
                val Singer: ArrayList<SingerRecommend.SingerRecommendDataResult> = ArrayList()
                for (i in 0..4) {
                    Singer.add(it.get(i))
                }
                val adapter2 = MyRecyclerGridAdapter2(requireContext(), Singer)
                binding.SingerGroup.adapter = adapter2
                val layoutManager2 = GridLayoutManager(context, 5)
                layoutManager2.orientation = RecyclerView.VERTICAL
                binding.SingerGroup.layoutManager = layoutManager2
            }
        }
    }

    private fun ClickLocalMusic() {
        StaticData.PlayListData = StandardPlaylistData(
            -1,
            "云端歌曲",
            "https://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg",
            0,
            StaticData.Cloud
        )
        val path =
            Environment.getExternalStorageDirectory().path + "/PureMusic/Music/" + "Cloud" + "/" + StaticData.Songs?.id
        if (!File("$path.mp3").exists()) {
            Toasty.info(MainActivity.context, "开始缓存歌曲.", Toast.LENGTH_SHORT, true).show()
            Thread {
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
                                "缓存成功",
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
                                "网络问题,请稍后再试!",
                                Toast.LENGTH_SHORT,
                                true
                            ).show()
                        }

                        override fun warn(task: BaseDownloadTask) {}
                    })
                    .start()
            }.start()
        }
        StaticData.isFirstPlay = true
        val intent = Intent(activity, PlayerActivity::class.java)
        StaticData.Style = "Cloud"
        startActivity(intent)
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