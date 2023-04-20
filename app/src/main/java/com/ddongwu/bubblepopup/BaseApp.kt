package com.ddongwu.bubblepopup

import android.app.Application

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/4/20 15:49 <br/>
 */
class BaseApp : Application() {
    companion object {
        private var mApplication: BaseApp? = null
        fun getApp(): BaseApp {
            return mApplication!!
        }
    }

    override fun onCreate() {
        super.onCreate()
        mApplication = this
    }
}