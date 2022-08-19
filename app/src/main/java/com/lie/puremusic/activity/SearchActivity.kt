package com.lie.puremusic.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.View.OnLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.adapter.MyRecyclerAdapter2
import com.lie.puremusic.adapter.MyRecyclerAdapter3
import com.lie.puremusic.databinding.ActivitySearchBinding
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        overridePendingTransition(R.anim.top_in, R.anim.top_out)

        var alphaAdapter: AlphaInAnimationAdapter? = null
        if (StaticData.SelectID == 0 || StaticData.SelectID == 4) {
            var adapter = StaticData.Result.getSongs()?.let { MyRecyclerAdapter(
                this,
                StaticData.SearchResult,
                "Search"
            ) }
            alphaAdapter = adapter?.let { AlphaInAnimationAdapter(it) }
        }
        if (StaticData.SelectID == 2) {
            val adapter2 = StaticData.Result.getSingers()?.let { MyRecyclerAdapter2(this, it) }
            alphaAdapter = adapter2?.let { AlphaInAnimationAdapter(it) }
        }
        if (StaticData.SelectID == 3) {
            val adapter3 = StaticData.Result.getSongLists()?.let { MyRecyclerAdapter3(this, it) }
            alphaAdapter = adapter3?.let { AlphaInAnimationAdapter(it) }
        }
        binding.SearchRv.setAdapter(AlphaInAnimationAdapter(alphaAdapter!!))
        binding.SearchRv.setLayoutManager(
            LinearLayoutManager(
                this@SearchActivity,
                LinearLayoutManager.VERTICAL,
                false
            )
        )
        binding.Search.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@SearchActivity, PreSearchActivity::class.java)
            startActivity(intent)
            finish()
        })
        binding.FAB.setOnClickListener(View.OnClickListener { binding.RefreshLayout.autoRefresh() })
        binding.FAB.setOnLongClickListener(OnLongClickListener {
            binding.SearchRv.smoothScrollToPosition(0)
            false
        })
        binding.Keywords.setText(StaticData.KeyWords)
        val finalAlphaAdapter = alphaAdapter
        binding.RefreshLayout.setOnRefreshListener(OnRefreshListener { refreshLayout ->
            for (i in 0 until finalAlphaAdapter.itemCount) {
                finalAlphaAdapter.notifyItemChanged(i)
            }
            refreshLayout.finishRefresh()
        })
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}