package com.ddongwu.bubblepopup.w

import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView

/**
 * 类描述：基础ViewHolder<br/>
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/6/14 16:20 <br/>
 */
open class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val mOldViewSa: SparseArray<View> = SparseArray()

    /**
     * 获取View
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : View> getView(@IdRes viewId: Int): T {
        val oldView = mOldViewSa.get(viewId)
        if (oldView == null) {
            val findView = itemView.findViewById<T>(viewId)
            mOldViewSa.put(viewId, findView)
            return findView
        }

        return oldView as T
    }

    /**
     * 设置TextView.text
     */
    fun setText(@IdRes viewId: Int, str: String): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.text = str
        return this
    }

    /**
     * 设置文字颜色
     */
    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): BaseViewHolder {
        val textView = getView<TextView>(viewId)
        textView.setTextColor(textColor)
        return this
    }

    /**
     * 设置背景
     */
    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.setBackgroundResource(backgroundRes)
        return this
    }

    /**
     * 设置背景
     */
    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt backgroundColor: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        view.setBackgroundColor(backgroundColor)
        return this
    }

    /**
     * 设置显隐
     */
    fun setVisibility(@IdRes viewId: Int, @Visibility visibility: Int): BaseViewHolder {
        val view = getView<View>(viewId)
        if (view.visibility != visibility) {
            view.visibility = visibility
        }
        return this
    }

    /**
     * 设置点击事件
     */
    fun setOnClickListener(@IdRes viewId: Int, l: View.OnClickListener): BaseViewHolder {
        val view = getView<View>(viewId)
        view.setOnClickListener(l)
        return this
    }

    /**
     * 设置imageView.setImageResource
     */
    fun setImageResource(@IdRes viewId: Int, resourceId: Int): BaseViewHolder {
        val imageView = getView<ImageView>(viewId)
        imageView.setImageResource(resourceId)
        return this
    }

    fun setPathImage(@IdRes viewId: Int, imageLoader: BaseImageLoader): BaseViewHolder {
        val imageView = getView<ImageView>(viewId)
        imageLoader.loadImage(imageView)
        return this
    }

    abstract class BaseImageLoader(protected val pathStr: String) {
        abstract fun loadImage(imageView: ImageView)
    }
}