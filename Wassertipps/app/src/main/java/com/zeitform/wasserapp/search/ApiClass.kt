package com.zeitform.wasserapp.search

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley
import com.zeitform.wasserapp.R


class ApiCall(ctx: Context) {

    private lateinit var mCtx: Context
    private var mRequestQueue: RequestQueue? = null

    val requestQueue: RequestQueue?
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext())
            }
            return mRequestQueue
        }

    init {
        mCtx = ctx
        mRequestQueue = requestQueue
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        requestQueue?.add(req)
    }

    companion object {

        private var mInstance: ApiCall? = null
        @Synchronized
        fun getInstance(context: Context): ApiCall {
            if (mInstance == null) {
                mInstance = ApiCall(context)
            }
            return mInstance as ApiCall
        }

        fun make(
            ctx: Context,
            query: String,
            listener: Response.Listener<String>,
            errorListener: Response.ErrorListener
        ) {
            val url = ctx.getString(R.string.search_query, query) // search request URL
            val stringRequest = StringRequest(
                Request.Method.GET, url,
                listener, errorListener
            )
            ApiCall.getInstance(ctx).addToRequestQueue(stringRequest)
        }
    }
}