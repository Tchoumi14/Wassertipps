package com.zeitform.wasserapp.adapters

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.cardview.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.zeitform.wasserapp.MainActivity
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.internalfragments.TippsHaerteFragment


class TippsHaerteAdapter (private val context: Context, private val listener: TippsHaerteFragment.OnFragmentInteractionListener?, private val dataSource: Array<String>, private val imgSource: ArrayList<Int>, private val colorSource: Array<String>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): String {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    //2
    fun getImage(position: Int): Int {
        return imgSource[position]
    }
    fun getColor(position: Int): String {
        return colorSource[position]
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.box_item, parent, false)
        val boxCard = rowView.findViewById<androidx.cardview.widget.CardView>(R.id.box_item_card)
        boxCard.setOnClickListener {
            Handler().postDelayed(Runnable { listener!!.generateHaerteTipps(getItem(position)) }, 500)
          }
        val boxText = rowView.findViewById<TextView>(R.id.box_text)
        boxText.text = getItem(position)
        val boxImage = rowView.findViewById<ImageView>(R.id.box_image)
        //boxCard.setBackgroundColor(Color.parseColor(getColor(position)))
        boxCard.setBackgroundResource(R.drawable.rounded_shape_box)
        val drawable = boxCard.background as GradientDrawable
        drawable.setColor(Color.parseColor(getColor(position)))
        boxImage.setImageResource(getImage(position))
        return rowView
    }
}