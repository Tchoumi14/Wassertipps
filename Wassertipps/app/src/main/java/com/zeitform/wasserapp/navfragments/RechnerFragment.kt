package com.zeitform.wasserapp.navfragments

import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.zeitform.wasserapp.R
import java.awt.font.TextAttribute


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RechnerFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RechnerFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class RechnerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var aufwachenText: TextView
    private lateinit var einschlafenText: TextView
    private lateinit var timePicker: TimePickerDialog
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
        var rootView = inflater.inflate(R.layout.fragment_rechner, container, false)
        aufwachenText = rootView.findViewById(R.id.aufwachen_text)
        einschlafenText = rootView.findViewById(R.id.einschlafen_text)
        aufwachenText.setOnClickListener {
            var timeText = aufwachenText.text
            var time = timeText.split(" : ")
            var hour = time[0].toInt()
            var min = time[1].toInt()
            openAufwachenTimePicker(hour, min)
        }
        einschlafenText.setOnClickListener {
            var timeText = aufwachenText.text
            var time = timeText.split(" : ")
            var hour = time[0].toInt()
            var min = time[1].toInt()
            openEinschlafenTimePicker(hour, min)
        }
        return rootView
    }

    private fun openAufwachenTimePicker(hourInput: Int, minInput: Int){
        val timePickerListener = TimePickerDialog.OnTimeSetListener{ view, h, m ->
            Toast.makeText(activity?.applicationContext, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()
            var hour = h.toString()
            var min = m.toString()
            if(hour.length<2) hour = "0"+hour
            if(min.length<2) min = "0"+min
            aufwachenText.text = hour+" : "+min
        }
        timePicker = TimePickerDialog(this.activity, timePickerListener,hourInput,minInput,true)

        timePicker.show()
    }
    private fun openEinschlafenTimePicker(hourInput: Int, minInput: Int){
        val timePickerListener = TimePickerDialog.OnTimeSetListener{ view, h, m ->
            Toast.makeText(activity?.applicationContext, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()
            var hour = h.toString()
            var min = m.toString()
            if(hour.length<2) hour = "0"+hour
            if(min.length<2) min = "0"+min
            einschlafenText.text = hour+" : "+min
        }
        timePicker = TimePickerDialog(this.activity, timePickerListener,hourInput,minInput,true)

        timePicker.show()
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
         * @return A new instance of fragment RechnerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RechnerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
