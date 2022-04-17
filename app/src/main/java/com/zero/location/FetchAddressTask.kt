package com.zero.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.text.TextUtils
import android.util.Log
import java.io.IOException
import java.util.*


class FetchAddressTask internal constructor(ctx: Context, listener: OnTaskCompleted) :
    AsyncTask<Location?, Void?, String>() {
    @SuppressLint("StaticFieldLeak")
    private val mContext: Context = ctx
    private val mListener: OnTaskCompleted = listener

    internal interface OnTaskCompleted {
        fun onTaskCompleted(result: String?)
    }

    override fun doInBackground(vararg locations: Location?): String {
        val loc: Location? = locations[0]
        val geocoder = Geocoder(mContext, Locale.getDefault())
        var addresses: List<Address>?
        var resMsg = ""
        try {
            addresses = geocoder.getFromLocation(loc!!.latitude, loc.longitude, 1)
            resMsg = if (addresses == null || addresses.isEmpty()) {
                "Address not found"
            } else {
                val address: Address = addresses[0]
                val parts: ArrayList<String?> = ArrayList()
                for (i in 0..address.maxAddressLineIndex) {
                    parts.add(address.getAddressLine(i))
                }
                TextUtils.join("\n", parts)
            }
        } catch (ex: IOException) {
            Log.d("Exception", "FetchAddressTask.doInBackground: Unable to get location")
        }
        return resMsg
    }

    override fun onPostExecute(s: String) {
        mListener.onTaskCompleted(s)
        super.onPostExecute(s)
    }

}