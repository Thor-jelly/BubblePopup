package com.ddongwu.bubblepopup.w

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ddongwu.bubblepopup.R

/**
 * 类描述：RecyclerView万能分割线,现在只有2个LayoutManager(线性和表格)<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/6/14 16:20 <br/>
 */
private const val MODE_UN_KNOW = -1
private const val MODE_LINEAR_HORIZONTAL = 0
private const val MODE_LINEAR_VERTICAL = 1
private const val MODE_GRID_HORIZONTAL = 2
private const val MODE_GRID_VERTICAL = 3

class UniversalItemDecoration @JvmOverloads constructor(
    private val width: Int = 1,
    private val divider: Drawable = ColorDrawable(Color.TRANSPARENT),
    private val isShowLastLineDecoration: Boolean = false
) : RecyclerView.ItemDecoration() {
    /**
     * rv manager模式
     */
    private var mMode = MODE_UN_KNOW

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //首先获取manager方式
        initManagerMode(parent)
        //获取当前view的position
        val nowViewPosition = parent.getChildAdapterPosition(view)
        //根据模式设置divider
        when (mMode) {
            MODE_LINEAR_HORIZONTAL -> {
                //如果是线性水平布局
                if (nowViewPosition != parent.adapter!!.itemCount - 1 || isShowLastLineDecoration) {
                    //如果是最后一行则不显示
                    outRect.right = width
                } else {
                    outRect.right = 0
                }
            }
            MODE_LINEAR_VERTICAL -> {
                //如果是线性垂直布局
                if (nowViewPosition != parent.adapter!!.itemCount - 1 || isShowLastLineDecoration) {
                    //如果是最后一行则不显示
                    outRect.bottom = width
                } else {
                    outRect.bottom = 0
                }
            }
            MODE_GRID_HORIZONTAL -> {
                //表格布局垂直方向
                val nowGridLM = parent.layoutManager as GridLayoutManager
                //列数
                val spanCount = nowGridLM.spanCount
                //当前所在列
                val nowColumn = (nowViewPosition % spanCount) + 1
                val top = (nowColumn - 1) * width / spanCount
                val bottom = (spanCount - nowColumn) * width / spanCount

                //最后一行没有下间距
                //其他都有
                //是否是正好列都填满行
                //总item个数
                val itemCount = parent.adapter!!.itemCount
                val lineCount = if (itemCount % spanCount == 0) {
                    itemCount / spanCount
                } else {
                    itemCount / spanCount + 1
                }
                //是否在最后一行
                val isLastLine =
                    ((nowViewPosition + 1) / spanCount + if ((nowViewPosition + 1) % spanCount == 0) 0 else 1) == lineCount
                var right = 0
                if (!isLastLine) {
                    right = width
                }
                outRect.top = top
                outRect.bottom = bottom
                outRect.left = 0
                outRect.right = right
            }
            MODE_GRID_VERTICAL -> {
                //表格布局垂直方向
                val nowGridLM = parent.layoutManager as GridLayoutManager
                //列数
                val spanCount = nowGridLM.spanCount
                //当前所在列
                val nowColumn = (nowViewPosition % spanCount) + 1
                val left = (nowColumn - 1) * width / spanCount
                val right = (spanCount - nowColumn) * width / spanCount

                //最后一行没有下间距
                //其他都有
                //是否是正好列都填满行
                //总item个数
                val itemCount = parent.adapter!!.itemCount
                val lineCount = if (itemCount % spanCount == 0) {
                    itemCount / spanCount
                } else {
                    itemCount / spanCount + 1
                }
                //是否在最后一行
                val isLastLine =
                    ((nowViewPosition + 1) / spanCount + if ((nowViewPosition + 1) % spanCount == 0) 0 else 1) == lineCount
                var bottom = 0
                if (!isLastLine) {
                    bottom = width
                }
                outRect.top = 0
                outRect.bottom = bottom
                outRect.left = left
                outRect.right = right
            }
        }
    }

    /**
     * 初始化manager模式
     */
    private fun initManagerMode(parent: RecyclerView) {
        val nowManager = parent.layoutManager
        mMode = when (nowManager) {
            is GridLayoutManager -> {
                val nowOrientation = nowManager.orientation
                when (nowOrientation) {
                    LinearLayout.HORIZONTAL -> MODE_GRID_HORIZONTAL
                    else -> MODE_GRID_VERTICAL
                }
            }
            is LinearLayoutManager -> {
                val nowOrientation = nowManager.orientation
                when (nowOrientation) {
                    LinearLayout.HORIZONTAL -> MODE_LINEAR_HORIZONTAL
                    else -> MODE_LINEAR_VERTICAL
                }
            }
            else -> MODE_UN_KNOW
        }
    }

    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        when (mMode) {
            MODE_LINEAR_HORIZONTAL -> {
                //如果是线性水平布局
                drawVertical(canvas, parent)
            }
            MODE_LINEAR_VERTICAL -> {
                //如果是线性垂直布局
                drawHorizontal(canvas, parent)
            }
            MODE_GRID_HORIZONTAL, MODE_GRID_VERTICAL -> {
                //表格布局
                drawHorizontal(canvas, parent)
                drawVertical(canvas, parent)
            }
        }
    }


    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        val itemCount = parent.adapter!!.itemCount
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val childView = parent.getChildAt(i)

            var top = childView.top
            var bottom = childView.bottom

            //顶部recyclerView padding
            val pair = topRecyclerViewPadding(top, parent, bottom)
            top = pair.first
            bottom = pair.second
            //底部recyclerView padding
            val pair1 = bottomRecyclerViewPadding(bottom, parent, top)
            bottom = pair1.first
            top = pair1.second

            var left = childView.right
            var right = when (mMode) {
                MODE_LINEAR_HORIZONTAL -> {
                    if ((i == itemCount - 1 && !isShowLastLineDecoration)
                        || parent.adapter!!.getItemViewType(i) == R.layout.bm_f_no_data
                    ) {
                        left
                    } else {
                        left + width
                    }
                }
                MODE_GRID_HORIZONTAL -> {
                    //表格布局垂直方向
                    val nowGridLM = parent.layoutManager as GridLayoutManager
                    //列数
                    val spanCount = nowGridLM.spanCount
                    //是否在最后一行
                    val lineCount = if (itemCount % spanCount == 0) {
                        itemCount / spanCount
                    } else {
                        itemCount / spanCount + 1
                    }
                    //是否在最后一行
                    val isLastLine =
                        ((i + 1) / spanCount + if ((i + 1) % spanCount == 0) 0 else 1) == lineCount
                    if (!isShowLastLineDecoration && isLastLine) {
                        left
                    } else {
                        left + width
                    }
                }
                else -> {
                    left + width
                }
            }

            //左侧recyclerView padding
            val pair2 = leftRecyclerViewPadding(left, parent, right)
            left = pair2.first
            right = pair2.second
            //右侧recyclerView padding
            val pair3 = rightRecyclerViewPadding(right, parent, left)
            left = pair3.first
            right = pair3.second

            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
    }

    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        val itemCount = parent.adapter!!.itemCount
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
//            val params = child.layoutParams as RecyclerView.LayoutParams
            var top = child.bottom
            var bottom = when (mMode) {
                MODE_LINEAR_VERTICAL -> {
                    if ((i == itemCount - 1 && !isShowLastLineDecoration)
                        || parent.adapter!!.getItemViewType(i) == R.layout.bm_f_no_data
                    ) {
                        top
                    } else {
                        top + width
                    }
                }
                MODE_GRID_VERTICAL -> {
                    //表格布局垂直方向
                    val nowGridLM = parent.layoutManager as GridLayoutManager
                    //列数
                    val spanCount = nowGridLM.spanCount
                    //是否在最后一行
                    val lineCount = if (itemCount % spanCount == 0) {
                        itemCount / spanCount
                    } else {
                        itemCount / spanCount + 1
                    }
                    //是否在最后一行
                    val isLastLine =
                        ((i + 1) / spanCount + if ((i + 1) % spanCount == 0) 0 else 1) == lineCount
                    if (!isShowLastLineDecoration && isLastLine) {
                        top
                    } else {
                        top + width
                    }
                }
                else -> {
                    top + width
                }
            }

            //顶部recyclerView padding
            val pair = topRecyclerViewPadding(top, parent, bottom)
            top = pair.first
            bottom = pair.second
            //底部recyclerView padding
            val pair1 = bottomRecyclerViewPadding(bottom, parent, top)
            bottom = pair1.first
            top = pair1.second

            var left = child.left
            var right = when (mMode) {
                MODE_LINEAR_VERTICAL -> {
                    child.right
                }
                MODE_GRID_HORIZONTAL -> {
                    //表格布局垂直方向
                    val nowGridLM = parent.layoutManager as GridLayoutManager
                    //列数
                    val spanCount = nowGridLM.spanCount
                    //是否在最后一行
                    val lineCount = if (itemCount % spanCount == 0) {
                        itemCount / spanCount
                    } else {
                        itemCount / spanCount + 1
                    }
                    //是否在最后一行
                    val isLastLine =
                        ((i + 1) / spanCount + if ((i + 1) % spanCount == 0) 0 else 1) == lineCount
                    if (!isShowLastLineDecoration && isLastLine) {
                        child.right
                    } else {
                        child.right + width
                    }
                }
                else -> {
                    child.right + width
                }
            }

            //左侧recyclerView padding
            val pair2 = leftRecyclerViewPadding(left, parent, right)
            left = pair2.first
            right = pair2.second
            //右侧recyclerView padding
            val pair3 = rightRecyclerViewPadding(right, parent, left)
            left = pair3.first
            right = pair3.second

            divider.setBounds(left, top, right, bottom)
            divider.draw(canvas)
        }
    }

    private fun rightRecyclerViewPadding(
        right: Int,
        parent: RecyclerView,
        left: Int
    ): Pair<Int, Int> {
        var right1 = right
        var left1 = left
        if (right1 > parent.width - parent.paddingRight) {
            right1 = parent.width - parent.paddingRight
        }
        if (left1 > parent.width - parent.paddingRight) {
            left1 = 0
            right1 = 0
        }
        return Pair(left1, right1)
    }

    private fun leftRecyclerViewPadding(
        left: Int,
        parent: RecyclerView,
        right: Int
    ): Pair<Int, Int> {
        var left1 = left
        var right1 = right
        if (left1 < parent.paddingLeft) {
            left1 = parent.paddingLeft
        }
        if (right1 < parent.paddingLeft) {
            left1 = 0
            right1 = 0
        }
        return Pair(left1, right1)
    }

    private fun bottomRecyclerViewPadding(
        bottom: Int,
        parent: RecyclerView,
        top: Int
    ): Pair<Int, Int> {
        var bottom1 = bottom
        var top1 = top
        if (bottom1 > parent.height - parent.paddingBottom) {
            bottom1 = parent.height - parent.paddingBottom
        }
        if (top1 > parent.height - parent.paddingBottom) {
            top1 = 0
            bottom1 = 0
        }
        return Pair(bottom1, top1)
    }

    private fun topRecyclerViewPadding(
        top: Int,
        parent: RecyclerView,
        bottom: Int
    ): Pair<Int, Int> {
        var top1 = top
        var bottom1 = bottom
        if (top1 < parent.paddingTop) {
            top1 = parent.paddingTop
        }
        if (bottom1 < parent.paddingTop) {
            top1 = 0
            bottom1 = 0
        }
        return Pair(top1, bottom1)
    }
}