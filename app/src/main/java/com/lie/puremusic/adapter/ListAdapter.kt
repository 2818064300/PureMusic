package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.R
import com.lie.puremusic.pojo.Record
import com.lie.puremusic.StaticData

class ListAdapter(private val context: Context, List: MutableList<Record?>) :
    RecyclerView.Adapter<ListAdapter.ListInnerHolder>() {
    private var List: MutableList<Record?> = ArrayList()

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListInnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item2, parent, false)
        return ListInnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: ListInnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        if (position == 0) {
            holder.ball.setBackgroundResource(R.drawable.item_ball2)
            holder.bar1.visibility = View.GONE
        }
        if (position == List.size - 1) {
            holder.ball.setBackgroundResource(R.drawable.item_ball2)
            holder.bar2.visibility = View.GONE
        }
        holder.tv.setText(List[List.size - 1 - position]?.getKeyWord())
        holder.tv.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","Search")
            StaticData.SelectID = List[List.size - 1 - position]?.getStyle()!!
            StaticData.KeyWords = List[List.size - 1 - position]?.getKeyWord()
            if (List[List.size - 1 - position]?.getMusicStyle().equals("网易云音乐")) {
                StaticData.Root = "网易云音乐"
            } else {
                StaticData.Root = "QQ音乐"
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class ListInnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ball: ImageView
        var bar1: ImageView
        var bar2: ImageView
        var tv: TextView

        init {
            ball = view.findViewById(R.id.ball)
            bar1 = view.findViewById(R.id.bar1)
            bar2 = view.findViewById(R.id.bar2)
            tv = view.findViewById(R.id.tv)
        }
    }
}