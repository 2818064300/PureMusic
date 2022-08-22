package com.lie.puremusic.activity

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationBarView
import com.lie.puremusic.adapter.FragmentViewPagerAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityMainBinding
import com.lie.puremusic.fragment.HomeFragment
import com.lie.puremusic.fragment.ToplistFragment
import com.lie.puremusic.fragment.MusicFragment
import com.lie.puremusic.fragment.VideoFragment
import com.lie.puremusic.ui.base.BaseActivity
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import es.dmoral.toasty.Toasty


class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private var mExitTime: Long = 0
    companion object {
        lateinit var context: Context
    }

    override fun initBinding() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
        context = this
    }
    override fun initData() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        if (StaticData.hasNew){
            Toasty.info(this@MainActivity,"Pure Music新版发布啦!",2500).show()
        } else{
            if (sharedPreference?.contains("id") != true) {
                Toasty.custom(
                    this@MainActivity,
                    "欢迎使用Pure Music!",
                    R.mipmap.ic_launcher_round,
                    R.color.material_blue_500,
                    1500,
                    false,
                    true
                ).show()
            } else {
                Toasty.custom(
                    this@MainActivity,
                    "登录成功!",
                    R.mipmap.ic_launcher_round,
                    R.color.material_blue_500,
                    1500,
                    false,
                    true
                ).show()
            }
        }
    }
    override fun initView(){
        //初始化控件
        initBottomNavigationView()
        initViewPage()
    }
    override fun initListener(){
        binding.tabItem4.setOnClickListener {
            startActivity(Intent(this@MainActivity, SettingActivity::class.java))
        }
        binding.tabItem6.setOnClickListener { System.exit(0) }
        binding.mainDrawer.setOnDrawerStateChangeListener(object :
            ElasticDrawer.OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {
            }
        })
    }

    fun initBottomNavigationView() {
        //设置默认选中item
        binding.bottomNavigation.selectedItemId = R.id.nav_home;
        //设置选择改变监听
        binding.bottomNavigation.setOnItemSelectedListener(object :
            NavigationBarView.OnItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.nav_music -> binding.ViewPaper.currentItem = 0
                    R.id.nav_home -> binding.ViewPaper.currentItem = 1
                    R.id.nav_loaderboard -> binding.ViewPaper.currentItem = 2
                    R.id.nav_video -> binding.ViewPaper.currentItem = 3
                }
                return true
            }
        })
    }

    fun initViewPage() {
        val Fragments: MutableList<Fragment> = ArrayList()
        val musicFragment: MusicFragment = MusicFragment()
        val homeFragment: HomeFragment = HomeFragment()
        val leaderboardFragment: ToplistFragment = ToplistFragment()
        val videoFragment: VideoFragment = VideoFragment()
        Fragments.add(musicFragment)
        Fragments.add(homeFragment)
        Fragments.add(leaderboardFragment)
        Fragments.add(videoFragment)
        binding.ViewPaper.adapter =
            FragmentViewPagerAdapter(this.supportFragmentManager,
                Fragments,
                LifecycleRegistry(this).apply {
                    currentState = Lifecycle.State.RESUMED
                })
        //同步ViewPaper索引
        binding.ViewPaper.currentItem = 1
        //设置页面改变监听
        binding.ViewPaper.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigation.selectedItemId = R.id.nav_music;
                    1 -> binding.bottomNavigation.selectedItemId = R.id.nav_home;
                    2 -> binding.bottomNavigation.selectedItemId = R.id.nav_loaderboard;
                    3 -> binding.bottomNavigation.selectedItemId = R.id.nav_video;
                }
            }
        })
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val period: Long
        if (keyCode == KeyEvent.KEYCODE_BACK) //确定按下返回键
        {
            period = System.currentTimeMillis() - mExitTime //两次按下的时间间隔
            if (period > 1500) {
                Toasty.info(this, "再按一次退出程序", Toast.LENGTH_SHORT, true).show()
                mExitTime = System.currentTimeMillis()
            } else {
                System.exit(0)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun open() {
        binding.mainDrawer.openMenu()
    }
}