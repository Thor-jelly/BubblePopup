package com.ddongwu.bubblepopup.utils

import android.util.TypedValue
import com.ddongwu.bubblepopup.BaseApp

/**
 * 类描述：Int扩展 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/4/19 15:08 <br/>
 */
fun Int.dp2px(): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        BaseApp.getApp().resources.displayMetrics
    ).toInt()
}