package io.kekasquad.queue.base.recycler

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class OffsetScrollListener internal constructor(
    private val mLayoutManager: RecyclerView.LayoutManager,
    private val mOffset: Int,
    private val mPageSize: Int,
    private val offsetListener: () -> Unit
) : RecyclerView.OnScrollListener() {
    private var lastTotalItemCount = 0
    private var loading: Boolean = false

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val totalItemCount = mLayoutManager.itemCount
        var lastVisibleItemPosition = 0
        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> lastVisibleItemPosition =
                getLastVisibleItem(
                    mLayoutManager.findLastVisibleItemPositions(null)
                )
            is GridLayoutManager -> lastVisibleItemPosition =
                mLayoutManager.findLastVisibleItemPosition()
            is LinearLayoutManager -> lastVisibleItemPosition =
                mLayoutManager.findLastVisibleItemPosition()
        }

        if (loading && lastTotalItemCount < totalItemCount) {
            lastTotalItemCount = totalItemCount
            loading = false
        }
        if (!loading && lastVisibleItemPosition % mPageSize + mOffset >= mPageSize) {
            offsetListener()
            loading = true
        }
    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxValue = Int.MIN_VALUE
        for (value in lastVisibleItemPositions) {
            if (value > maxValue) maxValue = value
        }
        return maxValue
    }

}