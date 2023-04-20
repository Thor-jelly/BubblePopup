package com.ddongwu.bubblepopup.model

import android.os.Parcelable
import androidx.annotation.LayoutRes
import kotlinx.parcelize.Parcelize

/**
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/2/6 15:04 <br/>
 */
@Parcelize
data class BubblePopupModel @JvmOverloads constructor(
    val id: String,//唯一id
    @LayoutRes val resLayoutId: Int? = null//自定义布局
) : Parcelable
