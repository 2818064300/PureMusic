package com.lie.puremusic.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.standard.data.StandardSongData
import com.lie.puremusic.utils.parse
import com.lie.puremusic.utils.runOnMainThread

/**
 * 播放列表适配器
 */
class PlaylistDialogAdapter(private val list: ArrayList<StandardSongData>?): RecyclerView.Adapter<PlaylistDialogAdapter.ViewHolder>() {

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvArtist: TextView = view.findViewById(R.id.tvArtist)
        val clSong: ConstraintLayout = view.findViewById(R.id.clSong)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        LayoutInflater.from(parent.context).inflate(R.layout.layout_playlist_dialog, parent, false).apply {
            return ViewHolder(this)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val lightSongData = StaticData.Songs
        val songData = list?.get(position)
        if (songData == lightSongData) {
            holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.blue))
            holder.tvArtist.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.blue))
        } else {
            holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.colorTextForeground))
            holder.tvArtist.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.colorTextForeground))
        }
        holder.clSong.setOnClickListener {
            StaticData.Songs = list?.get(position)
            if (songData == StaticData.Songs) {
                holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.blue))
                holder.tvArtist.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.blue))
            } else {
                holder.tvName.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.colorTextForeground))
                holder.tvArtist.setTextColor(ContextCompat.getColor(holder.tvName.context, R.color.colorTextForeground))
            }
        }

        holder.tvName.text = songData?.name
        holder.tvArtist.text = list?.get(position)?.artists?.parse()
    }

    override fun getItemCount(): Int {
        return list?.size!!
    }

}