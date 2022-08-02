package com.lie.puremusic.activity

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.lie.puremusic.adapter.FragmentAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.fragment.*
import com.lie.puremusic.databinding.ActivitySquareBinding

class SquareActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySquareBinding
    private val btn_group: MutableList<ImageButton?> = ArrayList()
    private val tv_group: MutableList<TextView?> = ArrayList()


    private val squareFragment1: SquareFragment1 = SquareFragment1()
    private val squareFragment2: SquareFragment2 = SquareFragment2()
    private val squareFragment3: SquareFragment3 = SquareFragment3()
    private val squareFragment4: SquareFragment4 = SquareFragment4()
    private val squareFragment5: SquareFragment5 = SquareFragment5()
    private val homeFragment: HomeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySquareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        initbtn()
        initPage()
        binding.Back.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun initPage() {
        val Fragments: MutableList<Fragment?> = ArrayList()
        Fragments.add(squareFragment1)
        Fragments.add(squareFragment2)
        Fragments.add(squareFragment3)
        Fragments.add(squareFragment4)
        Fragments.add(squareFragment5)

        binding.ViewPaper.setAdapter(FragmentAdapter(supportFragmentManager, lifecycle, Fragments))
        binding.ViewPaper.setCurrentItem(0)
        binding.ViewPaper.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        setbtn(StaticData.Square_SelectID, 0)
                        StaticData.Square_SelectID = 0
                    }
                    1 -> {
                        setbtn(StaticData.Square_SelectID, 1)
                        StaticData.Square_SelectID = 1
                    }
                    2 -> {
                        setbtn(StaticData.Square_SelectID, 2)
                        StaticData.Square_SelectID = 2
                    }
                    3 -> {
                        setbtn(StaticData.Square_SelectID, 3)
                        StaticData.Square_SelectID = 3
                    }
                    4 -> {
                        setbtn(StaticData.Square_SelectID, 4)
                        StaticData.Square_SelectID = 4
                    }
                }
                super.onPageSelected(position)
            }
        })
    }

    private fun initbtn() {
        btn_group.add(binding.btn1Bg)
        btn_group.add(binding.btn2Bg)
        btn_group.add(binding.btn3Bg)
        btn_group.add(binding.btn4Bg)
        btn_group.add(binding.btn5Bg)
        tv_group.add(binding.tv1)
        tv_group.add(binding.tv2)
        tv_group.add(binding.tv3)
        tv_group.add(binding.tv4)
        tv_group.add(binding.tv5)
        setbtn(1, 0)
        setbtn(2, 0)
        setbtn(3, 0)
        setbtn(4, 0)
    }

    private fun setbtn(last_SelectID: Int, SelectID: Int) {
        btn_group[last_SelectID]?.visibility = View.GONE
        btn_group[SelectID]?.visibility = View.VISIBLE
        tv_group[last_SelectID]?.setTextColor(Color.parseColor("#FF898d9d"))
        tv_group[SelectID]?.setTextColor(Color.parseColor("#FF397dff"))
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}