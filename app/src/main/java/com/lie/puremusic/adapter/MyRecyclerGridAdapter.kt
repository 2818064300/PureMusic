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
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.card.MaterialCardView
import com.lie.puremusic.utils.CropTransformation
import com.lie.puremusic.R
import com.lie.puremusic.StaticData
import com.lie.puremusic.activity.LoadingActivity
import com.lie.puremusic.activity.SongListActivity
import com.lie.puremusic.pojo.SongList
import jp.wasabeef.glide.transformations.BlurTransformation

class MyRecyclerGridAdapter(private val context: Context, List: MutableList<SongList?>, Style: Int) : RecyclerView.Adapter<MyRecyclerGridAdapter.InnerHolder>() {
    private val List: MutableList<SongList?>
    private val Style: Int

    init {
        this.List = List
        this.Style = Style
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InnerHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item5, parent, false)
        return InnerHolder(view)
    }

    override fun onBindViewHolder(
        holder: InnerHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        holder.tv.setText(List[position]?.getName())
        Glide.with(context)
            .load(List[position]?.getCover_url() + "?param=390y390")
            .transform(
                CropTransformation(
                    0,
                    0,
                    (390 * StaticData.Scale).toInt(),
                    (225 * StaticData.Scale).toInt()
                )
            )
            .into(holder.bg1)
        Glide.with(context)
            .load(List[position]?.getCover_url() + "?param=390y390")
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation<Bitmap?>(
                        CropTransformation(
                            0,
                            (225 * StaticData.Scale).toInt(),
                            (390 * StaticData.Scale).toInt(),
                            (165 * StaticData.Scale).toInt()
                        ), BlurTransformation(25, 3), FitCenter()
                    )
                )
            )
            .into(holder.bg2)
        holder.Card.setOnClickListener {
            if (Style == 1) {
                StaticData.Style = "PopularList"
                StaticData.SongsList_ID = position + StaticData.offset * 6
            }
            if (Style == 2) {
                StaticData.Style = "SquareList"
                StaticData.Square_Position = position
            }
            val intent: Intent
            StaticData.Root = "网易云音乐"
            if (StaticData.Containr.contains(List[position]?.getId())) {
                if (Style == 1) {
                    StaticData.PlayList = List[position]?.getSongs()!!
                }
                if (Style == 2) {
                    StaticData.PlayList = List[StaticData.Square_Position]?.getSongs()!!
                }
                intent = Intent(context, SongListActivity::class.java)
            } else {
                intent = Intent(context, LoadingActivity::class.java)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return List.size
    }

    inner class InnerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val Card: MaterialCardView
        val bg1: ImageView
        val bg2: ImageView
        val tv: TextView

        init {
            Card = view.findViewById(R.id.Card)
            bg1 = view.findViewById(R.id.bg1)
            bg2 = view.findViewById(R.id.bg2)
            tv = view.findViewById(R.id.tv)
        }
    }
}