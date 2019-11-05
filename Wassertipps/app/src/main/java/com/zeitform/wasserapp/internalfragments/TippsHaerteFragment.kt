package com.zeitform.wasserapp.internalfragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.adapters.GridSpaceItemDecoration
import com.zeitform.wasserapp.adapters.HaerteGridRecyclerViewAdapter


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TippsHaerteFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TippsHaerteFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TippsHaerteFragment : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var gridView: GridView? = null
    private lateinit var gridRecyclerView: RecyclerView
    private lateinit var gridLayoutManager: GridLayoutManager
    private lateinit var listItem: Array<String>
    private var gridDrawables = ArrayList<Int>()
    private lateinit var gridColors: Array<String>
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_tipps_layout, container, false)
        listItem = resources.getStringArray(R.array.haerte_tipps)
        var gridImagesString = resources.getStringArray(R.array.haerte_box_images)
        gridColors = resources.getStringArray(R.array.haerte_box_colors)
        for(i in 0 until gridImagesString.size){
            gridDrawables.add(resources.getIdentifier(gridImagesString[i],"drawable",activity!!.applicationContext.packageName))
        }

        gridRecyclerView = rootView.findViewById(R.id.grid_recyclerview)
        gridRecyclerView.addItemDecoration(GridSpaceItemDecoration(15))
        gridLayoutManager = GridLayoutManager(context,2)
        if(listItem.size % 2 != 0){
            println("ODD")
            gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {

                override fun getSpanSize(position: Int): Int {
                    return if (position == listItem.size-1) { // totalRowCount : How many item you want to show
                        2 // the item in position now takes up 4 spans
                    } else 1
                }
            }
        }
        gridRecyclerView.layoutManager = gridLayoutManager
        gridRecyclerView.adapter = HaerteGridRecyclerViewAdapter(activity!!.applicationContext,listener,listItem,gridDrawables,gridColors)
        return rootView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
        fun generateHaerteTipps(button: String)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TippsHaerteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TippsHaerteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
