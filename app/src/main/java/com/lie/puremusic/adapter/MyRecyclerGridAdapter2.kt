package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.music.netease.SingerRecommend
import com.lie.puremusic.ui.activity.LoadingActivity
import de.hdodenhof.circleimageview.CircleImageView

class MyRecyclerGridAdapter2(
    private val context: Context,
    private val singerRecommendDataResult: ArrayList<SingerRecommend.SingerRecommendDataResult>,
) : RecyclerView.Adapter<MyRecyclerGridAdapter2.InnerHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item4, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val singer = singerRecommendDataResult[position]
        Glide.with(context)
            .load(singer.picUrl)
            .into(holder.CircleImageView)
        holder.CircleImageView_TextView.text = singer.name
        holder.CircleImageView.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","SingerList")
            intent.putExtra("id",singer.id)
            intent.putExtra("picUrl", singer.picUrl)
            intent.putExtra("name", singer.name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return singerRecommendDataResult.size
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val CircleImageView: CircleImageView
        val CircleImageView_TextView: TextView

        init {
            CircleImageView = view.findViewById(R.id.CircleImageView)
            CircleImageView_TextView = view.findViewById(R.id.CircleImageView_TextView)
        }
    }
}