package com.lie.puremusic.ui.base

import android.content.Context
import android.view.View
import com.lie.puremusic.widget.SlideBackLayout

/**
 * 可拖拽 Activity
 */
abstract class SlideBackActivity: BaseActivity() {

    // SlideBackLayout 拖拽关闭 Activity
    private lateinit var slideBackLayout: SlideBackLayout

    var slideBackEnabled: Boolean = true
        set(value) {
            slideBackLayout.viewEnabled = value
            field = value
        }

    fun bindSlide(context: Context, view: View) {
        slideBackLayout = SlideBackLayout(context, view)
        slideBackLayout.bind()
    }

}