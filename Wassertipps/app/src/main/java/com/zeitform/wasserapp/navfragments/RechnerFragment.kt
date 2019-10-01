package com.zeitform.wasserapp.navfragments

import android.app.Dialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.SwitchCompat
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.gson.Gson
import com.zeitform.wasserapp.InputFilterMinMax
import com.zeitform.wasserapp.MainActivity
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.SelectorType
import com.zeitform.wasserapp.notif.AlarmScheduler
import com.zeitform.wasserapp.notif.NotificationHelper
import com.zeitform.wasserapp.notif.AlarmData
import com.zeitform.wasserapp.notif.AlarmDataManagerHelper
import com.zeitform.wasserapp.prefmanagers.AlarmDataManager
import com.zeitform.wasserapp.prefmanagers.RechnerDataManager






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
    private var rechnerDataManager: RechnerDataManager? = null
    private lateinit var aufwachenText: TextView
    private lateinit var einschlafenText: TextView
    private var aufwachenTimeInt: Int = 0
    private var einschlafenTimeInt: Int = 0
    private lateinit var timePicker: TimePickerDialog
    private lateinit var gewichtField: TextView
    private lateinit var alterField: TextView
    private lateinit var wasserProTagField: EditText
    private lateinit var wasserProTagText: TextView
    private lateinit var erinnerungenField: TextView
    private lateinit var sportSwitch: SwitchCompat
    private lateinit var stillendeFrauenSwitch: SwitchCompat
    private lateinit var mitteilungenSwitch: SwitchCompat
    private lateinit var mitteilungActiveTextBox: LinearLayout
    private lateinit var mitteilungActiveText: TextView
    private lateinit var consumptionTimes: ArrayList<Int>
    private lateinit var alarmTimes: ArrayList<AlarmData>
    private var alarmDataManager: AlarmDataManager? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        //DataManager init
        alarmDataManager = AlarmDataManager(activity!!.applicationContext)
        rechnerDataManager = RechnerDataManager(activity!!.applicationContext)
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
        wasserProTagText = rootView.findViewById(R.id.wasserprotag_text)
        erinnerungenField = rootView.findViewById(R.id.erinnerungen_field)
        aufwachenText = rootView.findViewById(R.id.aufwachen_text)
        einschlafenText = rootView.findViewById(R.id.einschlafen_text)
        gewichtField.setText("70",TextView.BufferType.EDITABLE)
        alterField.setText("29",TextView.BufferType.EDITABLE)
        wasserProTagField.setText("0",TextView.BufferType.EDITABLE)
        wasserProTagField.filters = arrayOf<InputFilter>(InputFilterMinMax(800, 10000))
        //erinnerungenField.setText("0",TextView.BufferType.EDITABLE)

        sportSwitch = rootView.findViewById(R.id.sport_switch)
        stillendeFrauenSwitch = rootView.findViewById(R.id.stillendefrauen_switch)
        mitteilungenSwitch = rootView.findViewById(R.id.mitteilungen_switch)
        mitteilungActiveText = rootView.findViewById(R.id.mitteilung_active_text_box)
        mitteilungActiveText = rootView.findViewById(R.id.mitteilung_active_text)
        //set field values
        initFields()

        //TextView number, onClick opens Dialog with NumberPicker
        gewichtField.setOnClickListener { openDialog(SelectorType.GEWICHT,gewichtField.text.toString()) }
        alterField.setOnClickListener { openDialog(SelectorType.ALTER,alterField.text.toString()) }
        erinnerungenField.setOnClickListener { openDialog(SelectorType.ERINNERUNGEN,erinnerungenField.text.toString()) }

        mitteilungenSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            rechnerDataManager!!.mitteilungenSwitch = isChecked //save to data manager
            //Set alarms
            if(isChecked)
                setAlarms()
            else
                clearAlarms()
                disableMitteilungActiveText()
        }

        //change Listeners
        gewichtField.addTextChangedListener(myTextWatcher)
        alterField.addTextChangedListener(myTextWatcher)
        wasserProTagField.addTextChangedListener(wasserProTagWatcher)
        sportSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            rechnerDataManager!!.sport = isChecked //save to data manager
            calculateWasser()
        }
        stillendeFrauenSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            rechnerDataManager!!.stillendefrauen = isChecked //save to data manager
            calculateWasser()
        }
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
    private fun openDialog(selectorType: SelectorType, currentValue: String){
        val dialog = AlertDialog.Builder(activity!!)
        val alert = dialog.create()
        val view = layoutInflater.inflate(R.layout.number_picker_dialog, null)

        alert.setView(view)
        val buttonSet = view.findViewById(R.id.button1) as Button
        val buttonCancel = view.findViewById(R.id.button2) as Button
        val numPicker = view.findViewById(R.id.numberPicker1) as NumberPicker

        when(selectorType){
            SelectorType.GEWICHT ->{
                alert.setTitle("Gewicht")
                numPicker.minValue=1
                numPicker.maxValue=100
                numPicker.value = currentValue.toInt()
                buttonSet.setOnClickListener {
                    gewichtField.text = numPicker.value.toString()
                    rechnerDataManager!!.gewicht = numPicker.value.toString()
                    alert.dismiss()
                }
                buttonCancel.setOnClickListener { alert.dismiss() }
            }
            SelectorType.ALTER ->{
                alert.setTitle("Alter")
                numPicker.minValue=1
                numPicker.maxValue=100
                numPicker.value = currentValue.toInt()
                buttonSet.setOnClickListener {
                    alterField.text = numPicker.value.toString()
                    rechnerDataManager!!.alter = numPicker.value.toString()
                    alert.dismiss()
                }
                buttonCancel.setOnClickListener { alert.dismiss() }
            }
            SelectorType.ERINNERUNGEN ->{
                alert.setTitle("Erinnerungen")
                numPicker.minValue=consumptionTimes[0]
                numPicker.maxValue=consumptionTimes[consumptionTimes.size-1]
                numPicker.value = currentValue.toInt()
                buttonSet.setOnClickListener {
                    erinnerungenField.text = numPicker.value.toString()
                    rechnerDataManager!!.erinnerungen = numPicker.value
                    Log.d("Selected value", numPicker.value.toString())
                    updateAlarms()
                    alert.dismiss()
                }
                buttonCancel.setOnClickListener { alert.dismiss() }
            }
        }
        alert.show()
    }
    /**
     * Init fields and switches in Wasserbedarf rechner from RechnerDataManager and call calculateWasser()
     */
    private fun initFields(){
        gewichtField.text = rechnerDataManager!!.gewicht
        alterField.text = rechnerDataManager!!.alter
        sportSwitch.isChecked = rechnerDataManager!!.sport
        stillendeFrauenSwitch.isChecked = rechnerDataManager!!.stillendefrauen
        //init times
        aufwachenText.text = rechnerDataManager!!.aufwachen
        aufwachenTimeInt = timetoNumber(rechnerDataManager!!.aufwachen) // convert text time to number
        einschlafenText.text = rechnerDataManager!!.einschlafen
        val sleepTime = timetoNumber(rechnerDataManager!!.einschlafen)
        einschlafenTimeInt = if(sleepTime == 0) 1440 else sleepTime// convert text time to number

        mitteilungenSwitch.isChecked = rechnerDataManager!!.mitteilungenSwitch

        val wasserProTagValue = rechnerDataManager!!.wasserProTag.trim()
        wasserProTagField.setText(wasserProTagValue,TextView.BufferType.EDITABLE)
        erinnerungenField.text = rechnerDataManager!!.erinnerungen.toString()


        if(wasserProTagValue=="-1" ){
            calculateWasser()
        } else {
            calculateErinnerung(wasserProTagValue.toInt())
        }

    }

    /**
     * Convert time string of format 'hh : mm' to int
     * @param timeAsString - time as a string in the format 'hh : mm'
     * @return timeInNumber - (hh * 60) + mm
     */
    private fun timetoNumber(timeAsString: String): Int {
        var timeInNumber = 0
        var time = timeAsString.split(" : ")
        var hour = time[0].toInt()
        var min = time[1].toInt()
        timeInNumber = (hour * 60) + min
        return timeInNumber
    }
    private fun openAufwachenTimePicker(hourInput: Int, minInput: Int){
        val timePickerListener = TimePickerDialog.OnTimeSetListener{ view, h, m ->
            Toast.makeText(activity?.applicationContext, h.toString() + " : " + m +" : " , Toast.LENGTH_LONG).show()
            var hour = h.toString()
            var min = m.toString()
            if(hour.length<2) hour = "0"+hour
            if(min.length<2) min = "0"+min
            var time = hour+" : "+min
            rechnerDataManager!!.aufwachen = time
            aufwachenTimeInt = timetoNumber(time) // convert text time to number
            if(aufwachenText.text!= time){
                aufwachenText.text = time
                updateAlarms()
                Log.d("Aufwachen", "updated")
            }
            Log.d("wake up time as number", aufwachenTimeInt.toString())
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
            val time = hour+" : "+min

            rechnerDataManager!!.einschlafen = time
            val sleepTime = timetoNumber(time)
            einschlafenTimeInt = if(sleepTime == 0) 1440 else sleepTime// convert text time to number
            if(einschlafenText.text != time){
                einschlafenText.text = time
                updateAlarms()
                Log.d("Einschlafen", "updated")
            }
            Log.d("Sleep time as number", einschlafenTimeInt.toString())
        }
        timePicker = TimePickerDialog(this.activity, timePickerListener,hourInput,minInput,true)

        timePicker.show()
    }

    /**
     * Text watcher for gewicht and alter, change in the values calls calculateWasser()
     */
    private var myTextWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            calculateWasser()
        }

    }
    /**
     * Text watcher for wasserProTag, change in the values calls calculateErinnerung()
     */
    private var wasserProTagWatcher = object: TextWatcher{
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable?) {
            if(s.toString().trim()!=""){
                calculateErinnerung(Integer.parseInt(s.toString().trim()))
            }
        }

    }

    /**
     * Calculate WasserProTag and call calculateErinnerungen
     */
    private fun calculateWasser(){
        sportSwitch.isEnabled = true
        stillendeFrauenSwitch.isEnabled = true
        val gewicht = Integer.parseInt(gewichtField.text.toString().trim())
        val alter = Integer.parseInt(alterField.text.toString().trim())
        var waterml = 0.0
        if(alter in 1..3){
            waterml = 60.0 * gewicht
        } else if(alter in 4..6){
            waterml = 44.0 * gewicht
        } else if(alter in 7..9){
            waterml = 32.0 * gewicht
        } else if(alter in 10..12){
            waterml = 27.0 * gewicht
        } else if(alter in 13..18){
            waterml = 22.0 * gewicht
        } else if(alter in 19..50){
            waterml = 19.0 * gewicht
        } else if(alter in 51..64){
            waterml = 16.5 * gewicht
        } else if(alter >=65){
            waterml = 17.5 * gewicht
        }

        if(alter < 13){
            stillendeFrauenSwitch.isChecked = false
            stillendeFrauenSwitch.isEnabled = false
        }
        if(alter < 19){
            sportSwitch.isChecked = false
            sportSwitch.isEnabled = false
        }
        //Zusätzlich Switch Stillende Frauen: 25 ml/kg (nur aktiv wenn Alter mindestens 13 Jahre)
        if(stillendeFrauenSwitch.isChecked){
            if(alter >=13){
                waterml = 25.0 * gewicht
            }
        }
        //Zusätzlich Switch Sport: +500ml (kann bleiben) nur aktiv wenn Alter mindestens 19 Jahre
        if(sportSwitch.isChecked){
            if(alter >= 19){
                waterml+= 500
            }
        }
        wasserProTagField.setText((waterml.toInt()).toString(), TextView.BufferType.EDITABLE)
        calculateErinnerung(waterml.toInt())
    }

    /**
     * Calculate number of reminders such that each reminder is 200-250ml
     * Use saved data if it exists in the array, else use middle value of the array
     * @param waterml - Wasser Pro Tag
     */
    private fun calculateErinnerung(waterml:Int){
        wasserProTagText.text = getString(R.string.wasser_pro_tag_text, waterml.toString())
        rechnerDataManager!!.wasserProTag = waterml.toString()
        //maximal 20 Erinnerungen pro Tag. Jede Erinnerung hat ein Maximum von 200-250ml.
        Log.d("Waterml at er", waterml.toString())
        consumptionTimes = ArrayList()
        for(i in 1 until 20){
            if((waterml/i >=200) && (waterml/i<=250)){
                consumptionTimes.add(i)
            }
        }
        var value = "0"
        if(consumptionTimes.size>2){
            val index = Math.round((consumptionTimes.size/2).toDouble())
            value = consumptionTimes.get(index.toInt()).toString()
        } else if(consumptionTimes.size in 1..2) {
            value = consumptionTimes.get(consumptionTimes.size-1).toString()
        } else {
            consumptionTimes.add(0)
            value = consumptionTimes.get(0).toString()
            Log.d("Cant be less", "that 200")
        }
        var savedValue = rechnerDataManager!!.erinnerungen
        if(savedValue!= -1 && consumptionTimes.indexOf(savedValue)!=-1){
                erinnerungenField.text = savedValue.toString()
        } else {
            erinnerungenField.text = value
            rechnerDataManager!!.erinnerungen = value.toInt()
        }
        Log.d("ConsumptionTimes", consumptionTimes.toString())
        updateAlarms()
    }
    /**
     * Updates alarms if switch is already turned on
     */
    private fun updateAlarms(){
        if(rechnerDataManager!!.mitteilungenSwitch){
            clearAlarms()
            setAlarms()
        }
    }
    /**
     * Create alarms at regular intervals between aufwachen and einschlafen time.
     */
    private fun setAlarms(){

        NotificationHelper.createNotificationChannel(activity!!.applicationContext,
            NotificationManagerCompat.IMPORTANCE_DEFAULT, false,
            getString(R.string.app_name), "App notification channel.")

        val duration = einschlafenTimeInt - aufwachenTimeInt
        val times = Integer.parseInt(erinnerungenField.text.toString().trim())
        val interval = Math.round((duration/times).toDouble()).toInt()
        var waterMl = Integer.parseInt(wasserProTagField.text.toString().trim())/times
        Log.d("Time interval", interval.toString()+"mins")
        alarmTimes = ArrayList()
        for(i in 1 until times+1){
            var t = aufwachenTimeInt + (i * interval)
            var mins = t%60
            var hour = t/60
            var id = Integer.parseInt(hour.toString()+""+mins.toString())
            alarmTimes.add(AlarmData(id, hour, mins, waterMl))
            Log.d("Times", times.toString())
        }
        AlarmDataManagerHelper.saveToAlarmDataManager(activity!!.applicationContext, alarmTimes)
        for(alarmTime in alarmTimes){
            AlarmScheduler.scheduleAlarmsForReminder(activity!!.applicationContext, alarmTime)
        }
        setMitteilungActiveText(waterMl,interval)
    }
    /**
     * Clear running alarms
     */
    private fun clearAlarms(){
        AlarmScheduler.removeAlarmsForReminder(activity!!.applicationContext)
    }

    private fun setMitteilungActiveText(waterMl: Int, interval: Int){
        mitteilungActiveText.text = resources.getString(R.string.mitteilungen_active_text, waterMl, interval)
        mitteilungActiveTextBox.visibility = View.VISIBLE
    }

    private fun disableMitteilungActiveText(){
        mitteilungActiveTextBox.visibility = View.INVISIBLE
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
