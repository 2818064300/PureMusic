package com.lie.puremusic.activity

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.dso.ext.visible
import com.lie.puremusic.adapter.ListAdapter
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.SearchHotAdapter
import com.lie.puremusic.databinding.ActivityPreSearchBinding
import com.lie.puremusic.music.netease.SearchDefault
import com.lie.puremusic.music.netease.SearchHot
import com.lie.puremusic.pojo.Record
import com.lie.puremusic.utils.MagicHttp.runOnMainThread
import com.lie.puremusic.utils.asDrawable
import es.dmoral.toasty.Toasty
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreSearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreSearchBinding
    private var realKeyWord = ""
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

        binding.etSearch.apply {
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
        }
        binding.FAB.visibility = View.GONE
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
//        val adapter = ListAdapter(this, StaticData.Records)
//        val alphaAdapter = AlphaInAnimationAdapter(adapter)
//        binding.PreSearchRv.adapter = AlphaInAnimationAdapter(alphaAdapter)
//        binding.PreSearchRv.layoutManager =
//            LinearLayoutManager(this@PreSearchActivity, LinearLayoutManager.VERTICAL, false)

        binding.etSearch.setOnClickListener{
            search()
        }
        // 搜索框
        binding.etSearch.apply {
            setOnEditorActionListener { _, p1, _ ->
                if (p1 == EditorInfo.IME_ACTION_SEARCH) { // 软键盘点击了搜索
                    search()
                }
                false
            }

            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if (binding.etSearch.text.toString() != "") {
                        binding.ivClear.visibility = View.VISIBLE // 有文字，显示清楚按钮
                    } else {
                        binding.ivClear.visibility = View.INVISIBLE // 隐藏
                    }
                }
            })
        }
        binding.clNetease.background =
            ContextCompat.getDrawable(this@PreSearchActivity, R.drawable.bg_edit_text)
        binding.clNetease.setOnClickListener {
        }
        // QQ
        binding.clQQ.setOnClickListener {
        }
        // 酷我
        binding.clKuwo.setOnClickListener {
//                toast("酷我音源暂只支持精确搜索，需要填入完整歌曲名")
        }
        binding.clBilibili.setOnClickListener {
        }
        // 获取推荐关键词
        SearchDefault.getSearchDefault {
            runOnMainThread {
                // toast(it)
                binding.etSearch.hint = it.data.showKeyword
                realKeyWord = it.data.realkeyword
            }
        }
        SearchHot.getSearchHot {
            runOnMainThread {
                binding.rvSearchHot.layoutManager = LinearLayoutManager(this)
                val searchHotAdapter = SearchHotAdapter(it)
                searchHotAdapter.setOnItemClick(object : SearchHotAdapter.OnItemClick {
                    override fun onItemClick(view: View?, position: Int) {
                        val searchWord = it.data[position].searchWord
                        binding.etSearch.setText(searchWord)
                        binding.etSearch.setSelection(searchWord.length)
                        search()
                    }
                })
                binding.rvSearchHot.adapter = searchHotAdapter
            }
        }
    }

    /**
     * 搜索音乐
     */
    private fun search() {
        // 关闭软键盘
        val inputMethodManager: InputMethodManager =
            this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.window?.decorView?.windowToken, 0)

        var keywords = binding.etSearch.text.toString()
        if (keywords == "") {
            keywords = realKeyWord
            binding.etSearch.setText(keywords)
            binding.etSearch.setSelection(keywords.length)
        }
        if (keywords != "") {
            GlobalScope.launch {
                StaticData.KeyWords = keywords
                val record = Record(StaticData.SelectID, keywords)
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
            }
        }
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}