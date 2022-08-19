package com.lie.puremusic.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.MyRecyclerGridAdapter3
import com.lie.puremusic.databinding.ActivitySplashBinding
import com.lie.puremusic.music.netease.NewSong
import com.lie.puremusic.music.netease.PlaylistRecommend
import com.lie.puremusic.pojo.*
import com.lie.puremusic.standard.data.SOURCE_NETEASE
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.*
import es.dmoral.toasty.Toasty
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    val permissions = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val mPermissionList = ArrayList<String>()
    val mRequestCode = 0x1//权限请求码

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (Environment.isExternalStorageManager()) {
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:" + this@SplashActivity.packageName)
            startActivityForResult(intent, 1)
        }
        //初始化权限
        initPermission()
    }

    fun initPermission() {
        mPermissionList.clear()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this@SplashActivity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mPermissionList.add(permission)
            }
        }
        if (!mPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this@SplashActivity, permissions, mRequestCode)
        } else {
            if (!StaticData.debug) {
                Thread {
                    StaticData.jedis = RedisUtils.getConnection()
                    //获取数据库连接对象,检测版本更新
                    StaticData.connection = DBUtils.getConnection()
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    //服务器版本
                    val date1 = format.parse(Dao.check_version(StaticData.connection))
                    //当前版本
                    val date2 = format.parse(StaticData.version_date)
                    if (date1.compareTo(date2) == 1) {
                        StaticData.hasNew = true
                    }
                }.start()
                init()
            } else {
                startActivity(Intent(this@SplashActivity, TestActivity::class.java))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0x1 -> for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toasty.info(this@SplashActivity, "您有未授予的权限!").show()
                }
            }

        }
    }

    fun init() {
        Thread {
            val pools = Executors.newCachedThreadPool()
            pools.submit {
                PlaylistRecommend.getPlaylistRecommend(this) {
                    StaticData.PlaylistRecommend = it
                    val sharedPreference =
                        getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                    if (sharedPreference?.contains("id") != true) {
                        StaticData.user = PureUser(
                            sharedPreference.getString("id", "暂无"),
                            sharedPreference.getString("account", "18296923394"),
                            sharedPreference.getString("password", "Lcz18296923394")
                        )
                        val intent = Intent(this@SplashActivity, LoadingActivity::class.java)
                        intent.putExtra("style", "UserInfo")
                        startActivity(intent)
                    } else {
                        startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                    }
                    finish()
                }
            }
            pools.submit{
                NewSong.getNewSong(this) {
                    StaticData.NewSong = it
                }
            }
            pools.submit(Runnable {
                for (i in 1..5) {
                    var name: String? = null
                    val standardArtistDataList = ArrayList<StandardSongData.StandardArtistData>()
                    if (i == 1) {
                        name = "反方向的钟"
                        val standardArtistData = StandardSongData.StandardArtistData(
                            -1,
                            "九三 (cover: 周杰伦)"
                        )
                        standardArtistDataList.add(standardArtistData)
                    }
                    if (i == 2) {
                        name = "关键词"
                        val standardArtistData = StandardSongData.StandardArtistData(
                            -1,
                            "王巨星 (cover: 林俊杰)"
                        )
                        standardArtistDataList.add(standardArtistData)
                    }
                    if (i == 3) {
                        name = "雨爱"
                        val standardArtistData = StandardSongData.StandardArtistData(
                            -1,
                            "不是花火呀"
                        )
                        standardArtistDataList.add(standardArtistData)
                    }
                    if (i == 4) {
                        name = "我的一个道姑朋友"
                        val standardArtistData = StandardSongData.StandardArtistData(
                            -1,
                            "九三 (cover: 双笙)"
                        )
                        standardArtistDataList.add(standardArtistData)
                    }
                    if (i == 5) {
                        name = "芳华慢 + 霜雪千年"
                        val standardArtistData = StandardSongData.StandardArtistData(
                            -1,
                            "等什么君"
                        )
                        standardArtistDataList.add(standardArtistData)
                    }
                    val songs = StandardSongData(
                        SOURCE_NETEASE,
                        "music$i",
                        name,
                        "http://puremusic.com.cn/Cloud/Cover/cover$i.jpg",
                        standardArtistDataList,
                        StandardSongData.NeteaseInfo(
                            0,
                            100,
                            1,
                            0,
                            0
                        ),
                        null,
                        null
                    )
                    StaticData.Cloud.add(songs)
                }
            })
            pools.shutdown()
//            StaticData.user = PureUser(
//                "暂无",
//                "18296923394",
//                "Lcz18296923394"
//            )
        }.start()
    }
}