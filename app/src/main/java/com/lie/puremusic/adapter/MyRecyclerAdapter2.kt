package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.music.netease.data.UserPlaylistData
import com.lie.puremusic.ui.activity.LoadingActivity

class MyRecyclerAdapter2(private val context: Context, private val List : ArrayList<UserPlaylistData.Playlist>?) :
    RecyclerView.Adapter<MyRecyclerAdapter2.InnerHolder>() {
    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivCover: ImageView = view.findViewById(R.id.ivCover)
        var tvTitle: TextView = view.findViewById(R.id.tvTitle)
        var tvSub: TextView = view.findViewById(R.id.tvSub)
        var ibtn: ConstraintLayout = view.findViewById(R.id.ibtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item6, parent, false)
        return InnerHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        with(holder) {
            val UserPlaylist = List?.get(position)
            tvTitle.text = UserPlaylist?.name
            tvSub.text = UserPlaylist?.creator?.nickname
                Glide.with(context)
                    .load(UserPlaylist?.coverImgUrl)
                    .into(holder.ivCover)
            ibtn.setOnClickListener {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "SongList")
                intent.putExtra("id", UserPlaylist?.id)
                intent.putExtra("picUrl", UserPlaylist?.coverImgUrl)
                intent.putExtra("name", UserPlaylist?.name)
                intent.putExtra("playCount", 0)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return List?.size!!
    }
}