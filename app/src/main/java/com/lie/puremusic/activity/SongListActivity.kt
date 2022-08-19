package com.lie.puremusic.activity

import android.graphics.Bitmap
import android.graphics.Typeface
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.appbar.AppBarLayout
import com.gyf.immersionbar.ktx.immersionBar
import com.lie.puremusic.*
import com.lie.puremusic.adapter.MyRecyclerAdapter
import com.lie.puremusic.databinding.ActivitySongListBinding
import com.lie.puremusic.listener.AppBarStateChangeListener
import com.lie.puremusic.standard.data.StandardPlaylistData
import com.lie.puremusic.ui.base.BaseActivity
import com.lie.puremusic.utils.CropTransformation
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadListener
import com.liulishuo.filedownloader.FileDownloader
import es.dmoral.toasty.Toasty
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import java.io.File

class SongListActivity : BaseActivity() {
    private lateinit var binding: ActivitySongListBinding
    private var PlaylistData: StandardPlaylistData? = null

    override fun initBinding() {
        binding = ActivitySongListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        miniPlayer = binding.miniPlayer
    }
    override fun initData() {
        PlaylistData = StaticData.PlayListData
        FileDownloader.setup(this)
    }
    override fun initView(){
        overridePendingTransition(R.anim.top_in, R.anim.top_out)
        immersionBar {
            statusBarColor(R.color.nullcolor)
            statusBarDarkFont(true)
        }
        binding.RadioButtonFirst.isChecked = true
        binding.RefreshLayout.autoRefresh(200)
        binding.SonglistName.text = PlaylistData?.name
        binding.SonglistCount.text = "共 " + PlaylistData?.songs?.size + " 首"
        binding.tips1.text = PlaylistData?.name
        binding.tips2.text = "共 " + PlaylistData?.songs?.size + " 首"
        val typeface = Typeface.createFromAsset(this.assets, "汉仪雅酷黑65W.ttf")
        binding.Title.setTypeface(typeface)
        binding.Title.setTypeface(typeface)
        Glide.with(this@SongListActivity)
            .load(PlaylistData?.picUrl + "?param=1440y1440")
            .into(binding.Avatar)
        Glide.with(this@SongListActivity)
            .load(PlaylistData?.picUrl + "?param=1440y1440")
            .transform(
                CropTransformation(
                    0,
                    0,
                    (1260 * StaticData.Scale).toInt(),
                    (910 * StaticData.Scale).toInt()
                )
            )
            .into(binding.SonglistBg)
        Glide.with(this@SongListActivity)
            .load(PlaylistData?.picUrl + "?param=1440y1440")
            .apply(
                RequestOptions.bitmapTransform(
                    MultiTransformation<Bitmap?>(
                        CropTransformation(
                            0,
                            (910 * StaticData.Scale).toInt(),
                            (1260 * StaticData.Scale).toInt(),
                            (280 * StaticData.Scale).toInt()
                        ), BlurTransformation(25, 8), FitCenter()
                    )
                )
            )
            .into(binding.SonglistBg2)
        //        Glide.with(SongListActivity.this)
//                .load(SongList.getCover_url() + "?param=1440y1440")
//                .transform(new CropTransformation(0,0,944,683))
//                .into(binding.SonglistBg);
//        Glide.with(SongListActivity.this)
//                .load(SongList.getCover_url() + "?param=1440y1440")
//                .apply(bitmapTransform(new MultiTransformation(new CropTransformation(0,683,944,210),new BlurTransformation(25, 8),new FitCenter())))
//                .into(binding.SonglistBg2);
        val adapter = MyRecyclerAdapter(
            this,
            PlaylistData?.songs,
            "SongList"
        )
        val alphaAdapter = AlphaInAnimationAdapter(adapter)
        binding.SonglistRv.adapter = AlphaInAnimationAdapter(alphaAdapter)
        binding.SonglistRv.layoutManager =
            LinearLayoutManager(this@SongListActivity, LinearLayoutManager.VERTICAL, false)
        binding.RefreshLayout.setHeaderMaxDragRate(4F)
        binding.RefreshLayout.setOnRefreshListener { refreshLayout ->
            for (i in 0 until alphaAdapter.itemCount) {
                alphaAdapter.notifyItemChanged(i)
            }
            refreshLayout.finishRefresh()
        }
    }
    override fun initListener(){
        binding.Back.setOnClickListener { finish() }
        binding.CenterAppbarLayout.addOnOffsetChangedListener(object : AppBarStateChangeListener() {
            override fun onStateChanged(appBarLayout: AppBarLayout?, state: State?) {
                if (state === State.EXPANDED) {
                    binding.Container1.visibility = View.VISIBLE
                    binding.Container2.visibility = View.VISIBLE
                    binding.Container3.visibility = View.GONE
                } else if (state === State.COLLAPSED) {
                    binding.Container1.visibility = View.GONE
                    binding.Container2.visibility = View.GONE
                    binding.Container3.visibility = View.VISIBLE
                } else {
                    binding.Container1.visibility = View.VISIBLE
                    binding.Container2.visibility = View.VISIBLE
                    binding.Container3.visibility = View.GONE
                }
            }
        })
        binding.SonglistBg.setOnLongClickListener {
            File(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/SongList/")
                .mkdirs()
            FileDownloader.getImpl().create(PlaylistData?.picUrl)
                .setPath(Environment.getExternalStorageDirectory().path + "/PureMusic/Picture/SongList/" + PlaylistData?.id + ".png")
                .setListener(object : FileDownloadListener() {
                    override fun pending(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun progress(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun completed(task: BaseDownloadTask) {
                        Toasty.success(
                            MainActivity.context,
                            "缓存成功.",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }

                    override fun paused(
                        task: BaseDownloadTask,
                        soFarBytes: Int,
                        totalBytes: Int
                    ) {
                    }

                    override fun error(task: BaseDownloadTask, e: Throwable) {
                        Toasty.error(
                            MainActivity.context,
                            "网络问题,请稍后再试!",
                            Toast.LENGTH_SHORT,
                            true
                        ).show()
                    }

                    override fun warn(task: BaseDownloadTask) {}
                })
                .start()
            false
        }
    }
    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out)
    }
}