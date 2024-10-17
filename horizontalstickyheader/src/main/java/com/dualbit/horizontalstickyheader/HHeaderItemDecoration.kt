package com.dualbit.horizontalstickyheader

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class HHeaderItemDecoration(
    parent: RecyclerView,
    private val shouldFadeOutHeader: Boolean = false,
    private val isHeader: (itemPosition: Int) -> Boolean
) : RecyclerView.ItemDecoration() {

    private var currentHeader: Pair<Int, RecyclerView.ViewHolder>? = null

    init {
        parent.adapter?.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onChanged() {
                // Clear saved header as it can be outdated now
                currentHeader = null
            }
        })

        parent.doOnEachNextLayout {
            // Clear saved layout as it may need layout update
            currentHeader = null
        }

        // Handle click on sticky header
        parent.addOnItemTouchListener(object : RecyclerView.SimpleOnItemTouchListener() {
            override fun onInterceptTouchEvent(recyclerView: RecyclerView, motionEvent: MotionEvent): Boolean {
                return if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    motionEvent.x <= currentHeader?.second?.itemView?.right ?: 0
                } else false
            }
        })
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val leftChild = parent.findChildViewUnder(
            parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat()
        ) ?: return
        val leftChildPosition = parent.getChildAdapterPosition(leftChild)
        if (leftChildPosition == RecyclerView.NO_POSITION) {
            return
        }

        val headerView = getHeaderViewForItem(leftChildPosition, parent) ?: return

        val contactPoint = headerView.right + parent.paddingLeft
        val childInContact = getChildInContactHorizontal(parent, contactPoint) ?: return

        if (isHeader(parent.getChildAdapterPosition(childInContact))) {
            moveHeaderHorizontal(c, headerView, childInContact, parent.paddingLeft)
            return
        }

        drawHeaderHorizontal(c, headerView, parent.paddingLeft)
    }

    private fun getHeaderViewForItem(itemPosition: Int, parent: RecyclerView): View? {
        if (parent.adapter == null) {
            return null
        }
        val headerPosition = getHeaderPositionForItem(itemPosition)
        if (headerPosition == RecyclerView.NO_POSITION) return null
        val headerType = parent.adapter?.getItemViewType(headerPosition) ?: return null
        // if match reuse viewHolder
        if (currentHeader?.first == headerPosition && currentHeader?.second?.itemViewType == headerType) {
            return currentHeader?.second?.itemView
        }

        val headerHolder = parent.adapter?.createViewHolder(parent, headerType)
        if (headerHolder != null) {
            parent.adapter?.onBindViewHolder(headerHolder, headerPosition)
            fixLayoutSize(parent, headerHolder.itemView)
            // save for next draw
            currentHeader = headerPosition to headerHolder
        }
        return headerHolder?.itemView
    }

    private fun drawHeaderHorizontal(c: Canvas, header: View, paddingLeft: Int) {
        c.save()
        c.translate(paddingLeft.toFloat(), 0f)
        header.draw(c)
        c.restore()
    }

    private fun moveHeaderHorizontal(c: Canvas, currentHeader: View, nextHeader: View, paddingLeft: Int) {
        c.save()
        if (!shouldFadeOutHeader) {
            c.clipRect(paddingLeft, 0, paddingLeft + currentHeader.width, c.height)
        } else {
            c.saveLayerAlpha(
                RectF(0f, 0f, c.width.toFloat(), c.height.toFloat()),
                (((nextHeader.left - paddingLeft) / nextHeader.width.toFloat()) * 255).toInt()
            )
        }
        c.translate((nextHeader.left - currentHeader.width).toFloat(), 0f)

        currentHeader.draw(c)
        if (shouldFadeOutHeader) {
            c.restore()
        }
        c.restore()
    }

    private fun getChildInContactHorizontal(parent: RecyclerView, contactPoint: Int): View? {
        var childInContact: View? = null
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val mBounds = Rect()
            parent.getDecoratedBoundsWithMargins(child, mBounds)
            if (mBounds.right > contactPoint && mBounds.left <= contactPoint) {
                // This child overlaps the contactPoint
                childInContact = child
                break
            }
        }
        return childInContact
    }

    private fun fixLayoutSize(parent: ViewGroup, view: View) {
        val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.width, View.MeasureSpec.EXACTLY)
        val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.height, View.MeasureSpec.UNSPECIFIED)

        val childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.paddingLeft + parent.paddingRight, view.layoutParams.width)
        val childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.paddingTop + parent.paddingBottom, view.layoutParams.height)

        view.measure(childWidthSpec, childHeightSpec)
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)
    }

    private fun getHeaderPositionForItem(itemPosition: Int): Int {
        var headerPosition = RecyclerView.NO_POSITION
        var currentPosition = itemPosition
        do {
            if (isHeader(currentPosition)) {
                headerPosition = currentPosition
                break
            }
            currentPosition -= 1
        } while (currentPosition >= 0)
        return headerPosition
    }
}

private inline fun View.doOnEachNextLayout(crossinline action: (view: View) -> Unit) {
    addOnLayoutChangeListener { view, _, _, _, _, _, _, _, _ ->
        action(view)
    }
}
