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
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivitySplashBinding
import com.lie.puremusic.pojo.*
import com.lie.puremusic.utils.DBUtils
import com.lie.puremusic.utils.Dao
import com.lie.puremusic.utils.RedisUtils
import es.dmoral.toasty.Toasty
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {

private lateinit var binding: ActivitySplashBinding

    val permissions = arrayOf(Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
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
        } else{
            if (!StaticData.debug) {
                Thread{
                    StaticData.jedis =  RedisUtils.getConnection()
                    //获取数据库连接对象,检测版本更新
                    StaticData.connection = DBUtils.getConnection()
                    val format = SimpleDateFormat("yyyy-MM-dd")
                    //服务器版本
                    val date1 = format.parse(Dao.check_version(StaticData.connection))
                    //当前版本
                    val date2 = format.parse(StaticData.version_date)
                    if (date1.compareTo(date2) == 1){
                        StaticData.hasNew = true
                    }
                }.start()
                init()
            } else{
                startActivity(Intent(this@SplashActivity, TestActivity::class.java))
            }
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            0x1 -> for (i in 0 until grantResults.size) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    Toasty.info(this@SplashActivity,"您有未授予的权限!").show()
                }
            }

        }
    }
    fun init(){
        Thread {
            val pools = Executors.newCachedThreadPool()
            pools.submit(Runnable {
                val request: Request = Request.Builder()
                    .url("http://www.puremusic.com.cn:3000/personalized?limit=60")
                    .addHeader("cookie", StaticData.cookie)
                    .method("GET", null)
                    .build()
                val response: Response = OkHttpClient().newCall(request).execute()
                val result = JSONObject(response.body?.string()).getJSONArray("result")
                for (i in 0 until result.length()) {
                    StaticData.Home.getSongsLists()?.add(SongList(result.getJSONObject(i).getString("id")))
                    StaticData.Home.getSongsLists()?.get(i)?.setName(result.getJSONObject(i).getString("name"))
                    StaticData.Home.getSongsLists()?.get(i)?.setPlayCount(result.getJSONObject(i).getInt("playCount"))
                    StaticData.Home.getSongsLists()?.get(i)?.setCover_url(result.getJSONObject(i).getString("picUrl"))
                }
            })
            pools.submit(Runnable {
                val request: Request = Request.Builder()
                    .url("http://www.puremusic.com.cn:3000/top/artists?limit=5")
                    .method("GET", null)
                    .build()
                val response: Response = OkHttpClient().newCall(request).execute()
                val artists = JSONObject(response.body?.string()).getJSONArray("artists")
                for (i in 0 until artists.length()) {
                    StaticData.Home.getSingers()?.add(Singer(artists.getJSONObject(i).getString("id")))
                    StaticData.Home.getSingers()?.get(i)?.setName(artists.getJSONObject(i).getString("name"))
                    StaticData.Home.getSingers()?.get(i)?.setCover_url(artists.getJSONObject(i).getString("picUrl"))
                }
            })
            pools.submit(Runnable {
                for (i in 1..5) {
                    val songs = Song()
                    songs.setMusic_url("http://puremusic.com.cn/Cloud/Music/music$i.mp3")
                    songs.setCover_url("http://puremusic.com.cn/Cloud/Cover/cover$i.jpg")
                    songs.setFee(10)
                    songs.setPop(0)
                    songs.setTime(0)
                    songs.setStyle("Cloud")
                    songs.getLyric()?.setContent(("云端歌曲,暂无歌词."))
                    songs.setId("music$i")
                    songs.setType("mp3")
                    StaticData.Cloud.add(songs)
                    val singer = Singer()
                    if (i == 1) {
                        StaticData.Cloud.get(0)?.setName("反方向的钟")
                        singer.setName("九三 (cover: 周杰伦)")
                    }
                    if (i == 2) {
                        StaticData.Cloud.get(1)?.setName("关键词")
                        singer.setName("王巨星 (cover: 林俊杰)")
                    }
                    if (i == 3) {
                        StaticData.Cloud.get(2)?.setName("雨爱")
                        singer.setName("不是花火呀")
                    }
                    if (i == 4) {
                        StaticData.Cloud.get(3)?.setName("我的一个道姑朋友")
                        singer.setName("九三 (cover: 双笙)")
                    }
                    if (i == 5) {
                        StaticData.Cloud.get(4)?.setName("芳华慢 + 霜雪千年")
                        singer.setName("等什么君")
                    }
                    StaticData.Cloud.get(i - 1)?.getSingers()?.add(singer)
                }
            })
            pools.shutdown()
            pools.awaitTermination(Long.MAX_VALUE,TimeUnit.NANOSECONDS)
            val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
            if (sharedPreference?.contains("id") != true) {
                StaticData.user = PureUser(
                    sharedPreference.getString("id","暂无"),
                    sharedPreference.getString("account","18296923394"),
                    sharedPreference.getString("password","Lcz18296923394")
                )
                val intent = Intent(this@SplashActivity, LoadingActivity::class.java)
                intent.putExtra("style","UserInfo")
                startActivity(intent)
            } else {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }
//            StaticData.user = PureUser(
//                "暂无",
//                "18296923394",
//                "Lcz18296923394"
//            )
            finish()
        }.start()
    }
}