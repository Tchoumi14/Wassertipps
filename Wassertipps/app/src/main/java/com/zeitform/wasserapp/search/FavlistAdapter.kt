package com.zeitform.wasserapp.search

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.navfragments.WasserinfoFragment
import org.json.JSONObject

class FavlistAdapter (private val context: Context, private val dataSource: ArrayList<JSONObject>) : BaseAdapter() {

    private val inflater: LayoutInflater
            = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    //1
    override fun getCount(): Int {
        return dataSource.size
    }

    //2
    override fun getItem(position: Int): JSONObject {
        return dataSource[position]
    }

    //3
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    fun updateList() {
        notifyDataSetChanged()
    }
    //4
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Get view for row item
        val rowView = inflater.inflate(R.layout.fav_listitem, parent, false)
        val item = getItem(position)
        val listContainer = rowView.findViewById<LinearLayout>(R.id.fav_listitem)
        listContainer.setOnClickListener { println("Item selected"+item.optString("plz")) }
        val city = rowView.findViewById<TextView>(R.id.city_name)
        val addresse = rowView.findViewById<TextView>(R.id.addresse)
        val deleteBtn = rowView.findViewById<Button>(R.id.fav_delete_btn)
        deleteBtn.setOnClickListener { println("DELETED") }
        city.text = item.optString("ort")
        addresse.text = item.optString("plz")+", "+item.optString("land")
        return rowView
    }

}