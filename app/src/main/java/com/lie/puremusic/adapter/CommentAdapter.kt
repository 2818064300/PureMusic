package com.lie.puremusic.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.music.netease.data.CommentData
import com.lie.puremusic.utils.msTimeToFormatDate

/**
 * 评论 Adapter
 */
class CommentAdapter(private val context: Context, private val commentData: CommentData, private val activity: Activity): RecyclerView.Adapter<CommentAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvContent: TextView = view.findViewById(R.id.tvContent)
        val tvLikedCount: TextView = view.findViewById(R.id.tvLikedCount)
        val ivCover: ImageView = view.findViewById(R.id.ivCover)
        val tvTime: TextView = view.findViewById(R.id.tvTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_comment, parent, false).apply {
            return ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            tvName.text = commentData.hotComments[position].user.nickname
            tvContent.text = commentData.hotComments[position].content
            tvLikedCount.text = commentData.hotComments[position].likedCount.toString()
            tvTime.text = msTimeToFormatDate(commentData.hotComments[position].time)
            Glide.with(context)
                .load(commentData.hotComments[position].user.avatarUrl)
                .into(holder.ivCover)
        }
    }

    override fun getItemCount(): Int {
        return commentData.hotComments.size
    }

}