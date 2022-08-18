package com.lie.puremusic.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.activity.PreSearchActivity
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerGridAdapter
import com.lie.puremusic.adapter.MyRecyclerGridAdapter3
import com.lie.puremusic.databinding.FragmentLeaderboardBinding
import com.lie.puremusic.music.netease.PlaylistRecommend
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.SpacesItemDecoration
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
        val NewSongList: ArrayList<StandardSongData> = ArrayList()
        for (i in 0..5) {
            NewSongList.add(StaticData.NewSong.get(i))
        }
        val adapter = MyRecyclerGridAdapter3(requireContext(), NewSongList)
        binding.NewSongGroup.adapter = adapter
        val layoutManager = GridLayoutManager(context, 1)
        layoutManager.orientation = RecyclerView.HORIZONTAL
        binding.NewSongGroup.layoutManager = layoutManager
        binding.NewSongGroup.addItemDecoration(SpacesItemDecoration2())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}