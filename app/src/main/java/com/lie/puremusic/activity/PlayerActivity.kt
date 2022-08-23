package com.lie.puremusic.activity

import android.os.Bundle
import android.view.KeyEvent
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.lie.puremusic.adapter.FragmentAdapter
import com.lie.puremusic.fragment.LrcFragment
import com.lie.puremusic.utils.MediaPlayerHelper
import com.lie.puremusic.R
import com.lie.puremusic.databinding.ActivityPlayerBinding
import com.lie.puremusic.fragment.PlayerFragment
import com.lie.puremusic.fragment.TestFragment1

class PlayerActivity : AppCompatActivity(){
    companion object {
        var mediaPlayerHelper: MediaPlayerHelper? = null
        lateinit var fragmentManager : FragmentManager
    }
    private val lrcFragment: LrcFragment = LrcFragment()
    private val playerFragment: PlayerFragment = PlayerFragment()
    private val testFragment1: TestFragment1 = TestFragment1()
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        PlayerActivity.fragmentManager = supportFragmentManager
        mediaPlayerHelper = MediaPlayerHelper.getInstance(this)
        val Fragments: MutableList<Fragment?> = ArrayList()
        Fragments.add(testFragment1)
        Fragments.add(playerFragment)
        Fragments.add(lrcFragment)
        binding.ViewPaper.setAdapter(FragmentAdapter(supportFragmentManager, lifecycle, Fragments))
        binding.ViewPaper.setCurrentItem(1)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) //确定按下返回键
        {
            if(binding.ViewPaper.currentItem == 2){
                binding.ViewPaper.setCurrentItem(1)
                return false
            } else{
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    fun goPlayer(){
        binding.ViewPaper.setCurrentItem(1)
    }
    fun goLrc(){
        binding.ViewPaper.setCurrentItem(2)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}