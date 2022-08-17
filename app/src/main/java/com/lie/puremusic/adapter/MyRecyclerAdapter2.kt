package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.pojo.Singer
import de.hdodenhof.circleimageview.CircleImageView

class MyRecyclerAdapter2(private val context: Context, List: MutableList<Singer?>) :
    RecyclerView.Adapter<MyRecyclerAdapter2.InnerHolder>() {
    private var List: MutableList<Singer?> = ArrayList()

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item3, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.tv1.setText(List[position]?.getName())
        val url: String? = List[position]?.getCover_url()
        Glide.with(context)
            .load(url)
            .into(holder.avatar)
        holder.ibtn.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","SingerList")
            intent.putExtra("id",StaticData.Result.getSingers()?.get(position)?.getId())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv1: TextView
        var avatar: CircleImageView
        var ibtn: ImageButton
        var mark: ImageButton

        init {
            tv1 = view.findViewById(R.id.tv1)
            avatar = view.findViewById(R.id.avatar)
            ibtn = view.findViewById(R.id.ibtn)
            mark = view.findViewById(R.id.mark)
        }
    }
}