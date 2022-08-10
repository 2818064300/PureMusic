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
import com.lie.puremusic.adapter.MyRecyclerGridAdapter2
import com.lie.puremusic.databinding.FragmentHomeBinding
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.utils.SpacesItemDecoration
import com.lie.puremusic.utils.SpacesItemDecoration2
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
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","Square")
            startActivity(intent)
        }
        Glide.with(this)
            .load(StaticData.user?.getAvatarUrl())
            .into(binding.Avatar)
        val SongList: MutableList<SongList?> = ArrayList()
        for (i in 0..5){
            SongList.add(StaticData.Home.getSongsLists()?.get(i))
        }
        val adapter = MyRecyclerGridAdapter(requireContext(), SongList, 1)
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.CardGroup.adapter = AlphaInAnimationAdapter(alphaAdapter)
        val layoutManager = GridLayoutManager(context, 3)
        layoutManager.orientation = RecyclerView.VERTICAL
        binding.CardGroup.layoutManager = layoutManager
        binding.CardGroup.addItemDecoration(SpacesItemDecoration())
        val Singer: MutableList<Singer?> = ArrayList()
        for (i in 0..4){
            Singer.add(StaticData.Home.getSingers()?.get(i))
        }
        val adapter2 = MyRecyclerGridAdapter2(requireContext(), Singer)
        val alphaAdapter2 = AlphaInAnimationAdapter(adapter2)
        binding.SingerGroup.adapter = AlphaInAnimationAdapter(alphaAdapter2)
        val layoutManager2 = GridLayoutManager(context, 5)
        layoutManager2.orientation = RecyclerView.VERTICAL
        binding.SingerGroup.layoutManager = layoutManager2
        binding.SingerGroup.addItemDecoration(SpacesItemDecoration2())


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