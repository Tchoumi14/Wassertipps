package com.zeitform.wasserapp.navfragments

import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
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
    private lateinit var gewichtField: EditText
    private lateinit var alterField: EditText
    private lateinit var wasserProTagField: EditText
    private lateinit var erinnerungenField: EditText
    private lateinit var gewichtButtonMinus: Button
    private lateinit var gewichtButtonPlus: Button
    private lateinit var alterButtonMinus: Button
    private lateinit var alterButtonPlus: Button
    private lateinit var erinnerungButtonMinus: Button
    private lateinit var erinnerungButtonPlus: Button
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
        gewichtField = rootView.findViewById(R.id.gewicht_field)
        alterField = rootView.findViewById(R.id.alter_field)
        wasserProTagField = rootView.findViewById(R.id.wasserprotag_field)
        erinnerungenField = rootView.findViewById(R.id.erinnerungen_field)
        aufwachenText = rootView.findViewById(R.id.aufwachen_text)
        einschlafenText = rootView.findViewById(R.id.einschlafen_text)
        gewichtField.setText("70",TextView.BufferType.EDITABLE)
        alterField.setText("29",TextView.BufferType.EDITABLE)
        wasserProTagField.setText("1330",TextView.BufferType.EDITABLE)
        erinnerungenField.setText("7",TextView.BufferType.EDITABLE)

        gewichtButtonMinus = rootView.findViewById(R.id.gewicht_minus)
        gewichtButtonPlus = rootView.findViewById(R.id.gewicht_plus)
        gewichtButtonController()
        alterButtonMinus = rootView.findViewById(R.id.alter_minus)
        alterButtonPlus = rootView.findViewById(R.id.alter_plus)
        alterButtonController()
        erinnerungButtonMinus = rootView.findViewById(R.id.erinnerungen_minus)
        erinnerungButtonPlus = rootView.findViewById(R.id.erinnerungen_plus)
        erinnerungButtonController()

        aufwachenText.setOnClickListener {
            var timeText = aufwachenText.text
            var time = timeText.split(" : ")
            var hour = time[0].toInt()
            var min = time[1].toInt()
            openAufwachenTimePicker(hour, min)
        }
        einschlafenText.setOnClickListener {
            var timeText = einschlafenText.text
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

    /**
     * Gewicht button listener. Increment and decrement values
     *
     */
    private fun gewichtButtonController(){
        gewichtButtonMinus.setOnClickListener {
            var value = Integer.parseInt(gewichtField.text.toString().trim())
            gewichtField.setText((value-1).toString(),TextView.BufferType.EDITABLE)
        }

        gewichtButtonPlus.setOnClickListener {
            var value = Integer.parseInt(gewichtField.text.toString().trim())
            gewichtField.setText((value+1).toString(),TextView.BufferType.EDITABLE)
        }
    }
    /**
     * Alter button listener. Increment and decrement values
     *
     */
    private fun alterButtonController(){
        alterButtonMinus.setOnClickListener {
            var value = Integer.parseInt(alterField.text.toString().trim())
            alterField.setText((value-1).toString(),TextView.BufferType.EDITABLE)
        }

        alterButtonPlus.setOnClickListener {
            var value = Integer.parseInt(alterField.text.toString().trim())
            alterField.setText((value+1).toString(),TextView.BufferType.EDITABLE)
        }
    }
    /**
     * Erinnerungen button listener. Increment and decrement values
     *
     */
    private fun erinnerungButtonController(){
        erinnerungButtonMinus.setOnClickListener {
            var value = Integer.parseInt(erinnerungenField.text.toString().trim())
            erinnerungenField.setText((value-1).toString(),TextView.BufferType.EDITABLE)
        }

        erinnerungButtonPlus.setOnClickListener {
            var value = Integer.parseInt(erinnerungenField.text.toString().trim())
            erinnerungenField.setText((value+1).toString(),TextView.BufferType.EDITABLE)
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
