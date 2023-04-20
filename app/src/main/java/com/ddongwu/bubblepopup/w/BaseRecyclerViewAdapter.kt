package com.jushuitan.jht.basemodule.widget.rv.w

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.ddongwu.bubblepopup.w.BaseViewHolder
import com.ddongwu.bubblepopup.w.MultiTypeSupport
import com.jushuitan.jht.basemodule.R
import com.jushuitan.jht.basemodule.constant.NoDataTypeEnum
import com.jushuitan.jht.basemodule.utils.NetUtils
import com.jushuitan.jht.basemodule.utils.kotlin.setNewVisibility

/**
 * 类描述：基础RecyclerView<br/>
 * 需要注意如果是有无数据模型时 不能通过notifyItemInserted(0)插入第一条数据，只能通过notifyItemChanged(0)更新
 * 无图暂时支持线性布局，如果非要GridLayoutManager实现无图展示，需要动态设置setSpanSizeLookup 根据状态动态改变数列个数
 * 创建人：吴冬冬<br/>
 * 创建时间：2022/6/14 16:20 <br/>
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder> {
    companion object {
        private val NO_DATA_LAYOUT = R.layout.bm_f_no_data
    }

    private val layoutId: Int
    private var multiTypeSupport: MultiTypeSupport<T>?
    protected var itemList: List<T>?

    private var noDataTypeEnum: NoDataTypeEnum

    private var mOnItemClickListener: OnItemClickListener<T>? = null

    @JvmOverloads
    constructor(
        @LayoutRes layoutId: Int,
        itemList: List<T>?,
        noDataTypeEnum: NoDataTypeEnum = NoDataTypeEnum.NO_DATA
    ) : this(layoutId, null, itemList, noDataTypeEnum)

    @JvmOverloads
    constructor(
        @NonNull multiTypeSupport: MultiTypeSupport<T>,
        itemList: List<T>?,
        noDataTypeEnum: NoDataTypeEnum = NoDataTypeEnum.NO_DATA
    ) : this(-1, multiTypeSupport, itemList, noDataTypeEnum)

    constructor(
        layoutId: Int,
        @Nullable multiTypeSupport: MultiTypeSupport<T>?,
        itemList: List<T>?,
        noDataTypeEnum: NoDataTypeEnum
    ) {
        this.layoutId = layoutId
        this.multiTypeSupport = multiTypeSupport
        this.itemList = itemList
        this.noDataTypeEnum = noDataTypeEnum
    }

    /**
     * 更改没数据展示类型，展示完恢复无数据展示样式
     * 暂时只有在刷新提示无网络可能需要，不了直接都是无数据 初始化设置的默认提示无数据样式
     */
    fun changeNoDataType(noDataTypeEnum: NoDataTypeEnum) {
        val oldNoDataTypeEnum = this.noDataTypeEnum
        this.noDataTypeEnum = noDataTypeEnum
        notifyDataSetChanged()
        this.noDataTypeEnum = oldNoDataTypeEnum
    }

    override fun getItemViewType(position: Int): Int {
        val itemSize = getItemListCount()
        if (NoDataTypeEnum.NULL != noDataTypeEnum && itemSize == 0) {
            //设置了无数据展示时 才展示无数据
            //设置了无数据类型，添加无数据布局
            return NO_DATA_LAYOUT
        }
        if (multiTypeSupport != null) {
            return multiTypeSupport!!.getLayoutId(itemList?.get(position), position)
        }
        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val viewId = when {
            viewType == NO_DATA_LAYOUT -> {
                NO_DATA_LAYOUT
            }
            layoutId != -1 -> {
                //设置单独布局
                layoutId
            }
            multiTypeSupport != null -> {
                //设置了多布局展示
                viewType
            }
            else -> {
                throw IllegalArgumentException("布局未设置，请检查！")
            }
        }

        val view = layoutInflater.inflate(viewId, parent, false)
        val viewHolder = BaseViewHolder(view)
        setOnItemClick(viewHolder, viewType)
        return viewHolder
    }

    private fun setOnItemClick(viewHolder: BaseViewHolder, viewType: Int) {
        //判断当前类型的item是否可以点击
        if (!isNowViewTypeCanClick(viewType)) {
            return
        }
        viewHolder.itemView.setOnClickListener {
            if (mOnItemClickListener != null) {
                if (viewType == R.layout.bm_f_no_data) {
                    mOnItemClickListener!!.onItemClick(null, viewType)
                } else {
                    val position = viewHolder.adapterPosition
                    if (position >= itemList?.size ?: 0) {
                        return@setOnClickListener
                    }
                    mOnItemClickListener!!.onItemClick(itemList?.get(position), viewType)
                }
            }
        }
    }

    /**
     * 当前类型的item 是否可以点击
     */
    protected fun isNowViewTypeCanClick(viewType: Int): Boolean {
        return true
    }

    /**
     * 获取当前数据 数量
     */
    private fun getItemListCount(): Int {
        return itemList?.size ?: 0
    }

    override fun getItemCount(): Int {
        val dataItemCount = getItemListCount()
        if (NoDataTypeEnum.NULL != noDataTypeEnum && dataItemCount == 0) {
            return 1
        }
        return dataItemCount
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        if (getItemViewType(position) == NO_DATA_LAYOUT) {
            if (NetUtils.isNetworkAvailable()) {
                setNoDataLayout(holder, noDataTypeEnum)
            } else {
                //设置无网络数据图
                setNoDataLayout(holder, NoDataTypeEnum.NO_NET)
            }
            return
        }
        if (itemList == null) {
            return
        }
        convert(holder, itemList!![position], position)
    }

    private fun setNoDataLayout(holder: BaseViewHolder, noDataTypeEnum: NoDataTypeEnum) {
        if (noDataTypeEnum.hintStr.isNullOrEmpty()) {
            holder.getView<TextView>(R.id.mTv).setNewVisibility(View.GONE)
        } else {
            holder.getView<TextView>(R.id.mTv).setNewVisibility(View.VISIBLE)
            holder.getView<TextView>(R.id.mTv).text = this.noDataTypeEnum.hintStr
        }
        if (this.noDataTypeEnum.imageResourceId == null) {
            holder.getView<ImageView>(R.id.mIv).setNewVisibility(View.GONE)
        } else {
            holder.getView<ImageView>(R.id.mIv).setNewVisibility(View.VISIBLE)
            holder.getView<ImageView>(R.id.mIv).setImageResource(noDataTypeEnum.imageResourceId!!)
        }
    }

    abstract fun convert(holder: BaseViewHolder, t: T, position: Int)

    override fun onBindViewHolder(
        holder: BaseViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (getItemViewType(position) == NO_DATA_LAYOUT) {
                if (NetUtils.isNetworkAvailable()) {
                    setNoDataLayout(holder, noDataTypeEnum)
                } else {
                    //设置无网络数据图
                    setNoDataLayout(holder, NoDataTypeEnum.NO_NET)
                }
                return
            }
            if (itemList == null) {
                return
            }
            convert(holder, itemList!![position], payloads)
        }
    }

    protected open fun convert(holder: BaseViewHolder, t: T, payloads: MutableList<Any>) {

    }

    public interface OnItemClickListener<T> {
        fun onItemClick(t: T?, viewType: Int)
    }

    public fun setOnItemClickListener(onItemClickListener: OnItemClickListener<T>) {
        mOnItemClickListener = onItemClickListener
    }
}