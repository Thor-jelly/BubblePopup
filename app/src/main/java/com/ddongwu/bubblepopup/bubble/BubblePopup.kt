package com.ddongwu.bubblepopup.bubble

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.RecyclerView
import com.ddongwu.bubblepopup.R
import com.ddongwu.bubblepopup.model.BubblePopupModel
import com.ddongwu.bubblepopup.utils.dp2px
import com.ddongwu.bubblepopup.w.BaseViewHolder
import com.ddongwu.bubblepopup.w.MultiTypeSupport
import com.ddongwu.bubblepopup.w.UniversalItemDecoration
import com.jushuitan.jht.basemodule.widget.rv.w.BaseRecyclerViewAdapter

/**
 * 类描述：气泡弹窗 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/11/3 19:34 <br/>
 */
class BubblePopup private constructor(val context: Context, val list: ArrayList<BubblePopupModel>) :
    PopupWindow(context), PopupWindow.OnDismissListener {
    companion object {
        @JvmStatic
        fun create(
            context: Context,
            list: ArrayList<String>?
        ): BubblePopup {
            val newList = arrayListOf<BubblePopupModel>()
            if (!list.isNullOrEmpty()) {
                list.map {
                    BubblePopupModel(it)
                }.toCollection(newList)
            }
            return BubblePopup(context, newList)
        }

        /**
         * 传入自定义view
         */
        @JvmStatic
        fun createCustomView(
            context: Context,
            list: ArrayList<BubblePopupModel>
        ): BubblePopup {
            return BubblePopup(context, list)
        }
    }

    private val mView: View
    private var mAnimationStyle = 0

    private var mAnchorView: View? = null

    @YGravity
    private var mYGravity = YGravity.BELOW

    @XGravity
    private var mXGravity = XGravity.LEFT
    private var mOffsetX = 0
    private var mOffsetY = 0
    private var mCallback: BubblePopupCallback? = null

    /**
     * 初始化调用时候有效果
     */
    fun setAnimationStyleRes(@StyleRes animationStyle: Int): BubblePopup {
        mAnimationStyle = animationStyle
        return this
    }

    fun setItemClick(
        callback: BubblePopupCallback
    ): BubblePopup {
        mCallback = callback
        return this
    }

    fun showAs(
        anchor: View,
        @YGravity verticalGravity: Int,
        @XGravity horizontalGravity: Int,
        xoff: Int = 0,
        yoff: Int = 0
    ) {
        mAnchorView = anchor
        mYGravity = verticalGravity
        mXGravity = horizontalGravity
        mOffsetX = xoff
        mOffsetY = yoff

        mView.measure(makeDropDownMeasureSpec(width), makeDropDownMeasureSpec(height));
        val measureW = mView.measuredWidth
        val measureH = mView.measuredHeight
        val x = calculateX(anchor, horizontalGravity, measureW, xoff)
        val y = calculateY(anchor, verticalGravity, measureH, yoff)

        showAsDropDown(anchor, x, y, Gravity.NO_GRAVITY)
    }

    private fun calculateY(anchor: View, verticalGravity: Int, measureH: Int, yoff: Int): Int {
        var newY = yoff
        when (verticalGravity) {
            YGravity.ABOVE ->
                //anchor view之上
                newY -= measureH + anchor.height

            YGravity.ALIGN_BOTTOM -> {
                //anchor view底部对齐
                newY -= measureH
            }

            YGravity.CENTER ->
                //anchor view垂直居中
                newY -= anchor.height / 2 + measureH / 2

            YGravity.ALIGN_TOP ->
                //anchor view顶部对齐
                newY -= anchor.height

            YGravity.BELOW -> {
                //anchor view之下
            }

            else -> {

            }
        }
        return newY
    }


    private fun calculateX(anchor: View, horizontalGravity: Int, measureW: Int, xoff: Int): Int {
        var newX = xoff
        when (horizontalGravity) {
            XGravity.LEFT -> {
                //anchor view左侧
                newX -= measureW
            }

            XGravity.ALIGN_LEFT -> {
                //与anchor view左边对齐
            }

            XGravity.CENTER -> {
                //anchor view水平居中
                newX += anchor.width / 2 - measureW / 2
            }

            XGravity.RIGHT -> {
                //anchor view右侧
                newX += anchor.width
            }

            XGravity.ALIGN_RIGHT -> {
                //与anchor view右边对齐
                newX -= measureW - anchor.width
            }

            else -> {

            }
        }

        return newX
    }

    private fun makeDropDownMeasureSpec(measureSpec: Int): Int {
        return View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.getSize(measureSpec),
            getDropDownMeasureSpecMode(measureSpec)
        )
    }

    private fun getDropDownMeasureSpecMode(measureSpec: Int): Int {
        return when (measureSpec) {
            ViewGroup.LayoutParams.WRAP_CONTENT -> View.MeasureSpec.UNSPECIFIED
            else -> View.MeasureSpec.EXACTLY
        }
    }

    private fun updateAnimationStyle() {
        if (mAnimationStyle != 0) {
            this.animationStyle = mAnimationStyle
        }
    }

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        isFocusable = true
        isOutsideTouchable = true
        isClippingEnabled = false
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        mView = LayoutInflater.from(this.context).inflate(R.layout.midm_bubble_right_pop, null)
        contentView = mView
//        val arrowV = mView.findViewById<View>(R.id.v_arrow)
//        arrowV.background =
//            TriangleDrawable(
//                TriangleDrawable.TOP,
//                Color.WHITE
//            )
        val rv = mView.findViewById<RecyclerView>(R.id.rv)
        rv.addItemDecoration(
            UniversalItemDecoration(
                1.dp2px(),
                ColorDrawable(Color.parseColor("#66D7DCE9"))
            )
        )
        rv.adapter = object :
            BaseRecyclerViewAdapter<BubblePopupModel>(object : MultiTypeSupport<BubblePopupModel> {
                override fun getLayoutId(item: BubblePopupModel?, position: Int): Int {
                    if (item?.resLayoutId != null) {
                        return R.layout.midm_bubble_item_pop_p
                    } else {
                        return R.layout.midm_bubble_item_pop
                    }
                }
            }, list) {
            private val defaultView = R.layout.midm_bubble_item_pop
            override fun convert(holder: BaseViewHolder, t: BubblePopupModel, position: Int) {
                if (getItemViewType(position) == defaultView) {
                    holder.getView<TextView>(R.id.tv).text = t.id
                } else {
                    holder.getView<LinearLayout>(R.id.ll).apply {
                        removeAllViews()
                        addView(
                            LayoutInflater.from(holder.itemView.context)
                                .inflate(t.resLayoutId ?: 0, this, false)
                        )
                        postInvalidate()
                    }
                }
                holder.itemView.setOnClickListener {
                    mCallback?.click(t.id)
                    dismiss()
                }

            }
        }

        setOnDismissListener(this)
    }

    override fun onDismiss() {

    }
}