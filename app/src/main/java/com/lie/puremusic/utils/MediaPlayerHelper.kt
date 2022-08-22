package com.lie.puremusic.utils

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import java.io.IOException

class MediaPlayerHelper{

    private var instance: MediaPlayerHelper? = null
    private var mContext : Context? = null
    private var mMediaPlayer //MediaPlayer媒体类
            : AudioPlayer? = null
    private var mPath //歌曲路径
            : String? = null

    companion object {
        private var instance: MediaPlayerHelper? = null
        fun getInstance(context: Context): MediaPlayerHelper? {
            if (instance == null) {
                synchronized(MediaPlayerHelper::class.java) {
                    if (instance == null) {
                        instance = MediaPlayerHelper(context)
                    }
                }
            }
            return instance
        }
    }
    private var onMeidaPlayerHelperListener : OnMeidaPlayerHelperListener? = null
    constructor(context: Context){
        mContext = context
        mMediaPlayer = AudioPlayer()
    }
    /**
     * 媒体监听接口，播放准备和播放完成
     */
    interface OnMeidaPlayerHelperListener {
        fun onPrepared(mp: MediaPlayer?)
        fun onError(mp: MediaPlayer?): Boolean
        fun onCompletion(mp: MediaPlayer?)
        fun onBufferingUpdate(mp: MediaPlayer?, i: Int)
    }

    fun setOnMeidaPlayerHelperListener(onMeidaPlayerHelperListener: OnMeidaPlayerHelperListener?) {
        this.onMeidaPlayerHelperListener = onMeidaPlayerHelperListener
    }

    /**
     * 设置当前需要播放的音乐
     */
    fun setPath(path: String) {

        // 1、音乐正在播放，重置音乐播放状态
        if (mMediaPlayer?.isPlaying == true || path != this.mPath) {
            mMediaPlayer?.reset()
        }
        this.mPath = path

        // 2、设置播放音乐路径
        try {
            mContext?.let { mMediaPlayer?.setDataSource(it, Uri.parse(path)) }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // 3、准备播放
        mMediaPlayer?.prepareAsync()
        mMediaPlayer?.setScreenOnWhilePlaying(true)
        mMediaPlayer?.setOnPreparedListener { mp ->
            if (onMeidaPlayerHelperListener != null) {
                onMeidaPlayerHelperListener?.onPrepared(mp)
            }
        }
        mMediaPlayer?.setOnErrorListener { mp, i, i1 ->
            if (onMeidaPlayerHelperListener != null) {
                onMeidaPlayerHelperListener?.onError(mp)
            }
            true
        }
        //监听音乐播放完成
        mMediaPlayer?.setOnCompletionListener { mp ->
            if (onMeidaPlayerHelperListener != null) {
                onMeidaPlayerHelperListener?.onCompletion(mp)
            }
        }
        mMediaPlayer?.setOnBufferingUpdateListener { mp, i ->
            if (onMeidaPlayerHelperListener != null) {
                onMeidaPlayerHelperListener?.onBufferingUpdate(mp, i)
            }
        }
    }

    /**
     * 播放音乐
     */
    fun start() {
        if (mMediaPlayer?.isPlaying == true) {
            return
        }
        mMediaPlayer?.startSmooth()
    }

    /**
     * 返回正在播放的音乐路径AudioSessionId
     */
    val audioSessionId: Int?
        get() = mMediaPlayer?.audioSessionId

    /**
     * 暂停播放
     */
    fun pause() {
        mMediaPlayer?.pauseSmooth()
    }

    fun release() {
        mMediaPlayer?.release()
    }

    fun reset() {
        mMediaPlayer?.reset()
    }

    /**
     * 是否正在播放
     */
    val isPlaying: Boolean
        get() = mMediaPlayer?.isPlaying == true

    /**
     * 获取媒体播放的位置（进度条）
     */
    val currentPosition: Int?
        get() = mMediaPlayer?.currentPosition

    /**
     * 获取媒体播放的总时长
     */
    val duration: Int?
        get() = mMediaPlayer?.duration

    /**
     * 定位到媒体播放进度
     */
    fun seekTo(progress: Int) {
        mMediaPlayer?.seekTo(progress)
    }
}