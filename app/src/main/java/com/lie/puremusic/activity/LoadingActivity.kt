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
import com.lie.puremusic.pojo.SquareSongList
import com.lie.puremusic.utils.*
import es.dmoral.toasty.Toasty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class LoadingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoadingBinding
    private var mExitTime: Long = 0
    private var Pass = true


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)

        if (!StaticData.hitokoto.getHitokoto().equals("null")) {
            binding.hitokoto.text = StaticData.hitokoto.getHitokoto()
        }
        if (!StaticData.hitokoto.getFrom_who().equals("null")) {
            binding.fromWho.setText("-- by " + StaticData.hitokoto.getFrom_who())
        }
        if (StaticData.Style.equals("PopularList") || StaticData.Style.equals("Leaderboard")) {
            load(StaticData.SongsList_ID, StaticData.Style)
            StaticData.PlayList =
                StaticData.Home.getSongsLists()?.get(StaticData.SongsList_ID)?.getSongs()!!
        }
        if (StaticData.Style.equals("SingerList")) {
            load(StaticData.Singer_ID, StaticData.Style)
            StaticData.PlayList =
                StaticData.Home.getSingers()?.get(StaticData.Singer_ID)?.getSongs()!!
        }
        if (StaticData.Style.equals("Search")) {
            load(0, StaticData.Style)
            StaticData.PlayList = StaticData.Result.getSongs()!!
        }
        if (StaticData.Style.equals("SearchSinger")) {
            load(StaticData.Singer_Result_ID, StaticData.Style)
            StaticData.PlayList =
                StaticData.Result.getSingers()?.get(StaticData.Singer_Result_ID)?.getSongs()!!
        }
        if (StaticData.Style.equals("Singer_ID")) {
            load(0, StaticData.Style)
            StaticData.PlayList = StaticData.Singer?.getSongs()!!
        }
        if (StaticData.Style.equals("SearchList")) {
            load(StaticData.SongList_Result_ID, StaticData.Style)
            StaticData.PlayList =
                StaticData.Result.getSongLists()?.get(StaticData.SongList_Result_ID)?.getSongs()!!
        }
        if (StaticData.Style.equals("List_ID")) {
            load(0, StaticData.Style)
            StaticData.PlayList = StaticData.SongList?.getSongs()!!
        }
        if (StaticData.Style.equals("UserList")) {
            load(StaticData.SongList_Music_ID, StaticData.Style)
            StaticData.PlayList =
                StaticData.User?.getSongLists()?.get(StaticData.SongList_Music_ID)?.getSongs()!!
        }
        if (StaticData.Style.equals("UserInfo")) {
            load(0, StaticData.Style)
        }
        if (StaticData.Style.equals("Square")) {
            load(0, StaticData.Style)
        }
        if (StaticData.Style.equals("SquareList")) {
            load(StaticData.Square_Position, StaticData.Style)
            StaticData.PlayList = StaticData.Square.get(StaticData.Square_SelectID)?.getSongsLists()
                ?.get(StaticData.Square_Position)?.getSongs()!!
        }
    }

    private fun load(i: Int, Style: String?) {
        Thread {
            val pools = Executors.newCachedThreadPool()
            var intent: Intent? = null
            if (Style == "PopularList") {
                intent = Intent(this@LoadingActivity, SongListActivity::class.java)

                pools.submit(GetSonglistData(i, 0))
            }
            if (Style == "SingerList") {
                intent = Intent(this@LoadingActivity, SingerActivity::class.java)
                pools.submit(GetSingerData(i))
            }
            if (Style == "Leaderboard") {
                intent = Intent(this@LoadingActivity, LeaderboardActivity::class.java)
                pools.submit(GetSonglistData(i, 0))
            }
            if (Style == "Search") {
                intent = Intent(this@LoadingActivity, SearchActivity::class.java)
                pools.submit(GetResultData(StaticData.KeyWords))
            }
            if (Style == "SearchSinger") {
                intent = Intent(this@LoadingActivity, SingerActivity::class.java)
                pools.submit(GetSingerData(i))
            }
            if (Style == "Singer_ID") {
                intent = Intent(this@LoadingActivity, SingerActivity::class.java)
                pools.submit(GetSingerData(StaticData.Singer?.getId()))
            }
            if (Style == "SearchList") {
                intent = Intent(this@LoadingActivity, SongListActivity::class.java)
                pools.submit(GetSonglistData(i, 0))
            }
            if (Style == "List_ID") {
                intent = Intent(this@LoadingActivity, SongListActivity::class.java)
                pools.submit(GetSonglistData(StaticData.SongList?.getId(), 0))
            }
            if (Style == "UserList") {
                intent = Intent(this@LoadingActivity, SongListActivity::class.java)
                pools.submit(GetSonglistData(i, 0))
            }
            if (Style == "UserInfo") {
                intent = Intent(this@LoadingActivity, MainActivity::class.java)
                pools.submit(
                    GetUser(StaticData.user?.getAccount(), StaticData.user?.getPassword(),this)
                )
            }
            if (Style == "Square") {
                intent = Intent(this@LoadingActivity, SquareActivity::class.java)
                for (i in 0..4) {
                    StaticData.Square.add(SquareSongList())
                }
                pools.submit(GetSquare("华语", 0))
                pools.submit(GetSquare("欧美", 1))
                pools.submit(GetSquare("流行", 2))
                pools.submit(GetSquare("古风", 3))
                pools.submit(GetSquare("轻音乐", 4))
            }
            if (Style == "SquareList") {
                intent = Intent(this@LoadingActivity, SongListActivity::class.java)
                pools.submit(GetSonglistData(i, 0))
            }
            pools.shutdown()
            try {
                pools.awaitTermination(
                    Long.MAX_VALUE,
                    TimeUnit.NANOSECONDS
                )
                if (Pass) {
                    startActivity(intent)
                    finish()
                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
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