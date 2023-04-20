package com.ddongwu.bubblepopup.w

import androidx.annotation.LayoutRes

/**
 * 类描述：多布局支持<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/6/14 14:33 <br/>
 */
interface MultiTypeSupport<T> {
    /**
     * 根据当前位置或者条目数据返回布局
     */
    @LayoutRes
    fun getLayoutId(item: T?, position: Int): Int
}

