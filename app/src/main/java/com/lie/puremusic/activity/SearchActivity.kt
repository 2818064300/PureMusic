package com.lie.puremusic.activity

import android.content.Intent
import android.view.View
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivitySearchBinding
import com.lie.puremusic.ui.base.BaseActivity
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun initBinding() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
    }

    override fun initView() {
        var alphaAdapter: AlphaInAnimationAdapter? = null
        if (StaticData.SelectID == 0 || StaticData.SelectID == 4) {
            val adapter = MyRecyclerAdapter(
                this,
                StaticData.SearchResult,
                "Search"
            )
            alphaAdapter = AlphaInAnimationAdapter(adapter)
        }
        binding.SearchRv.setAdapter(AlphaInAnimationAdapter(alphaAdapter!!))
        binding.SearchRv.setLayoutManager(
            LinearLayoutManager(
                this@SearchActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        binding.RefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            refreshLayout.finishRefresh()
        })
        binding.Keywords.text = StaticData.KeyWords
    }

    override fun initListener() {
        binding.Search.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SearchActivity, PreSearchActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}