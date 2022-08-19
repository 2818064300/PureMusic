package com.lie.puremusic.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivityLoadingBinding
import com.lie.puremusic.music.netease.*
import com.lie.puremusic.music.netease.data.toStandardSongDataArrayList
import com.lie.puremusic.pojo.SquareSongList
import com.lie.puremusic.standard.data.StandardPlaylistData
import com.lie.puremusic.standard.data.StandardSingerData
import com.lie.puremusic.utils.*
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private var mExitTime: Long = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        Thread {
            val pools = Executors.newCachedThreadPool()
            var jedis = StaticData.jedis
            var style = intent.getStringExtra("style")
            if (style.equals("UserInfo")) {
                pools.submit(
                    GetUser(StaticData.user?.getAccount(), StaticData.user?.getPassword(), this)
                )
                pools.submit() {
                    UserDetail.getUserDetail(this){
                        StaticData.UserDetailData = it
                    }
                }
                pools.submit() {
                    UserPlaylist.getUserByCookie(1333576013){
                        StaticData.MyFavorite = it.get(0)
                        it.remove(StaticData.MyFavorite)
                        StaticData.UserPlaylistData = it
                        startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                        finish()
                    }
                }
            }
            if (style.equals("SongList")
            ) {
                pools.submit(Runnable {
                    StaticData.Root = "网易云音乐"
                    var id: Long = intent.getLongExtra("id", 0)
                    Playlist.getPlaylistByCookie(id) {
                        StaticData.PlayListData =
                            StandardPlaylistData(
                                id,
                                intent.getStringExtra("name").toString(),
                                intent.getStringExtra("picUrl").toString(),
                                intent.getLongExtra("playCount", 0),
                                it
                            )
                        startActivity(Intent(this@LoadingActivity, SongListActivity::class.java))
                        finish()
                    }
//                    if (jedis?.get("SongList_" + id) != null) {
//                        val songs = JSONArray(jedis?.get("SongList_" + id))
//                        SongList?.setCount(songs.length())
//                        for (j in 0 until songs.length()) {
//                            if (SongList?.getSongs()?.size!! < songs.length()) {
//                                SongList?.getSongs()
//                                    ?.add(Song(songs.getJSONObject(j).getString("id")))
//                            }
//                        }
//                        StaticData.PlayList = SongList?.getSongs()!!
//                        val intent =
//                            Intent(this@LoadingActivity, SongListActivity::class.java)
//                        when (style) {
//                            "PopularList" -> intent.putExtra("style", "PopularList")
//                            "SearchList" -> intent.putExtra("style", "SearchList")
//                            "List_ID" -> intent.putExtra("style", "List_ID")
//                            "UserList" -> intent.putExtra("style", "UserList")
//                            "SquareList" -> intent.putExtra("style", "SquareList")
//                        }
//                        intent.putExtra("index", index)
//                        startActivity(intent)
//                        finish()
//                    } else {
//                    }
                })
            }
            if (style.equals("SingerList")) {
                pools.submit(Runnable {
                    StaticData.Root = "网易云音乐"
                    var id: Long = intent.getLongExtra("id", 0)
                    Playlist.getSingerByCookie(id) {
                        StaticData.SingerData =
                            StandardSingerData(
                                id,
                                intent.getStringExtra("name").toString(),
                                intent.getStringExtra("picUrl").toString(),
                                it
                            )
                        startActivity(Intent(this@LoadingActivity, SingerActivity::class.java))
                        finish()
                    }
//                    if (jedis?.get("Singer_" + Singer?.getId()) != null) {
//                        val songs = JSONArray(jedis?.get("Singer_" + Singer?.getId()))
//                        for (j in 0 until songs.length()) {
//                            if (Singer?.getSongs()?.size!! < songs.length()) {
//                                Singer?.getSongs()
//                                    ?.add(Song(songs.getJSONObject(j).getString("id")))
//                            }
//                        }
//                        StaticData.PlayList = Singer?.getSongs()!!
//                        val intent =
//                            Intent(this@LoadingActivity, SingerActivity::class.java)
//                        when (style) {
//                            "SingerList" -> intent.putExtra("style", "SingerList")
//                            "SearchSinger" -> intent.putExtra("style", "SearchSinger")
//                            "Singer_ID" -> intent.putExtra("style", "Singer_ID")
//                        }
//                        intent.putExtra("index", index)
//                        startActivity(intent)
//                        finish()
//                    }
                })
            }
            if (style.equals("Search")) {
                SearchDetail.getSearchResult(this){
                    StaticData.SearchResult = it.getSongArrayList()
                    startActivity(Intent(this@LoadingActivity, SearchActivity::class.java))
                    finish()
                }
            }
            if (style.equals("DailyRecommend")) {
                if(StaticData.DailyRecommendSongData == null){
                    RecommendSong.getRecommendSong(this){
                        StaticData.DailyRecommendSongData = it
                        startActivity(Intent(this@LoadingActivity, RecommendActivity::class.java))
                        finish()
                    }
                } else{
                    startActivity(Intent(this@LoadingActivity, RecommendActivity::class.java))
                    finish()
                }
            }
            if (style.equals("UserCloud")) {
                if(StaticData.UserCloudData == null){
                    UserCloud.getUserCloud{
                        StaticData.UserCloudData = it
                        startActivity(Intent(this@LoadingActivity, UserCloudActivity::class.java))
                        finish()
                    }
                } else{
                    startActivity(Intent(this@LoadingActivity, UserCloudActivity::class.java))
                    finish()
                }
            }
            if (style.equals("Square")) {
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
                val intent = Intent(this@LoadingActivity, SquareActivity::class.java)
                intent.putExtra("style", "Square")
                startActivity(intent)
                finish()
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
                Toasty.info(this, "再按一次退出加载", Toast.LENGTH_SHORT, true).show()
                mExitTime = System.currentTimeMillis()
            } else {
                finish()
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}