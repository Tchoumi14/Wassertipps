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

class FavistAdapter (private val context: Context, private val listener: WasserinfoFragment.OnFragmentInteractionListener?, private val dataSource: Array<String>, private val icon:Drawable?) : BaseAdapter() {

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
        val rowView = inflater.inflate(R.layout.list_item, parent, false)
        val item = getItem(position)
        val listContainer = rowView.findViewById<LinearLayout>(R.id.list_item)
        listContainer.setOnClickListener { Handler().postDelayed(Runnable { listener!!.generateWasserinfos(item) }, 500) }
        val leftIcon = rowView.findViewById<ImageView>(R.id.list_item_icon)
        leftIcon.setImageDrawable(getIcon())
        val listText = rowView.findViewById<TextView>(R.id.list_item_text)

        listText.text = item
        return rowView
    }
    private fun getIcon(): Drawable?{
        return icon
    }
}