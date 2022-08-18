package com.lie.puremusic.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.activity.PreSearchActivity
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.adapter.MyRecyclerGridAdapter3
import com.lie.puremusic.databinding.FragmentLeaderboardBinding
import com.lie.puremusic.music.netease.NewSong
import com.lie.puremusic.music.netease.TopList
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.MagicHttp
import com.lie.puremusic.utils.SpacesItemDecoration2


class LeaderboardFragment : Fragment() {

    private var _binding: FragmentLeaderboardBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLeaderboardBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchLeaderboardIbtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, PreSearchActivity::class.java)
            startActivity(intent)
        })
        NewSong.getNewSong(requireContext()) {
            MagicHttp.runOnMainThread {
                val NewSongList: ArrayList<StandardSongData> = ArrayList()
                for (i in 0..5) {
                    NewSongList.add(it.get(i))
                }
                val adapter = MyRecyclerGridAdapter3(requireContext(), NewSongList)
                binding.NewSongGroup.adapter = adapter
                val layoutManager = GridLayoutManager(context, 1)
                layoutManager.orientation = RecyclerView.HORIZONTAL
                binding.NewSongGroup.layoutManager = layoutManager
                binding.NewSongGroup.addItemDecoration(SpacesItemDecoration2())
            }
        }
        TopList.getTopList(requireContext()){
            val TopList = it
            binding.toplistItem1.setOnClickListener {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "SongList")
                intent.putExtra("id", TopList.list.get(0).id)
                intent.putExtra("picUrl", TopList.list.get(0).coverImgUrl)
                intent.putExtra("name", TopList.list.get(0).name)
                intent.putExtra("playCount", TopList.list.get(0).playCount)
                context?.startActivity(intent)
            }
            binding.toplistItem2.setOnClickListener {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "SongList")
                intent.putExtra("id", TopList.list.get(1).id)
                intent.putExtra("picUrl", TopList.list.get(1).coverImgUrl)
                intent.putExtra("name", TopList.list.get(1).name)
                intent.putExtra("playCount", TopList.list.get(1).playCount)
                context?.startActivity(intent)
            }
            binding.toplistItem3.setOnClickListener {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "SongList")
                intent.putExtra("id", TopList.list.get(2).id)
                intent.putExtra("picUrl", TopList.list.get(2).coverImgUrl)
                intent.putExtra("name", TopList.list.get(2).name)
                intent.putExtra("playCount", TopList.list.get(2).playCount)
                context?.startActivity(intent)
            }
            binding.toplistItem4.setOnClickListener {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "SongList")
                intent.putExtra("id", TopList.list.get(3).id)
                intent.putExtra("picUrl", TopList.list.get(3).coverImgUrl)
                intent.putExtra("name", TopList.list.get(3).name)
                intent.putExtra("playCount", TopList.list.get(3).playCount)
                context?.startActivity(intent)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}