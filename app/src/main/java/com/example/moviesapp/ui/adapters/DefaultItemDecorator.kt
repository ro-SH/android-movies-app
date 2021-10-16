package com.example.moviesapp.ui.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *  Add vertical and horizontal spacing to RecyclerView.
 */
class DefaultItemDecorator(
    private val horizontalSpacing: Int,
    private val verticalSpacing: Int,
    private val mode: Int = 0
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (mode == 0 || parent.getChildLayoutPosition(view) == 0)outRect.left = horizontalSpacing
        if (mode == 0 || parent.getChildLayoutPosition(view) != parent.adapter?.itemCount?.minus(1))
            outRect.right = horizontalSpacing
        if (mode == 1 || parent.getChildLayoutPosition(view) == 0) outRect.top = verticalSpacing
        if (mode == 1 || parent.getChildLayoutPosition(view) != parent.adapter?.itemCount?.minus(1))
            outRect.bottom = verticalSpacing
    }
}
