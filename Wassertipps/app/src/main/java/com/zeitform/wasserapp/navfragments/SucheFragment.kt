package com.zeitform.wasserapp.navfragments

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AutoCompleteTextView
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.search.AutoSuggestAdapter
import com.android.volley.VolleyError
import org.json.JSONObject
import org.json.JSONArray
import com.zeitform.wasserapp.search.ApiCall
import com.android.volley.Response
import com.zeitform.wasserapp.MainActivity
import com.zeitform.wasserapp.prefmanagers.DataManager
import com.zeitform.wasserapp.search.FavlistAdapter
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import org.json.JSONException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [SucheFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [SucheFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class SucheFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var dataManager: DataManager? = null
    private lateinit var fullResponseData : ArrayList<JSONObject>
    private val TRIGGER_AUTO_COMPLETE = 100
    private val AUTO_COMPLETE_DELAY: Long = 300
    private var handler: Handler? = null
    private lateinit var autoCompleteSearch: AutoCompleteTextView
    private lateinit var autoSuggestAdapter: AutoSuggestAdapter
    private lateinit var favArrayList: ArrayList<JSONObject>
    private lateinit var favList: ListView
    private var listener: OnFragmentInteractionListener? = null
    private var sharedViewModel: SharedViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("LongLogTag")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_suche, container, false)
        favArrayList = ArrayList()
        // TO DO - Load saved favArrayList at startup.
        autoCompleteSearch = rootView.findViewById(R.id.autoCompleteSearch)
        autoSuggestAdapter = AutoSuggestAdapter(context,  android.R.layout.simple_dropdown_item_1line)
        autoCompleteSearch.threshold = 2
        autoCompleteSearch.setAdapter(autoSuggestAdapter)

        /**
         * Dropdown list click listener
         */
        autoCompleteSearch.setOnItemClickListener { parent, view, position, id ->
            val item = fullResponseData[position] // show this in the home page and add it to favorite list
            favArrayList.add(fullResponseData[position])
            sharedViewModel?.favoriteList?.postValue(favArrayList) //update sharedViewModel
            autoCompleteSearch.text.clear()
            listener?.loadSearchedItem(item.getDouble("lon"), item.getDouble("lat")) //call function in MainActivity to fetch data
            //TO DO - , Switch to Home page

        }
        autoCompleteSearch.addTextChangedListener(inputTextWatcher) //Input listener (search input field)
        handler = Handler(Handler.Callback { msg ->
            if (msg.what == TRIGGER_AUTO_COMPLETE) {
                if (!TextUtils.isEmpty(autoCompleteSearch.getText())) {
                    print("Api call with query "+autoCompleteSearch.getText().toString())
                    makeApiCall(autoCompleteSearch.getText().toString())
                }
            }
            false
        })

        //favorite List
        favList = rootView.findViewById(R.id.fav_list)
        val favlistAdapter = FavlistAdapter(activity!!.applicationContext, this, favArrayList)
        favList.adapter = favlistAdapter

        sharedViewModel?.favoriteList?.observe(this, Observer {
            it?.let {
                favArrayList = it
                //TO DO -save the list to DataManager (loaded to variable 'favArrayList' on app startup)
                updateFavoritelist()
            }
        })

        return rootView
    }
    private fun updateFavoritelist() {
        val adapter = favList.adapter as FavlistAdapter
        adapter.notifyDataSetChanged()
    }
    /*Observes text input in auto complete input field*/
    private var inputTextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            handler?.removeMessages(TRIGGER_AUTO_COMPLETE)
            handler?.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE, AUTO_COMPLETE_DELAY)
        }

        override fun afterTextChanged(s: Editable?) {

        }

    }

    /**
     * Send search request to the URL and add response to the dropdown list
     */
    private fun makeApiCall(text: String) {
        context?.let {
            ApiCall.make(it, text, Response.Listener<String> { response ->
                //parsing logic, please change it as per your requirement
                fullResponseData = ArrayList<JSONObject>()
                val stringList = ArrayList<String>()
                try {
                    val array = JSONArray(response)
                    for (i in 0 until array.length()) {
                        val row = array.getJSONObject(i)
                        fullResponseData.add(row)
                        val listItem = row.getString("ort") +", "+ row.getString("plz")
                        stringList.add(listItem)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList)
                autoSuggestAdapter.notifyDataSetChanged()
            }, Response.ErrorListener { error -> print(error) })
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
        fun loadSearchedItem(longitude: Double, latitude: Double)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SucheFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SucheFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
