package com.zeitform.wasserapp.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.navfragments.WasserinfoFragment

class FavlistAdapter (private val context: Context, private val dataSource: Array<String>) : BaseAdapter() {

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

    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.fav_listitem, parent, false)
        val item = getItem(position)
        val listContainer = rowView.findViewById<LinearLayout>(R.id.fav_listitem)
        listContainer.setOnClickListener { println("Item selected") }
        val city = rowView.findViewById<ImageView>(R.id.city_name)
        val addresse = rowView.findViewById<TextView>(R.id.addresse)

        addresse.text = item
        return rowView
    }
    private fun getIcon(): Drawable?{
        return icon
    }
}