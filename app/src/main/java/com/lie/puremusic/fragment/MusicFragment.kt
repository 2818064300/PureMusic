package com.lie.puremusic.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.*
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.activity.MainActivity
import com.lie.puremusic.activity.PreSearchActivity
import com.lie.puremusic.activity.SongListActivity
import com.lie.puremusic.adapter.MyRecyclerAdapter4
import com.lie.puremusic.databinding.FragmentMusicBinding
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.util.*

class MusicFragment : Fragment() {

    private var _binding: FragmentMusicBinding? = null

    private val binding get() = _binding!!
    companion object {
        var MusicFragmentContext: Context? = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMusicBinding.inflate(inflater, container, false)
        return binding.root

    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MusicFragmentContext = context
        val typeface = Typeface.createFromAsset(MusicFragmentContext?.assets, "汉仪雅酷黑65W.ttf")
        binding.tv1.typeface = typeface
        binding.tv2.typeface = typeface
        val adapter = context?.let {
            StaticData.User?.getSongLists()
                ?.let { it1 -> MyRecyclerAdapter4(it, it1) }
        }
        val alphaAdapter = adapter?.let { AlphaInAnimationAdapter(it) }
        binding.CardGroup.adapter = alphaAdapter?.let { AlphaInAnimationAdapter(it) }
        binding.CardGroup.layoutManager =
            object : LinearLayoutManager(activity, VERTICAL, false) {
                override fun canScrollVertically(): Boolean {
                    // 直接禁止垂直滑动
                    return false
                }
            }
        binding.search.setOnClickListener {
            val intent = Intent(activity, PreSearchActivity::class.java)
            startActivity(intent)
        }
        binding.container.setOnClickListener {
            val activity = activity as MainActivity?
            activity?.open()
        }
        val d = Date()
        val hours = d.hours
        if (hours < 6) {
            binding.tv1.text = "夜深了,月亮都睡了"
            binding.rect3.setImageResource(R.drawable.pic3)
        }
        if (hours >= 6) {
            binding.tv1.text = "早上好鸭,Lie."
            binding.rect3.setImageResource(R.drawable.pic1)
        }
        if (hours >= 8) {
            binding.tv1.text = "今天要元气满满哦"
            binding.rect3.setImageResource(R.drawable.pic1)
        }
        if (hours >= 12) {
            binding.tv1.text = "不喝杯下午茶吗?"
            binding.rect3.setImageResource(R.drawable.pic1)
        }
        if (hours >= 17) {
            binding.tv1.text = "傍晚该放松一下喽"
            binding.rect3.setImageResource(R.drawable.pic2)
        }
        if (hours >= 22) {
            binding.tv1.text = "夜深了,请注意休息"
            binding.rect3.setImageResource(R.drawable.pic3)
        }
        binding.tv9.text = "共" + StaticData.User?.getFavorite()?.getCount() + "首"
        binding.rect3.setOnClickListener {
            val intent: Intent
            StaticData.Style = "List_ID"
            StaticData.Root = "网易云音乐"
            if (!StaticData.Containr.contains(StaticData.User?.getFavorite()?.getId())) {
                StaticData.SongList = StaticData.User?.getFavorite()
                intent = Intent(activity, LoadingActivity::class.java)
            } else {
                StaticData.PlayList = StaticData.User?.getFavorite()?.getSongs()!!
                intent = Intent(activity, SongListActivity::class.java)
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}