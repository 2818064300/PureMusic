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
                StaticData.CurrentPosition?.let { binding.LrcView.updateTime(it) }
                if(!Lrc.equals(StaticData.SongLrc)){
                    init()
                    val activity = activity as PlayerActivity?
                    activity?.goPlayer()
                }
            }
        }, 0, 50)
    }

    private fun init(){
        Lrc = StaticData.SongLrc
        binding.LrcView.loadLrc(Lrc)
        if (StaticData.PlayDataEx?.VibrantLight != null && StaticData.PlayDataEx?.Vibrant != null) {
            StaticData.PlayDataEx?.Vibrant?.getRgb()
                ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
            StaticData.PlayDataEx?.VibrantLight?.getRgb()
                ?.let { binding.LrcView.setNormalColor(it) }
        } else{
            if (StaticData.PlayDataEx?.VibrantDark != null && StaticData.PlayDataEx?.Muted != null) {
                StaticData.PlayDataEx?.Muted?.getRgb()
                    ?.let { BurnUtil.colorBurn(it) }?.let { binding.LrcView.setCurrentColor(it) }
                StaticData.PlayDataEx?.VibrantDark?.getRgb()
                    ?.let { binding.LrcView.setNormalColor(it) }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = LrcFragment()
    }
}