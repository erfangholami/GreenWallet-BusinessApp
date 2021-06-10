package com.greenwallet.business.helper.ui.widget

import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.greenwallet.business.R

class SpinnerWidgetAdapter<T>(private val mapper: Mapper<T>?) :
    BaseAdapter(), SpinnerAdapter {
    private var data: List<T>? = null

    var selected: T? = null
        private set

    private var dataSizeListener: ((Int) -> Unit)? = null
    private var onItemSelectedListener: ((SpinnerItem) -> Unit)? = null

    override fun getCount(): Int {
        return if (data == null) 0 else data!!.size
    }

    override fun getItem(i: Int): T {
        return data!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        var view = view
        if (view == null) view = View.inflate(viewGroup.context, R.layout.spinner_item, null)
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        if (convertView == null) {
            convertView = View.inflate(parent.context, R.layout.spinner_item, null)
        }
        if (mapper != null) {
            (convertView as TextView).text = mapper.getName(data!![position])
        } else {
            (convertView as TextView).text = data!![position].toString()
        }
        return convertView
    }

    fun setData(data: List<T>, selectFirst: Boolean = true) {
        if (selectFirst && data.size > 0) {
            setData(data, data[0])
        } else {
            setData(data, null)
        }
    }

    fun setData(data: List<T>, selected: T?) {
        setDataList(data)
        select(selected)
    }

    private fun setDataList(data: List<T>) {
        this.data = data
        if (dataSizeListener != null) {
            dataSizeListener!!.invoke(data.size)
        }
    }

    fun select(item: T?) {
        if (selected !== item) {
            this.selected = item
            if (onItemSelectedListener != null) {
                onItemSelectedListener!!.invoke(object : SpinnerItem {
                    override val name: String?
                        get() = if (item != null) {
                            if (mapper != null) {
                                mapper.getName(item)
                            } else {
                                item.toString()
                            }
                        } else {
                            null
                        }
                }
                )
            }
        }
    }


    fun onItemSelected(position: Int) {
        if (position >= 0 && data != null && data!!.isNotEmpty()) {
            select(data!![position])
        } else {
            select(null)
        }
    }

    fun setDataSizeListener(dataSizeListener: (Int) -> Unit) {
        this.dataSizeListener = dataSizeListener
    }

    fun setOnItemSelectedListener(onItemSelectedListener: (SpinnerItem) -> Unit) {
        this.onItemSelectedListener = onItemSelectedListener
        onItemSelectedListener.invoke(object : SpinnerItem {
            override val name: String?
                get() = if (selected != null) {
                    if (mapper != null) {
                        mapper.getName(selected!!)
                    } else {
                        selected.toString()
                    }
                } else {
                    null
                }
        })
    }

    interface SpinnerItem {
        val name: String?
    }

    interface Mapper<V> {
        fun getName(item: V): String?
    }
}