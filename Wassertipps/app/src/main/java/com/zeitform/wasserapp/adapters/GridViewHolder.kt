package com.zeitform.wasserapp.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.internalfragments.TippsNitratFragment
import kotlinx.android.synthetic.main.box_item.view.*

class GridViewHolder(itemView: View, private val listener: TippsNitratFragment.OnFragmentInteractionListener?) : RecyclerView.ViewHolder(itemView) {
    var textView = itemView.box_text
    var imageView = itemView.box_image
    var cardView = itemView.box_item_card

    fun updateHolder(text: String, image: Int, color: Int){
        textView.text = text
        imageView.setImageResource(image)
        cardView.setOnClickListener {
                Handler().postDelayed(Runnable { listener!!.generateNitratTipps(text) }, 500)
            }
        cardView.setBackgroundResource(R.drawable.rounded_shape_box)
        val drawable = cardView.background as GradientDrawable
        drawable.setColor(color)
    }
}