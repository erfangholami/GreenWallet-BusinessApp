package com.greenwallet.business.scenes.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.greenwallet.business.R
import com.greenwallet.business.databinding.ItemEmptyBinding
import com.greenwallet.business.databinding.ItemLoadingBinding

abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var context: Context

    var emptyStateTitle: String? = null
    var emptyStateDescription: String? = null

    enum class Mode {
        NORMAL,
        SEARCH
    }

    companion object {
        const val ITEM_VIEW_TYPE_PRODUCT = 0
        const val ITEM_VIEW_TYPE_LOADING = 1
        const val ITEM_VIEW_TYPE_EMPTY = 2
        const val ITEM_VIEW_TYPE_ERROR = 3
    }

    var items: ArrayList<T> = arrayListOf()
        set(value) {
            field = value
            notifyDataSetChanged()

            isLoading = false
            hasError = false
            updateEmptyState()
        }

    var isLoading = false
    var hasError = false
    var isEmpty = false

    var mode = Mode.NORMAL

    override fun getItemViewType(position: Int): Int {
        return if (position == items.size) {
            if (isLoading) {
                ITEM_VIEW_TYPE_LOADING
            } else if (hasError) {
                ITEM_VIEW_TYPE_ERROR
            } else {
                ITEM_VIEW_TYPE_EMPTY
            }
        } else {
            ITEM_VIEW_TYPE_PRODUCT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context

        return if (viewType == ITEM_VIEW_TYPE_LOADING) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemLoadingBinding.inflate(layoutInflater, parent, false)

            LoadingItemViewHolder(itemBinding)
        } else if (viewType == ITEM_VIEW_TYPE_EMPTY) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemEmptyBinding.inflate(layoutInflater, parent, false)

            emptyStateTitle?.let {
                itemBinding.tvOhNo.text = it
            }
            emptyStateDescription?.let {
                itemBinding.tvNoItemDescription.text = it
            }

            EmptyItemViewHolder(itemBinding)
        } else if (viewType == ITEM_VIEW_TYPE_ERROR) {
            val layoutInflater = LayoutInflater.from(parent.context)
            val itemBinding = ItemEmptyBinding.inflate(layoutInflater, parent, false)

            EmptyItemViewHolder(itemBinding)
        } else {
            onCreateViewHolder2(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == items.size) {
            if (isLoading) {

            } else if (isEmpty) {
                when (mode) {
                    Mode.NORMAL -> {
                        (holder as EmptyItemViewHolder).setDrawable(ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_common_empty_state,
                            null
                        ))
                    }
                    Mode.SEARCH -> {
                        (holder as EmptyItemViewHolder).setDrawable(
                            ResourcesCompat.getDrawable(
                                context.resources,
                                R.drawable.ic_search_empty_state,
                                null
                            )
                        )
                    }
                }
            }
        } else {
            //item
            onBindViewHolder2(holder, position)
        }
    }

    override fun getItemCount(): Int = items.size + (if (isEmpty || isLoading || hasError) 1 else 0)

    abstract fun onCreateViewHolder2(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
    abstract fun onBindViewHolder2(holder: RecyclerView.ViewHolder, position: Int)

    fun addItems(newItems: ArrayList<T>) {
        hasError(false)
        showLoading(false)
        if (!items.containsAll(newItems)) {
            items.addAll(newItems)
            notifyItemRangeChanged(items.size - newItems.size, newItems.size)
        }

        updateEmptyState()
    }

    fun showLoading(show: Boolean) {
        if (show) {
            hasError(false)
            isEmpty = false
        }

        if (show) {
            isLoading = true
            if (items.size < itemCount) {
                //already showed
            } else {
                notifyItemInserted(items.size)
            }
        } else {
            isLoading = false
            if (items.size < itemCount) {
                notifyItemRemoved(items.size)
            } else {
                //do nothing
            }
        }
    }

    fun hasError(hasError: Boolean) {
        if (hasError) {
            showLoading(false)
            isEmpty = false
        }

        if (hasError) {
            this.hasError = true
            if (items.size < itemCount) {
                //already showed
            } else {
                notifyItemInserted(items.size)
            }
        } else {
            this.hasError = false
            if (items.size < itemCount) {
                notifyItemRemoved(items.size)
            } else {
                //do nothing
            }
        }
    }

    private fun updateEmptyState() {
        if (items.size > 0) {
            isEmpty = false
            if (items.size < itemCount) {
                notifyItemRemoved(items.size)
            } else {
                //don't do anything
            }
        } else {
            isEmpty = true
            notifyItemInserted(items.size)
        }
    }

    class LoadingItemViewHolder(val binding: ItemLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

    class EmptyItemViewHolder(val binding: ItemEmptyBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setDrawable(drawable: Drawable?) {
            binding.ivNoItem.setImageDrawable(drawable)
        }
    }
}

interface LoadMoreCallBack<T, Mode> {
    fun onLoadMoreFinished(newList: ArrayList<T>, mode: Mode)

    fun onLoadMoreFailed()
}