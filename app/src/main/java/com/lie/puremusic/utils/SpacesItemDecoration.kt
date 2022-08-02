package com.lie.puremusic.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesItemDecoration : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.left = 25
        outRect.right = 25
        val count = parent.adapter!!.itemCount
        if (parent.getChildAdapterPosition(view) == 0 || parent.getChildAdapterPosition(view) == 1 || parent.getChildAdapterPosition(
                view
            ) == 2
        ) {
            outRect.top = 56
        }
        var count2 = 3
        if (count % 3 != 0) {
            count2 = count % 3
        }
        if (count2 == 3) {
            if (parent.getChildAdapterPosition(view) == count - 3 || parent.getChildAdapterPosition(
                    view
                ) == count - 2 || parent.getChildAdapterPosition(view) == count - 1
            ) {
                outRect.top = 0
                outRect.bottom = 75
            } else {
                outRect.bottom = 45
            }
        }
        if (count2 == 2) {
            if (parent.getChildAdapterPosition(view) == count - 2 || parent.getChildAdapterPosition(
                    view
                ) == count - 1
            ) {
                outRect.top = 0
                outRect.bottom = 75
            } else {
                outRect.bottom = 45
            }
        }
        if (count2 == 1) {
            if (parent.getChildAdapterPosition(view) == count - 1) {
                outRect.top = 0
                outRect.bottom = 75
            } else {
                outRect.bottom = 45
            }
        }
    }
}