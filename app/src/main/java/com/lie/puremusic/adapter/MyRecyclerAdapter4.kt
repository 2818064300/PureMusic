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
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.fragment.MusicFragment
import com.lie.puremusic.pojo.SongList
import com.liulishuo.filedownloader.FileDownloader

class MyRecyclerAdapter4(private val context: Context, List: MutableList<SongList?>) :
    RecyclerView.Adapter<MyRecyclerAdapter4.InnerHolder>() {
    private var List: MutableList<SongList?> = ArrayList()

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        FileDownloader.setup(parent.context)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item6, parent, false)
        return InnerHolder(view)
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tv1.setText(List[position]?.getName())
        holder.tv2.setText(List[position]?.getCreator_name())
        val url: String? = List[position]?.getCover_url()
        Glide.with(context)
            .load(url)
            .into(holder.SongsList_bg)
        holder.ibtn.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            if (context === MusicFragment.MusicFragmentContext) {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("style", "UserList")
                intent.putExtra("index", position)
            } else {
                intent.putExtra("style", "SearchList")
                intent.putExtra("index", position)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var SongsList_bg: ImageView
        var tv1: TextView
        var tv2: TextView
        var ibtn: ConstraintLayout

        init {
            SongsList_bg = view.findViewById(R.id.SongsList_bg)
            tv1 = view.findViewById(R.id.tv1)
            tv2 = view.findViewById(R.id.tv2)
            ibtn = view.findViewById(R.id.ibtn)
        }
    }
}