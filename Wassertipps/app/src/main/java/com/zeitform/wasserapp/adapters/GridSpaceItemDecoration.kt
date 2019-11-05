package com.zeitform.wasserapp.adapters

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class GridSpaceItemDecoration(spacing: Int):  RecyclerView.ItemDecoration(){
    var spacing = spacing
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        with(outRect) {
            top = spacing
            left =  spacing
            right = spacing
            bottom = spacing
            if (parent.getChildAdapterPosition(view) % 2 == 0) {
                left =  spacing + 5
            } else {
                right = spacing + 5
            }

            if(parent.childCount % 2 !=0){
                if(parent.getChildAdapterPosition(view) == parent.childCount-1){
                    println("Last item")
                    left =  spacing + 5
                    right = spacing + 5
                }
            }
        }
    }

}