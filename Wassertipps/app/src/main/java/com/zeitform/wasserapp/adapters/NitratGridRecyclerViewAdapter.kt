package com.zeitform.wasserapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.internalfragments.TippsNitratFragment

class NitratGridRecyclerViewAdapter(private val context: Context, private val listener: TippsNitratFragment.OnFragmentInteractionListener?, private val dataSource: Array<String>, private val imgSource: ArrayList<Int>, private val colorSource: Array<String>): RecyclerView.Adapter<NitratGridViewHolder>(){
    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NitratGridViewHolder {
        val boxView = inflater.inflate(R.layout.box_item, parent, false)
        return NitratGridViewHolder(boxView, listener)
    }

    override fun getItemCount(): Int {
        return dataSource.size
    }

    private fun getItem(position: Int): String {
        return dataSource[position]
    }
    private fun getImage(position: Int): Int {
        return imgSource[position]
    }
    private fun getColor(position: Int): String {
        return colorSource[position]
    }
    override fun onBindViewHolder(holderNitrat: NitratGridViewHolder, position: Int) {
        val text = getItem(position)
        val image = getImage(position)
        val color= Color.parseColor(getColor(position))
        holderNitrat.updateHolder(text, image, color)
    }

}