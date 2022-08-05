package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lie.puremusic.*
import com.lie.puremusic.databinding.ActivityLoadingBinding
import com.lie.puremusic.pojo.Singer
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.pojo.SquareSongList
import com.lie.puremusic.utils.*
import es.dmoral.toasty.Toasty
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import redis.clients.jedis.Jedis
import java.io.IOException
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private var mExitTime: Long = 0
    private var Pass = true

    companion object {
        var text: String? = null
        var fromWho: String? = null
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        Thread {
            val request: Request = Request.Builder()
                .url("https://v1.hitokoto.cn")
                .method("GET", null)
                .build()

            OkHttpClient().newCall(request)
                .enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val response = OkHttpClient().newCall(request).execute()
                        val jsonObject = response.body?.string()?.let { JSONObject(it) }
                        runOnUiThread {
                            text = jsonObject?.getString("hitokoto")
                            fromWho = ("-- by " + jsonObject?.getString("from_who"))
                        }
                    }
                })
        }.start()
        binding.hitokoto.text = text
        binding.fromWho.text = fromWho
        Thread {
            val pools = Executors.newCachedThreadPool()
            var jedis = StaticData.jedis
            if (intent.getStringExtra("style").equals("UserInfo")) {
                pools.submit(
                    GetUser(StaticData.user?.getAccount(), StaticData.user?.getPassword(), this)
                )
                pools.shutdown()
                pools.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
                )
                if (Pass) {
                    startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                    finish()
                }
            }
            if (intent.getStringExtra("style")
                    .equals("PopularList") || intent.getStringExtra("style")
                    .equals("SearchList") || intent.getStringExtra("style")
                    .equals("List_ID") || intent.getStringExtra("style")
                    .equals("UserList") || intent.getStringExtra("style").equals("SquareList")
            ) {
                pools.submit(Runnable {
                    StaticData.Root = "网易云音乐"
                    val index = intent.getIntExtra("index", Int.MAX_VALUE)
                    var SongList: SongList? = null
                    when (intent.getStringExtra("style")) {
                        "PopularList" -> SongList = StaticData.Home.getSongsLists()?.get(index)
                        "SearchList" -> SongList = StaticData.Result.getSongLists()?.get(index)
                        "List_ID" -> SongList = StaticData.SongList
                        "UserList" -> SongList = StaticData.User?.getSongLists()?.get(index)
                        "SquareList" -> StaticData.Square.get(StaticData.Square_SelectID)
                            ?.getSongsLists()?.get(index)
                    }
                    if (jedis?.get("SongList_" + SongList?.getId()) != null) {
                        println("缓存")
                        val songs = JSONArray(jedis?.get("SongList_" + SongList?.getId()))
                        SongList?.setCount(songs.length())
                        for (j in 0 until songs.length()) {
                            if (SongList?.getSongs()?.size!! < songs.length()) {
                                SongList?.getSongs()
                                    ?.add(Song(songs.getJSONObject(j).getString("id")))
                            }
                        }
                        StaticData.PlayList = SongList?.getSongs()!!
                        val intent =
                            Intent(this@LoadingActivity, SongListActivity::class.java)
                        intent.putExtra("style", "PopularList")
                        intent.putExtra("index", index)
                        startActivity(intent)
                        finish()
                    } else{
                        val url =
                            if (intent.getStringExtra("style").equals("List_ID")) {
                                "http://www.puremusic.com.cn:3000/playlist/track/all?id=" + StaticData.User?.getFavorite()
                                    ?.getId()
                            } else {
                                "http://www.puremusic.com.cn:3000/playlist/track/all?id=" + SongList?.getId()
                            }

                        val request: Request = Request.Builder()
                            .url(url)
                            .addHeader("cookie", StaticData.cookie)
                            .method("GET", null)
                            .build()

                        OkHttpClient().newCall(request)
                            .enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    println("发送异步请求")
                                    var songs =
                                        JSONObject(response.body?.string()).getJSONArray("songs")
                                    jedis?.set("SongList_" + SongList?.getId(),songs.toString())
                                    SongList?.setCount(songs.length())
                                    for (j in 0 until songs.length()) {
                                        if (SongList?.getSongs()?.size!! < songs.length()) {
                                            SongList?.getSongs()
                                                ?.add(Song(songs.getJSONObject(j).getString("id")))
                                        }
                                    }
                                    StaticData.PlayList = SongList?.getSongs()!!
                                    val intent =
                                        Intent(this@LoadingActivity, SongListActivity::class.java)

                                    when (intent.getStringExtra("style")) {
                                        "PopularList" -> intent.putExtra("style", "PopularList")
                                        "SearchList" -> intent.putExtra("style", "SearchList")
                                        "List_ID" -> intent.putExtra("style", "List_ID")
                                        "UserList" -> intent.putExtra("style", "UserList")
                                        "SquareList" -> intent.putExtra("style", "SquareList")
                                    }
                                    intent.putExtra("index", index)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                    }
                })
            }
            if (intent.getStringExtra("style")
                    .equals("SingerList") || intent.getStringExtra("style")
                    .equals("SearchSinger") || intent.getStringExtra("style").equals("Singer_ID")
            ) {
                pools.submit(Runnable {
                    StaticData.Root = "网易云音乐"
                    val index = intent.getIntExtra("index", Int.MAX_VALUE)

                    var Singer: Singer? = null
                    when (intent.getStringExtra("style")) {
                        "SingerList" -> Singer = StaticData.Home.getSingers()?.get(index)
                        "SearchSinger" -> Singer = StaticData.Result.getSingers()?.get(index)
                        "Singer_ID" -> Singer = StaticData.Singer
                    }
                    if (jedis?.get("Singer_" + Singer?.getId()) != null) {
                        println("缓存")
                        val songs = JSONArray(jedis?.get("Singer_" + Singer?.getId()))
                        for (j in 0 until songs.length()) {
                            if (Singer?.getSongs()?.size!! < songs.length()) {
                                Singer?.getSongs()
                                    ?.add(Song(songs.getJSONObject(j).getString("id")))
                            }
                        }
                        StaticData.PlayList = Singer?.getSongs()!!

                        val intent =
                            Intent(this@LoadingActivity, SingerActivity::class.java)
                        when (intent.getStringExtra("style")) {
                            "SingerList" -> intent.putExtra("style", "SingerList")
                            "SearchSinger" -> intent.putExtra("style", "SearchSinger")
                            "Singer_ID" -> intent.putExtra("style", "Singer_ID")
                        }
                        intent.putExtra("index", index)
                        startActivity(intent)
                        finish()
                    } else {
                        val url =
                            "http://www.puremusic.com.cn:3000/artist/songs?id=" + Singer?.getId()
                        val request: Request = Request.Builder()
                            .url(url)
                            .addHeader("cookie", StaticData.cookie)
                            .method("GET", null)
                            .build()

                        OkHttpClient().newCall(request)
                            .enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    println("发送异步请求")
                                    val songs =
                                        JSONObject(response.body?.string()).getJSONArray("songs")
                                    jedis?.set("Singer_" + Singer?.getId(), songs.toString())
                                    for (j in 0 until songs.length()) {
                                        if (Singer?.getSongs()?.size!! < songs.length()) {
                                            Singer?.getSongs()
                                                ?.add(Song(songs.getJSONObject(j).getString("id")))
                                        }
                                    }
                                    StaticData.PlayList = Singer?.getSongs()!!

                                    val intent =
                                        Intent(this@LoadingActivity, SingerActivity::class.java)
                                    when (intent.getStringExtra("style")) {
                                        "SingerList" -> intent.putExtra("style", "SingerList")
                                        "SearchSinger" -> intent.putExtra("style", "SearchSinger")
                                        "Singer_ID" -> intent.putExtra("style", "Singer_ID")
                                    }
                                    intent.putExtra("index", index)
                                    startActivity(intent)
                                    finish()
                                }
                            })
                    }
                })
            }
            if (intent.getStringExtra("style").equals("Search")) {
                pools.submit(GetResultData(intent.getStringExtra("value")))
                pools.shutdown()
                pools.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
                )
                if (Pass) {
                    val intent = Intent(this@LoadingActivity, SearchActivity::class.java)
                    intent.putExtra("style", "Search")
                    intent.putExtra("value", intent.getStringExtra("value"))
                    startActivity(intent)
                    finish()
                }
            }
            if (intent.getStringExtra("style").equals("Square")) {
                for (i in 0..4) {
                    StaticData.Square.add(SquareSongList())
                }
                pools.submit(GetSquare("华语", 0))
                pools.submit(GetSquare("欧美", 1))
                pools.submit(GetSquare("流行", 2))
                pools.submit(GetSquare("古风", 3))
                pools.submit(GetSquare("轻音乐", 4))
                pools.shutdown()
                pools.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
                )
                if (Pass) {
                    val intent = Intent(this@LoadingActivity, SquareActivity::class.java)
                    intent.putExtra("style", "Square")
                    startActivity(intent)
                    finish()
                }
            }
        }.start()
    }

    fun save() {
        val sharedPreference = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
        var editor = sharedPreference.edit()
        editor.putString("id", StaticData.user?.getId())
        editor.putString("music_id", StaticData.user?.getMusic_id())
        editor.putString("name", StaticData.user?.getName())
        editor.putString("avatarUrl", StaticData.user?.getAvatarUrl())
        editor.putString("account", StaticData.user?.getAccount())
        editor.putString("password", StaticData.user?.getPassword())
        editor.commit()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        val period: Long
        if (keyCode == KeyEvent.KEYCODE_BACK) //确定按下返回键
        {
            period = System.currentTimeMillis() - mExitTime //两次按下的时间间隔
            if (period > 1500) //3s之内两次按下退出有效
            {
                Toasty.info(this, "再按一次退出退出加载", Toast.LENGTH_SHORT, true).show()
                mExitTime = System.currentTimeMillis()
            } else {
                Pass = false
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}