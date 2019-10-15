package com.zeitform.wasserapp.navfragments

import android.content.Context
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.github.aakira.expandablelayout.ExpandableRelativeLayout
import com.zeitform.wasserapp.R
import androidx.constraintlayout.solver.widgets.WidgetContainer.getBounds
import java.util.Collections.rotate
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.Drawable




// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [FaqFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [FaqFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class FaqFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var faqContainer: LinearLayout
    private lateinit var expandableLayout1: ExpandableRelativeLayout
    private lateinit var expandableLayout2: ExpandableRelativeLayout
    private lateinit var expandableLayout3: ExpandableRelativeLayout
    private lateinit var expandableLayout4: ExpandableRelativeLayout
    private lateinit var expandableLayout5: ExpandableRelativeLayout
    private lateinit var expandableLayout6: ExpandableRelativeLayout
    private lateinit var expandableLayout7: ExpandableRelativeLayout
    private lateinit var expandableLayout8: ExpandableRelativeLayout
    private lateinit var expandableLayout9: ExpandableRelativeLayout
    private lateinit var expandableLayout10: ExpandableRelativeLayout
    private lateinit var expandableLayout11: ExpandableRelativeLayout
    private lateinit var expandableLayout12: ExpandableRelativeLayout
    private lateinit var expandableLayout13: ExpandableRelativeLayout

    private lateinit var expandableButton1: TextView
    private lateinit var expandableButton2: TextView
    private lateinit var expandableButton3: TextView
    private lateinit var expandableButton4: TextView
    private lateinit var expandableButton5: TextView
    private lateinit var expandableButton6: TextView
    private lateinit var expandableButton7: TextView
    private lateinit var expandableButton8: TextView
    private lateinit var expandableButton9: TextView
    private lateinit var expandableButton10: TextView
    private lateinit var expandableButton11: TextView
    private lateinit var expandableButton12: TextView
    private lateinit var expandableButton13: TextView
    private lateinit var expandableText1: TextView
    private lateinit var expandableText2: TextView
    private lateinit var expandableText3: TextView
    private lateinit var expandableText4: TextView
    private lateinit var expandableText5: TextView
    private lateinit var expandableText6: TextView
    private lateinit var expandableText7: TextView
    private lateinit var expandableText8: TextView
    private lateinit var expandableText9: TextView
    private lateinit var expandableText10: TextView
    private lateinit var expandableText11: TextView
    private lateinit var expandableText12: TextView
    private lateinit var expandableText13: TextView
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
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_faq, container, false)
        faqContainer = rootView.findViewById(R.id.faqContainer)
        setTitleOnClick(rootView)
        fillTextContent(rootView)
        return rootView
    }
    private fun setTitleOnClick(rootView: View){
        expandableLayout1 = rootView.findViewById(R.id.expandableLayout1)
        expandableLayout1.collapse()
        expandableLayout2 = rootView.findViewById(R.id.expandableLayout2)
        expandableLayout2.collapse()
        expandableLayout3 = rootView.findViewById(R.id.expandableLayout3)
        expandableLayout3.collapse()
        expandableLayout4 = rootView.findViewById(R.id.expandableLayout4)
        expandableLayout4.collapse()
        expandableLayout5 = rootView.findViewById(R.id.expandableLayout5)
        expandableLayout5.collapse()
        expandableLayout6 = rootView.findViewById(R.id.expandableLayout6)
        expandableLayout6.collapse()
        expandableLayout7 = rootView.findViewById(R.id.expandableLayout7)
        expandableLayout7.collapse()
        expandableLayout8 = rootView.findViewById(R.id.expandableLayout8)
        expandableLayout8.collapse()
        expandableLayout9 = rootView.findViewById(R.id.expandableLayout9)
        expandableLayout9.collapse()
        expandableLayout10 = rootView.findViewById(R.id.expandableLayout10)
        expandableLayout10.collapse()
        expandableLayout11 = rootView.findViewById(R.id.expandableLayout11)
        expandableLayout11.collapse()
        expandableLayout12 = rootView.findViewById(R.id.expandableLayout12)
        expandableLayout12.collapse()
        expandableLayout13 = rootView.findViewById(R.id.expandableLayout13)
        expandableLayout13.collapse()

        expandableButton1 = rootView.findViewById(R.id.expandableButton1)
        expandableButton1.setOnClickListener { expandableLayout1.toggle() }
        expandableButton2 = rootView.findViewById(R.id.expandableButton2)
        expandableButton2.setOnClickListener { expandableLayout2.toggle() }
        expandableButton3 = rootView.findViewById(R.id.expandableButton3)
        expandableButton3.setOnClickListener { expandableLayout3.toggle() }
        expandableButton4 = rootView.findViewById(R.id.expandableButton4)
        expandableButton4.setOnClickListener { expandableLayout4.toggle() }
        expandableButton5 = rootView.findViewById(R.id.expandableButton5)
        expandableButton5.setOnClickListener { expandableLayout5.toggle() }
        expandableButton6 = rootView.findViewById(R.id.expandableButton6)
        expandableButton6.setOnClickListener { expandableLayout6.toggle() }
        expandableButton7 = rootView.findViewById(R.id.expandableButton7)
        expandableButton7.setOnClickListener { expandableLayout7.toggle() }
        expandableButton8 = rootView.findViewById(R.id.expandableButton8)
        expandableButton8.setOnClickListener { expandableLayout8.toggle() }
        expandableButton9 = rootView.findViewById(R.id.expandableButton9)
        expandableButton9.setOnClickListener { expandableLayout9.toggle() }
        expandableButton10 = rootView.findViewById(R.id.expandableButton10)
        expandableButton10.setOnClickListener { expandableLayout10.toggle() }
        expandableButton11 = rootView.findViewById(R.id.expandableButton11)
        expandableButton11.setOnClickListener { expandableLayout11.toggle() }
        expandableButton12 = rootView.findViewById(R.id.expandableButton12)
        expandableButton12.setOnClickListener { expandableLayout12.toggle() }
        expandableButton13 = rootView.findViewById(R.id.expandableButton13)
        expandableButton13.setOnClickListener { expandableLayout13.toggle() }
    }

    private fun fillTextContent(rootView: View){
        expandableText1 = rootView.findViewById(R.id.expandableText1)
        expandableText2 = rootView.findViewById(R.id.expandableText2)
        expandableText3 = rootView.findViewById(R.id.expandableText3)
        expandableText4 = rootView.findViewById(R.id.expandableText4)
        expandableText5 = rootView.findViewById(R.id.expandableText5)
        expandableText6 = rootView.findViewById(R.id.expandableText6)
        expandableText7 = rootView.findViewById(R.id.expandableText7)
        expandableText8 = rootView.findViewById(R.id.expandableText8)
        expandableText9 = rootView.findViewById(R.id.expandableText9)
        expandableText10 = rootView.findViewById(R.id.expandableText10)
        expandableText11 = rootView.findViewById(R.id.expandableText11)
        expandableText12 = rootView.findViewById(R.id.expandableText12)
        expandableText13 = rootView.findViewById(R.id.expandableText13)

        val textArray: ArrayList<TextView> = ArrayList()
        textArray.add(expandableText1)
        textArray.add(expandableText2)
        textArray.add(expandableText3)
        textArray.add(expandableText4)
        textArray.add(expandableText5)
        textArray.add(expandableText6)
        textArray.add(expandableText7)
        textArray.add(expandableText8)
        textArray.add(expandableText9)
        textArray.add(expandableText10)
        textArray.add(expandableText11)
        textArray.add(expandableText12)
        textArray.add(expandableText13)

        var contentArray = resources.getStringArray(R.array.faq_content_array)
        for (i in 0 until contentArray.size){
            textArray[i].text = HtmlCompat.fromHtml(contentArray[i], HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
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
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FaqFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaqFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
