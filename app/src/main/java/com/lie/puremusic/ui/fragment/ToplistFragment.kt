package com.lie.puremusic.ui.fragment

import android.R.attr.banner
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerGridAdapter3
import com.lie.puremusic.databinding.FragmentToplistBinding
import com.lie.puremusic.music.netease.Banner
import com.lie.puremusic.music.netease.NewSong
import com.lie.puremusic.music.netease.TopList
import com.lie.puremusic.music.netease.data.BannerData
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.ui.activity.LoadingActivity
import com.lie.puremusic.ui.activity.PreSearchActivity
import com.lie.puremusic.utils.MagicHttp
import com.lie.puremusic.utils.SpacesItemDecoration2
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder
import com.youth.banner.indicator.CircleIndicator


class ToplistFragment : Fragment() {

    private var _binding: FragmentToplistBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentToplistBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchLeaderboardIbtn.setOnClickListener(View.OnClickListener {
            val intent = Intent(activity, PreSearchActivity::class.java)
            startActivity(intent)
        })

        Banner.getBanner {
            MagicHttp.runOnMainThread {
                binding.banner.setAdapter(object :BannerImageAdapter<BannerData.Banner>(it.banners){
                    override fun onBindView(
                        holder: BannerImageHolder?,
                        data: BannerData.Banner?,
                        position: Int,
                        size: Int
                    ) {
                        if (holder != null) {
                            Glide.with(this@ToplistFragment)
                                .load(data?.imageUrl)
                                .into(holder.imageView)
                        };
                    }

                })
            }
        }
        if (StaticData.NewSong == null) {
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
        } else {
            val NewSongList: ArrayList<StandardSongData> = ArrayList()
            for (i in 0..5) {
                NewSongList.add(StaticData.NewSong!!.get(i))
            }
            val adapter = MyRecyclerGridAdapter3(requireContext(), NewSongList)
            binding.NewSongGroup.adapter = adapter
            val layoutManager = GridLayoutManager(context, 1)
            layoutManager.orientation = RecyclerView.HORIZONTAL
            binding.NewSongGroup.layoutManager = layoutManager
            binding.NewSongGroup.addItemDecoration(SpacesItemDecoration2())
        }
        TopList.getTopList(requireContext()) {
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