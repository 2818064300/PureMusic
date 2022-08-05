package com.lie.puremusic.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.adapter.ListAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.databinding.ActivityPreSearchBinding
import com.lie.puremusic.pojo.Record
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class PreSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreSearchBinding
    private val btn_group: MutableList<ImageButton?> = ArrayList()
    private val tv_group: MutableList<TextView?> = ArrayList()
    private var last_SelectID = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        if (StaticData.Root.equals("网易云音乐")) {
            binding.FAB.setIconResource(R.drawable.icon2)
            binding.FAB.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFFFF")))
            binding.FAB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFf44336"))
        } else {
            binding.FAB.setIconResource(R.drawable.icon1)
            binding.FAB.setIconTint(ColorStateList.valueOf(Color.parseColor("#FF10b355")))
            binding.FAB.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFf6be1a"))
        }
        binding.SearchBar.setOnClickListener { binding.Search.isIconified = false }
        initbtn()
        if (StaticData.Records.size > 0) {
            binding.Card2.visibility = View.VISIBLE
        }
        binding.FAB.setOnClickListener {
            if (StaticData.Root.equals("网易云音乐")) {
                StaticData.Root = "QQ音乐"
                binding.FAB.setIconResource(R.drawable.icon1)
                binding.FAB.setIconTint(ColorStateList.valueOf(Color.parseColor("#FF10b355")))
                binding.FAB.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFf6be1a"))
                Toasty.custom(
                    this,
                    StaticData.Root,
                    R.mipmap.ic_launcher_round,
                    R.color.green,
                    300,
                    false,
                    true
                ).show()
            } else {
                StaticData.Root = "网易云音乐"
                binding.FAB.setIconResource(R.drawable.icon2)
                binding.FAB.setIconTint(ColorStateList.valueOf(Color.parseColor("#FFFFFFFF")))
                binding.FAB.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#FFf44336"))
                Toasty.custom(
                    this,
                    StaticData.Root,
                    R.mipmap.ic_launcher_round,
                    R.color.red,
                    300,
                    false,
                    true
                ).show()
            }
        }
        val adapter = ListAdapter(this, StaticData.Records)
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.PreSearchRv.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.PreSearchRv.layoutManager =
            LinearLayoutManager(this@PreSearchActivity, LinearLayoutManager.VERTICAL, false)
        binding.Search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                StaticData.KeyWords = query
                val record = Record(StaticData.SelectID, query)
                if (StaticData.Root.equals("网易云音乐")) {
                    record.setMusicStyle("网易云音乐")
                } else {
                    record.setMusicStyle("QQ音乐")
                }
                StaticData.Records.add(record)

                val intent = Intent(this@PreSearchActivity, LoadingActivity::class.java)
                intent.putExtra("style","Search")
                startActivity(intent)
                finish()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
    private fun initbtn() {
        StaticData.SelectID = 0
        binding.Card2.visibility = View.GONE
        binding.tv1.setOnClickListener {
            StaticData.SelectID = 0
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 0
        }
        binding.tv2.setOnClickListener {
            StaticData.SelectID = 1
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 1
        }
        binding.tv3.setOnClickListener {
            StaticData.SelectID = 2
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 2
        }
        binding.tv4.setOnClickListener {
            StaticData.SelectID = 3
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 3
        }
        binding.tv5.setOnClickListener {
            StaticData.SelectID = 4
            setbtn(last_SelectID, StaticData.SelectID)
            last_SelectID = 4
        }
        btn_group.add(binding.btn1)
        btn_group.add(binding.btn2)
        btn_group.add(binding.btn3)
        btn_group.add(binding.btn4)
        btn_group.add(binding.btn5)
        tv_group.add(binding.tv1)
        tv_group.add(binding.tv2)
        tv_group.add(binding.tv3)
        tv_group.add(binding.tv4)
        tv_group.add(binding.tv5)
    }

    private fun setbtn(last_SelectID: Int, SelectID: Int) {
        btn_group.get(last_SelectID)?.setVisibility(View.GONE)
        btn_group.get(SelectID)?.setVisibility(View.VISIBLE)
        tv_group.get(last_SelectID)?.setTextColor(Color.parseColor("#FF6399fd"))
        tv_group.get(SelectID)?.setTextColor(Color.parseColor("#FFFFFF"))
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}