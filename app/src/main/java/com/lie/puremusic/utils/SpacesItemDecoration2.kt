package com.lie.puremusic.utils

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class SpacesItemDecoration2 : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = 0
        outRect.bottom = 0
        if(parent.getChildAdapterPosition(view) == 1){
            outRect.left = 0
            outRect.right = 32
        }
        if(parent.getChildAdapterPosition(view) == 2){
            outRect.left = 32
            outRect.right = 32
        }
        if(parent.getChildAdapterPosition(view) == 3){
            outRect.left = 32
            outRect.right = 32
        }
        if(parent.getChildAdapterPosition(view) == 4){
            outRect.left = 32
            outRect.right = 32
        }
        if(parent.getChildAdapterPosition(view) == 5){
            outRect.left = 32
            outRect.right = 0
        }
    }
}