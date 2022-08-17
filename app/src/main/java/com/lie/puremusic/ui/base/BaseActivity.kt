package com.lie.puremusic.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lie.puremusic.manager.ActivityCollector

/**
 * 基类 Activity
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)
        initBinding()
        initData()
        initView()
        initListener()
        initObserver()
        initBroadcastReceiver()
    }

    override fun onStart() {
        super.onStart()
        initShowDialogListener()
    }

    protected open fun initBinding() {}

    protected open fun initView() {}

    protected open fun initData() {}

    protected open fun initListener() {}

    protected open fun initObserver() {}

    protected open fun initBroadcastReceiver() {}

    protected open fun initShowDialogListener() {}

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.removeActivity(this)
    }

}