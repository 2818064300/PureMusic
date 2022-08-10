package com.lie.puremusic.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.pojo.Singer
import de.hdodenhof.circleimageview.CircleImageView

class MyRecyclerGridAdapter2(
    private val context: Context,
    List: MutableList<Singer?>,
) : RecyclerView.Adapter<MyRecyclerGridAdapter2.InnerHolder>() {
    private val List: MutableList<Singer?>

    init {
        this.List = List
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item7, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        Glide.with(context)
            .load(StaticData.Home.getSingers()?.get(position)?.getCover_url())
            .into(holder.CircleImageView)
        holder.CircleImageView_TextView.setText(StaticData.Home.getSingers()?.get(position)?.getName())
        holder.CircleImageView.setOnClickListener {
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","SingerList")
            intent.putExtra("index",position)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return List.size
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