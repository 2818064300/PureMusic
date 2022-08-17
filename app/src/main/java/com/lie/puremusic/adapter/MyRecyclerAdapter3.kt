package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.fragment.MusicFragment
import com.lie.puremusic.pojo.SongList
import com.liulishuo.filedownloader.FileDownloader

class MyRecyclerAdapter3(private val context: Context, List: MutableList<SongList?>) :
    RecyclerView.Adapter<MyRecyclerAdapter3.InnerHolder>() {
    private var List: MutableList<SongList?> = ArrayList()

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        FileDownloader.setup(parent.context)
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item4, parent, false)
        return InnerHolder(view)
    }

    @SuppressLint("RecyclerView", "SetTextI18n")
    override fun onBindViewHolder(holder: InnerHolder, position: Int) {
        holder.tv1.setText(List[position]?.getName())
        holder.tv2.setText(List[position]?.getCreator_name())
        holder.tv5.text = "共" + List[position]?.getCount() + "首"
        if ((List[position]?.getBookCount().toString()).length < 3) {
            List[position]?.getBookCount()?.let { holder.Book.setText(it) }
        } else {
            holder.Book.setText(
                (List[position]?.getBookCount().toString()).substring(
                    0,
                    (List[position]?.getBookCount().toString()).length - 3
                ) + "K"
            )
        }
        if ((List[position]?.getPlayCount().toString()).length <= 3) {
            holder.tv4.setText(List[position]?.getPlayCount().toString())
        } else {
            if ((List[position]?.getPlayCount().toString()).length <= 6) {
                holder.tv4.setText(
                    (List[position]?.getPlayCount().toString()).substring(
                        0,
                        (List[position]?.getPlayCount().toString()).length - 3
                    ) + "K"
                )
            } else {
                if ((List[position]?.getPlayCount().toString()).length <= 9) {
                    holder.tv4.setText(
                        (List[position]?.getPlayCount().toString()).substring(
                            0,
                            (List[position]?.getPlayCount().toString()).length - 6
                        ) + "M"
                    )
                } else {
                    if ((List[position]?.getPlayCount().toString()).length < 12) {
                        holder.tv4.setText(
                            (List[position]?.getPlayCount().toString()).substring(
                                0,
                                (List[position]?.getPlayCount().toString()).length - 9
                            ) + "B"
                        )
                    } else {
                        holder.tv4.text = "TOP"
                    }
                }
            }
        }
        val url: String? = List[position]?.getCover_url()
        Glide.with(context)
            .load(url)
            .into(holder.SongsList_bg)
        holder.ibtn.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style", "SongList")
            if (context === MusicFragment.MusicFragmentContext) {
                val intent = Intent(context, LoadingActivity::class.java)
                intent.putExtra("id", StaticData.UserPlaylistData?.get(position)?.id)
            } else {
                intent.putExtra("id", StaticData.Result.getSongLists()?.get(position)?.getId())
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
        var tv4: TextView
        var tv5: TextView
        var ibtn: ImageButton
        var Book: TextView

        init {
            SongsList_bg = view.findViewById(R.id.SongsList_bg)
            tv1 = view.findViewById(R.id.tv1)
            tv2 = view.findViewById(R.id.tv2)
            ibtn = view.findViewById(R.id.ibtn)
            Book = view.findViewById(R.id.Book)
            tv4 = view.findViewById(R.id.tv4)
            tv5 = view.findViewById(R.id.tv5)
        }
    }
}