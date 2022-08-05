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
import com.lie.puremusic.pojo.Song
import com.lie.puremusic.pojo.SongList
import com.lie.puremusic.utils.GetSongData
import jp.wasabeef.glide.transformations.BlurTransformation
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.Executors

class MyRecyclerGridAdapter(
    private val context: Context,
    List: MutableList<SongList?>,
    Style: Int
) : RecyclerView.Adapter<MyRecyclerGridAdapter.InnerHolder>() {
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
            val intent = Intent(context, LoadingActivity::class.java)
            intent.putExtra("style","PopularList")
            intent.putExtra("index",position + StaticData.offset * 6)
//            val intent = Intent(context, LoadingActivity::class.java)
//            intent.putExtra("style","SquareList")
//            intent.putExtra("index",position)
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