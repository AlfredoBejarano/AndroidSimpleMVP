package com.alfredobejarano.sampleapp.presenter

import android.arch.lifecycle.MutableLiveData
import com.alfredobejarano.sampleapp.R
import com.alfredobejarano.sampleapp.repository.Routes
import com.alfredobejarano.simplemvp.presenter.SimpleNetworkPresenter
import com.alfredobejarano.simplemvp.view.SimpleView
import kotlin.concurrent.thread


/**
 * Sample class of the implementation of the SimplePresenter.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
class SampleAppPresenter(view: SimpleView?, apiDefinitions: Class<Routes>) : SimpleNetworkPresenter<Routes>(view, apiDefinitions) {
    val deviceIP = MutableLiveData<String>()

    /**
     * Defines the base URL for this presenter.
     * Usually the base URL will not change, so defining a
     * BuildConfig constant will save some hassle with this.
     */
    override fun getBaseURL() = "https://httpbin.org/"

    /**
     * Performs a request to retrieve your device IP.
     */
    fun getMyIP() = thread(start = true) {
        // Report that the presenter is doing work
        status.postValue(Status.STATUS_BUSY)
        // Build the request using the API definitions in Routes.kt
        val request = routes?.getDeviceIP()
        enqueueRequest(request) {
            if (it != null) {
                // Report the device ip.
                deviceIP.postValue(it.origin)
            } else {
                // Report that the device IP could not be read.
                deviceIP.postValue(view?.asContext()?.getString(R.string.cant_read_ip))
            }
            // Report that the presenter has finished doing work
            status.postValue(Status.STATUS_DONE)
        }
    }
}