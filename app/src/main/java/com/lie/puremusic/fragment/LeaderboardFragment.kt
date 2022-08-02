package com.lie.puremusic.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.lie.puremusic.activity.PreSearchActivity
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.FragmentLeaderboardBinding


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
        binding.HotSongs.setOnClickListener(View.OnClickListener {
            StaticData.SongsList_ID = 62
            StaticData.Style = "Leaderboard"
            val intent: Intent
            StaticData.Root = "网易云音乐"
//            if (!StaticData.Containr.contains(
//                    StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getId()
//                )
//            ) {
//                intent = Intent(activity, LoadingActivity::class.java)
//            } else {
//                StaticData.PlayList = StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getSongs()!!
//                intent = Intent(activity, LeaderboardActivity::class.java)
//            }
//            startActivity(intent)
        })
        binding.NewSongs1.setOnClickListener(View.OnClickListener {
            StaticData.SongsList_ID = 61
            StaticData.Style = "Leaderboard"
            val intent: Intent
            StaticData.Root = "网易云音乐"
//            if (!StaticData.Containr.contains(
//                    StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getId()
//                )
//            ) {
//                intent = Intent(activity, LoadingActivity::class.java)
//            } else {
//                StaticData.PlayList = StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getSongs()!!
//                intent = Intent(activity, LeaderboardActivity::class.java)
//            }
//            startActivity(intent)
        })
        binding.NewSongs2.setOnClickListener(View.OnClickListener {
            StaticData.SongsList_ID = 61
            StaticData.Style = "Leaderboard"
            val intent: Intent
            StaticData.Root = "网易云音乐"
//            if (!StaticData.Containr.contains(
//                    StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getId()
//                )
//            ) {
//                intent = Intent(activity, LoadingActivity::class.java)
//            } else {
//                StaticData.PlayList = StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getSongs()!!
//                intent = Intent(activity, LeaderboardActivity::class.java)
//            }
//            startActivity(intent)
        })
        binding.NewSongs3.setOnClickListener(View.OnClickListener {
            StaticData.SongsList_ID = 61
            StaticData.Style = "Leaderboard"
            val intent: Intent
            StaticData.Root = "网易云音乐"
//            if (!StaticData.Containr.contains(
//                    StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getId()
//                )
//            ) {
//                intent = Intent(activity, LoadingActivity::class.java)
//            } else {
//                StaticData.PlayList = StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getSongs()!!
//                intent = Intent(activity, LeaderboardActivity::class.java)
//            }
//            startActivity(intent)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}