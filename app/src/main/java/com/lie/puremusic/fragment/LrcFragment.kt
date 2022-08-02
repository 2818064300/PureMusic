package com.lie.puremusic.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.PlayerActivity
import com.lie.puremusic.databinding.FragmentLrcBinding
import com.lie.puremusic.utils.BurnUtil
import java.util.*

class LrcFragment : Fragment() {

    private var _binding: FragmentLrcBinding? = null
    private val binding get() = _binding!!
    private var Lrc : String? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLrcBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        Timer().schedule(object : TimerTask() {
            override fun run() {
                StaticData.Songs?.getLyric()?.getCurrentPosition()?.toLong()
                    ?.let { binding.LrcView.updateTime(it) }
                if(!Lrc.equals(StaticData.Songs?.getLyric()?.getContent())){
                    init()
                    val activity = activity as PlayerActivity?
                    activity?.goPlayer()
                }
            }
        }, 0, 50)
    }

    private fun init(){
        Lrc = StaticData.Songs?.getLyric()?.getContent()
        binding.LrcView.loadLrc(Lrc)
        if (StaticData.Songs?.getVibrantLight() != null && StaticData.Songs?.getVibrant() != null) {
            StaticData.Songs?.getVibrant()?.getRgb()
                ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
            StaticData.Songs?.getVibrantLight()?.getRgb()
                ?.let { binding.LrcView.setNormalColor(it) }
        } else{
            if (StaticData.Songs?.getVibrantDark() != null && StaticData.Songs?.getMuted() != null) {
                StaticData.Songs?.getMuted()?.getRgb()
                    ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
                StaticData.Songs?.getVibrantDark()?.getRgb()
                    ?.let { binding.LrcView.setNormalColor(it) }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LrcFragment()
    }
}