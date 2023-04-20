package com.ddongwu.bubblepopup.w

import android.graphics.drawable.Drawable

/**
 * 类描述：节点<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/11/9 10:07 <br/>
 */
data class NodeModel(
    var nextNode: ArrayList<NodeModel>? = null,//下级
    var isExpand: Boolean = false,//是否展开
    var data: Any,//当前model
    var level: Int = 1,//默认一级，如果添加下一节点
    var bg: Drawable? = null//背景色，如果设置二级背景色跟一级相同
) {
    /**
     * 是否有下一集
     */
    fun isHasNextNode(): Boolean {
        return nextNode != null && nextNode!!.size > 0
    }
}