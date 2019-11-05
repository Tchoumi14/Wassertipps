package com.zeitform.wasserapp.internalfragments

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.core.text.HtmlCompat
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

import com.zeitform.wasserapp.R
import com.zeitform.wasserapp.viewmodel.SharedViewModel
import org.w3c.dom.Text
import android.content.Intent
import android.text.method.LinkMovementMethod
import android.webkit.WebView


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TippsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TippsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TippsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var text: TextView
    private lateinit var webView: WebView
    private lateinit var urlButton: Button
    private lateinit var url: String
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
        var rootView = inflater.inflate(R.layout.fragment_tipps, container, false)
        text = rootView.findViewById(R.id.tipps_text)
        webView = rootView.findViewById(R.id.tipps_web)
        var webSettings = webView.settings
        webSettings.defaultFontSize = resources.getDimension(R.dimen.tipps_content_font_size).toInt()
        text.movementMethod = LinkMovementMethod.getInstance()
        urlButton = rootView.findViewById(R.id.url_Button)
        urlButton.setOnClickListener {
            val uri = Uri.parse(url) // missing 'http://' will cause crashed
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent) }
        return rootView
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        activity?.let {
            val sharedViewModel = ViewModelProviders.of(it).get(SharedViewModel::class.java)

            observeInput(sharedViewModel)
        }
    }
    private fun observeInput(sharedViewModel: SharedViewModel) {
        sharedViewModel.tippsContent.observe(this, Observer {
            it?.let {
                //text.text = it
                webView.loadData(it,"text/html","UTF-8")
            }
        })
        sharedViewModel.tippsTitle.observe(this, Observer {
            it?.let {
                //Log.d("Image DATA", it.toString())
            }
        })
        sharedViewModel.url.observe(this, Observer {
            it?.let {
                //Log.d("URL", it.toString())
                url = it.toString()
            }
        })
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
         * @return A new instance of fragment TippsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TippsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
