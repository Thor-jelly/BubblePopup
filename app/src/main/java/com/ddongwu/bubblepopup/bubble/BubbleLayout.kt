package com.ddongwu.bubblepopup.bubble

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.ddongwu.bubblepopup.R
import com.ddongwu.bubblepopup.utils.dp2px

/**
 * 类描述：气泡布局 <br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2023/5/16 15:35 <br/>
 */
class BubbleLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    companion object {
        const val LEFT = 1
        const val RIGHT = 2
        const val TOP = 3
        const val BOTTOM = 4

        const val LINE = 1
        const val DASHED = 2
    }

    private var mPain: Paint
    private var mPath: Path

    private var mTopLeftRadius = 5.dp2px()
    private var mTopRightRadius = 5.dp2px()
    private var mBottomLeftRadius = 5.dp2px()
    private var mBottomRightRadius = 5.dp2px()

    //气泡背景色
    private var mBubbleColor = Color.TRANSPARENT

    //气泡边框颜色
    private var mBubbleBorderColor = Color.BLACK

    //气泡边框大小
    private var mBubbleBorderSize = 0F
    private var mBubbleBorderStyle = LINE

    //气泡边框画笔
    private val mBubbleBorderPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)

    //方向
    private var mArrowOrientation = TOP

    //气泡箭头位置
    private var mArrowLocation = 0F
    private var mArrowLocationWeight = 0F

    private var mArrowWidth = 14.dp2px()
    private var mArrowHeight = 8.dp2px()

    private var mWidth = 0
    private var mHeight = 0

    init {
        setWillNotDraw(false)
        initAttr(attrs)
        mPain = Paint()
        mPain.isDither = true
        mPain.isAntiAlias = true
        mPath = Path()
        //mBubbleImageBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN))

        initPaint()
        //setLayerType(LAYER_TYPE_SOFTWARE, null)
    }

    private fun initPaint() {
        //mPaint.setPathEffect(new CornerPathEffect(mBubbleRadius))
        //mPaint.setShadowLayer(mShadowRadius, mShadowX, mShadowY, mShadowColor)
        mPain.color = mBubbleColor

        mBubbleBorderPaint.apply {
            color = mBubbleBorderColor
            strokeWidth = mBubbleBorderSize
            style = Paint.Style.STROKE
            pathEffect = if (mBubbleBorderStyle == DASHED) {
                DashPathEffect(floatArrayOf(4F, 4F), 0F)
            } else {
                null
            }
        }

        initPath()
    }

    private fun initPath() {
        mPath.reset()
        val bubbleBorderSpaceNode = mBubbleBorderSize / 2F
        when (mArrowOrientation) {
            LEFT -> {
                //起点移动到箭头尖
                mPath.moveTo(bubbleBorderSpaceNode, mArrowLocation)
                mPath.lineTo(
                    bubbleBorderSpaceNode + mArrowHeight,
                    mArrowLocation - mArrowWidth / 2
                )
                mPath.lineTo(
                    bubbleBorderSpaceNode + mArrowHeight,
                    mTopLeftRadius + bubbleBorderSpaceNode
                )
                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode + mArrowHeight,
                        bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode + mArrowHeight + mTopLeftRadius * 2,
                        bubbleBorderSpaceNode + mTopLeftRadius * 2
                    ),
                    180F, 90F, false
                )
                mPath.lineTo(
                    mWidth - mTopRightRadius - bubbleBorderSpaceNode,
                    bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - mTopRightRadius * 2 - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode + mTopRightRadius * 2
                    ),
                    270F, 90F, false
                )
                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode,
                    mHeight - mBottomRightRadius - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mHeight - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    0F, 90F, false
                )

                mPath.lineTo(
                    mArrowHeight + mBottomLeftRadius + bubbleBorderSpaceNode,
                    mHeight - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode + mArrowHeight,
                        mHeight - mBottomLeftRadius * 2 - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode + mArrowHeight + mBottomLeftRadius * 2,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    90F, 90F, false
                )

                mPath.lineTo(
                    bubbleBorderSpaceNode + mArrowHeight,
                    mArrowLocation + mArrowWidth / 2
                )
            }

            RIGHT -> {
                mPath.moveTo(mWidth - bubbleBorderSpaceNode, mArrowLocation)

                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode - mArrowHeight,
                    mArrowLocation + mArrowWidth / 2
                )

                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode - mArrowHeight,
                    mHeight - mBottomRightRadius - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - bubbleBorderSpaceNode - mArrowHeight - mBottomRightRadius * 2,
                        mHeight - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode - mArrowHeight,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    0F, 90F, false
                )

                mPath.lineTo(
                    bubbleBorderSpaceNode + mBottomLeftRadius,
                    mHeight - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        mHeight - mBottomLeftRadius * 2 - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode + mBottomLeftRadius * 2,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    90F, 90F, false
                )

                mPath.lineTo(
                    bubbleBorderSpaceNode,
                    mTopLeftRadius + bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode,
                        mTopLeftRadius * 2 + bubbleBorderSpaceNode,
                        mTopLeftRadius * 2 + bubbleBorderSpaceNode
                    ),
                    180F, 90F, false
                )

                mPath.lineTo(
                    mWidth - mArrowHeight - mTopRightRadius - bubbleBorderSpaceNode,
                    bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - mArrowHeight - mTopRightRadius * 2 - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode,
                        mWidth - mArrowHeight - bubbleBorderSpaceNode,
                        mTopRightRadius * 2 + bubbleBorderSpaceNode
                    ),
                    270F, 90F, false
                )
                mPath.lineTo(
                    mWidth - mArrowHeight - bubbleBorderSpaceNode,
                    mArrowLocation - mArrowHeight / 2 - bubbleBorderSpaceNode
                )
            }

            TOP -> {
                mPath.moveTo(mArrowLocation, bubbleBorderSpaceNode)

                mPath.lineTo(
                    mArrowLocation + mArrowWidth / 2,
                    mArrowHeight + bubbleBorderSpaceNode
                )


                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode - mTopRightRadius,
                    mArrowHeight + bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - bubbleBorderSpaceNode - mTopRightRadius * 2,
                        mArrowHeight + bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        mArrowHeight + bubbleBorderSpaceNode + mTopRightRadius * 2
                    ),
                    270F, 90F, false
                )

                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode,
                    mHeight - mBottomRightRadius - bubbleBorderSpaceNode
                )
                mPath.arcTo(
                    RectF(
                        mWidth - bubbleBorderSpaceNode - mBottomRightRadius * 2,
                        mHeight - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    0F, 90F, false
                )

                mPath.lineTo(
                    mBottomLeftRadius + bubbleBorderSpaceNode,
                    mHeight - bubbleBorderSpaceNode
                )
                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        mHeight - mBottomLeftRadius * 2 - bubbleBorderSpaceNode,
                        mBottomLeftRadius * 2 + bubbleBorderSpaceNode,
                        mHeight - bubbleBorderSpaceNode
                    ),
                    90F, 90F, false
                )

                mPath.lineTo(
                    bubbleBorderSpaceNode,
                    mArrowHeight + mTopLeftRadius + bubbleBorderSpaceNode
                )
                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        mArrowHeight + bubbleBorderSpaceNode,
                        mTopLeftRadius * 2 + bubbleBorderSpaceNode,
                        mArrowHeight + bubbleBorderSpaceNode + mTopLeftRadius * 2
                    ),
                    180F, 90F, false
                )
                mPath.lineTo(
                    mArrowLocation - mArrowWidth / 2,
                    mArrowHeight + bubbleBorderSpaceNode
                )
            }

            BOTTOM -> {
                mPath.moveTo(mArrowLocation, mHeight - bubbleBorderSpaceNode)

                mPath.lineTo(
                    mArrowLocation - mArrowWidth / 2,
                    mHeight - mArrowHeight - bubbleBorderSpaceNode
                )


                mPath.lineTo(
                    mBottomLeftRadius + bubbleBorderSpaceNode,
                    mHeight - mArrowHeight - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        mHeight - mArrowHeight - mBottomLeftRadius * 2 - bubbleBorderSpaceNode,
                        mBottomLeftRadius * 2 + bubbleBorderSpaceNode,
                        mHeight - mArrowHeight - bubbleBorderSpaceNode
                    ),
                    90F, 90F, false
                )

                mPath.lineTo(
                    bubbleBorderSpaceNode,
                    mTopLeftRadius + bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode,
                        mTopLeftRadius * 2 + bubbleBorderSpaceNode,
                        mTopLeftRadius * 2 + bubbleBorderSpaceNode
                    ),
                    180F, 90F, false
                )

                mPath.lineTo(
                    mWidth - mTopRightRadius - bubbleBorderSpaceNode,
                    bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - mTopRightRadius * 2 - bubbleBorderSpaceNode,
                        bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        mTopRightRadius * 2 + bubbleBorderSpaceNode
                    ),
                    270F, 90F, false
                )

                mPath.lineTo(
                    mWidth - bubbleBorderSpaceNode,
                    mHeight - mArrowHeight - mBottomRightRadius - bubbleBorderSpaceNode
                )

                mPath.arcTo(
                    RectF(
                        mWidth - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mHeight - mArrowHeight - mBottomRightRadius * 2 - bubbleBorderSpaceNode,
                        mWidth - bubbleBorderSpaceNode,
                        mHeight - mArrowHeight - bubbleBorderSpaceNode
                    ),
                    0F, 90F, false
                )

                mPath.lineTo(
                    mArrowLocation + mArrowWidth / 2,
                    mHeight - mArrowHeight - bubbleBorderSpaceNode
                )
            }
        }
        mPath.close()
    }

    private fun initAttr(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout)
        a.apply {
            val radius = a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_radius, 0)
            if (radius != 0) {
                mTopLeftRadius = radius.dp2px()
                mTopRightRadius = mTopLeftRadius
                mBottomLeftRadius = mTopLeftRadius
                mBottomRightRadius = mTopLeftRadius
            } else {
                mTopLeftRadius =
                    a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_top_left_radius, 0)
                        .dp2px()
                mTopRightRadius =
                    a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_top_right_radius, 0)
                        .dp2px()
                mBottomLeftRadius =
                    a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_bottom_left_radius, 0)
                        .dp2px()
                mBottomRightRadius =
                    a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_bottom_right_radius, 0)
                        .dp2px()
            }
            if (mTopLeftRadius == 0
                && mTopRightRadius == 0
                && mBottomLeftRadius == 0
                && mBottomRightRadius == 0
            ) {
                mTopLeftRadius = 5.dp2px()
                mTopRightRadius = mTopLeftRadius
                mBottomLeftRadius = mTopLeftRadius
                mBottomRightRadius = mTopLeftRadius
            }

            mBubbleColor = a.getColor(R.styleable.BubbleLayout_bl_bg_color, Color.TRANSPARENT)

            mBubbleBorderColor = a.getColor(R.styleable.BubbleLayout_bl_border_color, Color.BLACK)
            mBubbleBorderSize = a.getDimension(R.styleable.BubbleLayout_bl_border_size, 0F)
            mBubbleBorderStyle = a.getInt(R.styleable.BubbleLayout_bl_border_style, LINE)
            ///
            mArrowOrientation = a.getInt(R.styleable.BubbleLayout_bl_arrow_orientation, TOP)
            mArrowLocationWeight = a.getFloat(R.styleable.BubbleLayout_bl_arrow_location_weight, 0F)
            if (mArrowLocationWeight == 0F) {
                mArrowLocation = a.getDimensionPixelOffset(
                    R.styleable.BubbleLayout_bl_arrow_location_distance,
                    30.dp2px()
                ).toFloat()
            }
            mArrowWidth =
                a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_arrow_width, 14.dp2px())
            mArrowHeight =
                a.getDimensionPixelOffset(R.styleable.BubbleLayout_bl_arrow_height, 8.dp2px())

            recycle()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        if (mArrowLocationWeight != 0F) {
            mArrowLocation = mWidth * mArrowLocationWeight
        }
        initPath()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawPath(mPath, mPain)

        if (mBubbleBorderSize != 0F) {
            canvas?.drawPath(mPath, mBubbleBorderPaint)
        }
    }
}