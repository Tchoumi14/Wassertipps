package com.zeitform.wasserapp.navfragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Spanned
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AlertDialog
import org.json.JSONArray
import androidx.cardview.widget.CardView
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.text.HtmlCompat
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.zeitform.wasserapp.MainActivity
import com.zeitform.wasserapp.prefmanagers.DataManager
import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.billing.BillingConstants
import com.zeitform.wasserapp.billing.BillingManager
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import org.json.JSONObject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [HomeFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class HomeFragment : Fragment() {

    private lateinit var billingManager: BillingManager
    private var purchasedItems: MutableList<Purchase> = ArrayList()
    private lateinit var productToBuy: SkuDetails
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mainLayout: RelativeLayout
    private lateinit var nohardnessLayout: RelativeLayout
    private var jsonResult: JSONArray? = null
    private val haerteReduced: Double = 5.0
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dataManager: DataManager? = null
    private lateinit var haerteValue: TextView
    private lateinit var nitratValue: TextView
    private lateinit var haerteText: TextView
    private lateinit var nitratText: TextView
    private lateinit var haerteUnit: TextView
    private lateinit var nearText: TextView
    private lateinit var nearCard: androidx.cardview.widget.CardView
    private lateinit var dataTable: TableLayout
    private lateinit var infoIcon: ImageView
    private lateinit var tippsHaerteBtn: Button
    private lateinit var tippsNitratBtn: Button
    //private lateinit var consumeBtn: Button
    private var isReduced: Boolean = false
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
        }
        dataManager = DataManager(activity!!.applicationContext)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        mainLayout = rootView.findViewById(R.id.main_layout)
        nohardnessLayout = rootView.findViewById(R.id.nohardness_layout)
        nitratValue = rootView.findViewById(R.id.nitrat_value)
        haerteValue = rootView.findViewById(R.id.haerte_value)
        nitratValue = rootView.findViewById(R.id.nitrat_value)
        haerteValue.setOnClickListener { wasserPopUp() }
        infoIcon = rootView.findViewById(R.id.info_icon)
        haerteText = rootView.findViewById(R.id.haerte_text)
        nitratText = rootView.findViewById(R.id.nitrat_text)
        haerteUnit = rootView.findViewById(R.id.haerte_unit)
        nearCard = rootView.findViewById(R.id.near_card)
        nearText = rootView.findViewById(R.id.near_text)
        dataTable = rootView.findViewById(R.id.nodata_table)
        tippsHaerteBtn = rootView.findViewById(R.id.tipps_haerte)
        tippsHaerteBtn.setOnClickListener { listener!!.openTippsHaerte() /*if(checkAppPaymentStatus()) listener!!.openTippsHaerte() else purchasePopup() */}
        tippsNitratBtn = rootView.findViewById(R.id.tipps_nitrat)
        tippsNitratBtn.setOnClickListener { listener!!.openTippsNitrat() /* if(checkAppPaymentStatus()) listener!!.openTippsNitrat() else purchasePopup() */}
        tippsHaerteBtn.setBackgroundResource(R.drawable.button_background)
        tippsNitratBtn.setBackgroundResource(R.drawable.button_background)
        // CONSUME PURCHASE- FOR TEST
        /*
        consumeBtn = rootView.findViewById(R.id.consume_btn)
        consumeBtn.visibility = View.INVISIBLE
        consumeBtn.setOnClickListener {
            billingManager.consumeAsync(billingManager.mPurchases?.get(0)!!.purchaseToken)
        } */
        //TEST
        return rootView
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)

            observeInput(sharedViewModel)
        }
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        /*
        billingManager = BillingManager(activity!!)
        billingManager.setupBillingClient()
        listener!!.setBillingManager(billingManager)
        billingManager.refreshListListeners.add(object : BillingManager.InterfaceRefreshList {
            override fun refreshListRequest() {
                update()
            }
        })
*/
        listener!!.updateRechnerStatus(true) //enable rechner
    }

    private fun observeInput(sharedViewModel: SharedViewModel) {
        //sharedViewModel.serverData.removeObservers(this)
        sharedViewModel.serverData.observe(this, Observer {
            it?.let {
                //val jsonObject = it.optJSONObject(0)
                initElements(it)
                dataManager!!.savedData = it.toString()
                Log.d("SAVED DATA", dataManager!!.savedData.toString())
            }
        })
        //sharedViewModel.isReduced.removeObservers(this)
        sharedViewModel.isReduced.observe(this, Observer {
            it?.let {
                isReduced = it
            }
        })
        //sharedViewModel.completeData.removeObservers(this)
        sharedViewModel.completeData.observe(this, Observer {
            it?.let {
                jsonResult = it
            }
        })
        sharedViewModel.isInternetUnavailable.observe(this, Observer {
            it?.let {
                if(it){
                    val title = resources.getString(R.string.no_internet_title)

                    val content = resources.getString(R.string.no_internet_content)
                    createAlert(title, content)
                }
            }
        })
        sharedViewModel.isOutsideDeutschland.observe(this, Observer {
            it?.let {
                if(it){
                    val title = resources.getString(R.string.geolocation_title)
                    val content = HtmlCompat.fromHtml(resources.getString(R.string.geolocation_content), HtmlCompat.FROM_HTML_MODE_COMPACT)
                    createAlert(title, content)
                }
            }
        })
    }
    private fun initElements(jsonObj: JSONObject) {
        val min = jsonObj.optString("min").toDouble()
        isReduced = dataManager!!.isReduced
        val max = if(isReduced)  haerteReduced else jsonObj.optString("max").toDouble()
        val nmin = jsonObj.optString("nmin").toDouble()
        val nmax = jsonObj.optString("nmax").toDouble()
        val faktor = jsonObj.optString("faktor").toDouble()
        val unit = jsonObj.optString("unit")
        //val desc = jsonObj.optString("desc")
        val near = jsonObj.optString("near")

        val main = (activity as MainActivity)
        //Set hart 1, 2 or 3
        if(max < 8.4){
            main.hart = 1
        } else if(max > 8.4 && max <= 14){
            main.hart = 2
        } else if(max > 14){
            main.hart = 3
        }
        //Set nitrat 1, 2, or 3
        if(nmax <= 10){
            main.nitrat = 1
        } else if(nmax > 10 && max <= 25){
            main.nitrat = 2
        } else if(max > 25){
            main.nitrat = 3
        }
        if(max == 0.0 && nmax == 0.0){
            //show 5 nearest places to choose from
            Log.d("Tut uns leid","Tut uns leid")
            createTable()
            mainLayout.visibility = View.INVISIBLE
            nohardnessLayout.visibility = View.VISIBLE
        } else {
            mainLayout.visibility = View.VISIBLE
            nohardnessLayout.visibility = View.INVISIBLE
        }
        //set elements
        //haerte value
        if(min >0.0){
            haerteValue.text = min.toString()+"-"+max.toString()
        } else {
            haerteValue.text = max.toString()
        }
        //nitrat value
        if(nmin >0.0){
            nitratValue.text = nmin.toString()+"-"+nmax.toString()
        } else {
            nitratValue.text = nmax.toString()
        }
        haerteUnit.text = unit

        if(min==0.0){
            if(max <= (8.4 * faktor)){
                //#4dbd40
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte1
                )
                haerteText.text = getString(R.string.haerteText1)
            } else if((max > (8.4 * faktor)) && (max <= (14 * faktor))){
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte2
                )
                haerteText.text = getString(R.string.haerteText2)
                infoIcon.visibility = View.VISIBLE
                //clickEnabled
            } else if(max > (14 * faktor)){
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte3
                )
                haerteText.text = getString(R.string.haerteText3)
                infoIcon.visibility = View.VISIBLE
                //clickEnabled
            }
        } else if(min != 0.0){
            if(max <= (8.4 * faktor)){
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte1
                )
                haerteText.text = getString(R.string.haerteText4)
            } else if((max > (8.4 * faktor)) && (max <= (14 * faktor))){
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte2
                )
                haerteText.text = getString(R.string.haerteText5)
                //clickEnabled
                infoIcon.visibility = View.VISIBLE
            } else if(max > (14 * faktor)){
                haerteValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                    R.color.haerte3
                )
                haerteText.text = getString(R.string.haerteText6)
                //clickEnabled
                infoIcon.visibility = View.VISIBLE
            }
        }
        //Nitrat square
        if(nmax <= 10){
            nitratValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                R.color.nitrat1
            )
        } else if(nmax > 10 && nmax < 25){
            nitratValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                R.color.nitrat2
            )
        } else if (nmax > 25){
            nitratValue.background = ContextCompat.getDrawable(activity!!.applicationContext,
                R.color.nitrat3
            )
        }
        //nitrat text
        if(nmin == 0.0 && nmax <=10){
            nitratText.text = getString(R.string.nitratText1)
        } else if(nmin == 0.0 && nmax > 10 && nmax <=25){
            nitratText.text = getString(R.string.nitratText2)
        } else if(nmin == 0.0 && nmax <=25){
            nitratText.text = getString(R.string.nitratText3)
        } else if(nmin > 0.0 && nmax <=25){
            nitratText.text = getString(R.string.nitratText4)
        } else if(nmin > 0.0 && nmin <= 25 && nmax > 25){
            nitratText.text = getString(R.string.nitratText5)
        } else if(nmin > 25 && nmax > 25){
            nitratText.text = getString(R.string.nitratText6)
        }
        if(near=="1"){
            nearCard.visibility = View.VISIBLE
        } else {
            nearCard.visibility = View.INVISIBLE
        }

        //wasserenthärtungsanlage
        if(isReduced) infoIcon.visibility = View.VISIBLE
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

    fun updateView(data: String){
        Log.d("Test", data)
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
        fun openTippsHaerte()
        fun openTippsNitrat()
        fun setBillingManager(billingManager: BillingManager)
        fun updateRechnerStatus(isVisible: Boolean)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
    private fun updatePurchasedItems(){
        var purchases = billingManager.mPurchases
        if (purchases != null) {
            //if array not empty, check if purchased item matches.
            for(purchase in purchases){
                if(purchase.sku == BillingConstants.SKU_PRO){
                    //items unlocked
                    tippsHaerteBtn.setBackgroundResource(R.drawable.button_background)
                    tippsNitratBtn.setBackgroundResource(R.drawable.button_background)
                    println("unlocked")
                }
                else {
                    //items locked
                    tippsHaerteBtn.setBackgroundResource(R.drawable.button_background_locked)
                    tippsNitratBtn.setBackgroundResource(R.drawable.button_background_locked)
                    println("Locked 2")
                }
            }
        } else {
            tippsHaerteBtn.setBackgroundResource(R.drawable.button_background_locked)
            tippsNitratBtn.setBackgroundResource(R.drawable.button_background_locked)
            println("Locked 1")
        }

    }
    private fun update(){
        if(billingManager.isProPurchased){
            tippsHaerteBtn.setBackgroundResource(R.drawable.button_background)
            tippsNitratBtn.setBackgroundResource(R.drawable.button_background)
            listener!!.updateRechnerStatus(true)
            //consumeBtn.visibility = View.VISIBLE
            println("unlocked")
        } else {
            tippsHaerteBtn.setBackgroundResource(R.drawable.button_background_locked)
            tippsNitratBtn.setBackgroundResource(R.drawable.button_background_locked)
            listener!!.updateRechnerStatus(false)
            //consumeBtn.visibility = View.INVISIBLE
            println("Locked 1")
        }
    }
    /**
     * If the app is free, opens purchase prompt
     * else opens nitrat/härte page
     */
    private fun checkAppPaymentStatus(): Boolean {
        //If the array is empty, assumed that no purchase has been made
        var result = false
        if(billingManager.mPurchases?.size==0){
            result = false
        }
        //if array not empty, check if purchased item matches.
        for(purchase in billingManager.mPurchases!!){
            result = purchase.sku == BillingConstants.SKU_PRO
        }
        return result
    }
    private fun purchasePopup(){
        for(product in billingManager.productList){
            if(product.sku == BillingConstants.SKU_PRO){
                productToBuy = product
            }
        }
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                builder.setTitle(R.string.purchase_title)
                builder.setMessage(R.string.purchase_content)

                setPositiveButton(
                    context.getString(R.string.purchase_button,productToBuy.price)
                ) { _, _ ->
                    billingManager.initiatePurchaseFlow(productToBuy)
                }
                setNegativeButton(
                    R.string.dialog_button_no
                ) { _, _ ->
                    // User cancelled the dialog
                }
            }
            // Create the AlertDialog

            builder.create()
        }
        alertDialog?.show()
        var positive = alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        if(positive != null) {
            positive.background = ContextCompat.getDrawable(context!!.applicationContext, R.drawable.purchase_button_selector)
            positive.setTextColor(Color.WHITE)
            positive.isAllCaps = false
            var scale = resources.displayMetrics.density
            var padding16dp = (16 * scale + 0.5f).toInt()
            positive.setPadding(padding16dp,0,padding16dp,0)
        }
        var negative = alertDialog?.getButton(DialogInterface.BUTTON_NEGATIVE)
        if(negative!=null){
            negative.isAllCaps = false
        }
    }
    /**
     * Wasserhärtungsanlage popup.
     */
    private fun wasserPopUp(){
        if(infoIcon.visibility == View.VISIBLE){
            Log.d("Clicked!", "OK!")
            val alertDialog: AlertDialog? = activity?.let {
                val builder = AlertDialog.Builder(it)
                builder.apply {
                    if(isReduced){
                        builder.setTitle(R.string.dialog_title_2)
                        builder.setMessage(R.string.dialog_message_2)
                        setPositiveButton(
                            R.string.dialog_button_yes
                        ) { _, _ ->
                            // User clicked OK button
                            isReduced = false
                            dataManager!!.isReduced = false
                            //var data = jsonResult!!.optJSONObject(0)
                            var data = JSONObject(dataManager!!.savedData)
                            initElements(data)
                        }
                    } else {
                        builder.setTitle(R.string.dialog_title_1)
                        builder.setMessage(R.string.dialog_message_1)
                        setPositiveButton(
                            R.string.dialog_button_yes
                        ) { _, _ ->
                            // User clicked OK button
                            isReduced = true
                            dataManager!!.isReduced = true
                            //var data = jsonResult!!.optJSONObject(0)
                            var data = JSONObject(dataManager!!.savedData)
                            initElements(data)

                        }
                    }

                    setNegativeButton(
                        R.string.dialog_button_no
                    ) { _, _ ->
                        // User cancelled the dialog
                    }
                }
                // Set other dialog properties

                // Create the AlertDialog
                builder.create()
            }
            alertDialog?.show()

        }
    }
    /**
     * Updates serverData and saves it to sharedPreferences for when the app restarts
     */
    private fun updateServerData(jsonObj: JSONObject){
        activity?.let {
            val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)
            sharedViewModel.serverData.postValue(jsonObj) //saves only the first entry/ aktuelle ausgewählte daten
        }
    }
    private fun createTable(){
        val typedValue = TypedValue()
        val theme = context!!.theme
        theme.resolveAttribute(R.attr.colorAppTextPrimary, typedValue, true)
        var color = typedValue.data
        var jsonObj:JSONObject
        var row: TableRow
        var ort: TextView
        var entf: TextView
        var haerte: TextView
        var nitrat: TextView
        val lp1 = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.4f)
        val lp2 = TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.2f)
        for (i in 1 until 6){
            jsonObj = jsonResult!!.optJSONObject(i)
            row = TableRow(activity)
            row.setOnClickListener {
                updateServerData(jsonResult!!.optJSONObject(i))
                //initElements(jsonResult!!.optJSONObject(i))
                Log.d("Array",jsonResult!!.optJSONObject(i).toString())
            }
            row.layoutParams = TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,TableRow.LayoutParams.MATCH_PARENT)
            row.setPadding(5,5,5,5)
            ort = TextView(activity)
            ort.setTextColor(color)
            ort.text = jsonObj.optString("city")
            ort.layoutParams = lp1
            ort.gravity = Gravity.LEFT
            entf = TextView(activity)
            entf.setTextColor(color)
            entf.text = jsonObj.optString("distance")
            entf.layoutParams = lp2
            entf.gravity = Gravity.CENTER
            haerte = TextView(activity)
            haerte.setTextColor(color)
            haerte.text = jsonObj.optString("max")
            haerte.layoutParams = lp2
            haerte.gravity = Gravity.CENTER
            nitrat = TextView(activity)
            nitrat.setTextColor(color)
            nitrat.text = jsonObj.optString("nmax")
            nitrat.layoutParams = lp2
            nitrat.gravity = Gravity.END
            row.addView(ort)
            row.addView(entf)
            row.addView(haerte)
            row.addView(nitrat)
            dataTable.addView(row, TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.MATCH_PARENT))
        }
        dataTable.invalidate()
        dataTable.refreshDrawableState()
    }
    private fun createAlert(title: String, content: String){
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                builder.setTitle(title)
                builder.setMessage(content)

                setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    //close
                }
            }
            // Create the AlertDialog

            builder.create()
        }
        alertDialog?.show()
        var positive = alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        if(positive != null) {
            positive.isAllCaps = false
        }
    }
    private fun createAlert(title: String, content: Spanned){
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                builder.setTitle(title)
                builder.setMessage(content)

                setPositiveButton(
                    "Ok"
                ) { _, _ ->
                    //close
                }
            }
            // Create the AlertDialog

            builder.create()
        }
        alertDialog?.show()
        var positive = alertDialog?.getButton(DialogInterface.BUTTON_POSITIVE)
        if(positive != null) {
            positive.isAllCaps = false
        }
    }
}
