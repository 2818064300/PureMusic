package com.lie.puremusic.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.adapter.CommentAdapter
import com.lie.puremusic.databinding.ActivityCommentBinding
import com.lie.puremusic.music.netease.Comment
import com.lie.puremusic.ui.base.BaseActivity
import com.lie.puremusic.ui.base.SlideBackActivity
import com.lie.puremusic.utils.MagicHttp.runOnMainThread

class CommentActivity : BaseActivity() {

    private lateinit var binding: ActivityCommentBinding

    override fun initBinding() {
        binding = ActivityCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun initData() {
        StaticData.Songs?.id?.let {
            Comment.getComment(it) {
                runOnMainThread {
                    binding.rvComment.layoutManager = LinearLayoutManager(this@CommentActivity)
                    binding.rvComment.adapter =
                        CommentAdapter(this@CommentActivity, it, this@CommentActivity)
                }
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(
            R.anim.anim_no_anim,
            R.anim.anim_slide_exit_bottom
        )
    }
}