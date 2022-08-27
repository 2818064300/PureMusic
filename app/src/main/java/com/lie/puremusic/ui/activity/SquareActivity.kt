package com.lie.puremusic.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.lie.puremusic.adapter.FragmentAdapter
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivitySquareBinding

class SquareActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySquareBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.RadioButton1.isChecked = true
        initPage()
        binding.Back.setOnClickListener {
            finish()
        }

    }

    private fun initPage() {
        val Fragments: MutableList<Fragment?> = ArrayList()

        binding.ViewPaper.setAdapter(FragmentAdapter(supportFragmentManager, lifecycle, Fragments))
        binding.ViewPaper.setCurrentItem(0)
        binding.ViewPaper.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.RadioButton1.isChecked = true
                        StaticData.Square_SelectID = 0
                    }
                    1 -> {
                        binding.RadioButton2.isChecked = true
                        StaticData.Square_SelectID = 1
                    }
                    2 -> {
                        binding.RadioButton3.isChecked = true
                        StaticData.Square_SelectID = 2
                    }
                    3 -> {
                        binding.RadioButton4.isChecked = true
                        StaticData.Square_SelectID = 3
                    }
                    4 -> {
                        binding.RadioButton5.isChecked = true
                        StaticData.Square_SelectID = 4
                    }
                }
                super.onPageSelected(position)
            }
        })
    }

}