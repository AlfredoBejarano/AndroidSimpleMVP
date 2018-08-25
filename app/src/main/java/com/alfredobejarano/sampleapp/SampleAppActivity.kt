package com.alfredobejarano.sampleapp

import android.view.View
import com.alfredobejarano.sampleapp.databinding.ActivitySampleBinding
import com.alfredobejarano.sampleapp.presenter.SampleAppPresenter
import com.alfredobejarano.sampleapp.repository.Routes
import com.alfredobejarano.simplemvp.view.SimpleActivity

/**
 * Sample class of the implementation of the SimpleView.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
class SampleActivity : SimpleActivity<ActivitySampleBinding>() {

    /**
     * This function performs a request to a SampleAppPresenter class to retrieve this device IP.
     */
    fun getDeviceIP(button: View) {
        getPresenter(SampleAppPresenter::class.java)?.getMyIP()
    }

    /**
     * Return the layout id for this activity.
     */
    override fun getLayoutId() = R.layout.activity_sample

    /**
     * This method should be used to add callbacks and listeners
     * to the view's widgets.
     */
    override fun setup() {
        // Attach a presenter to this view.
        attachPresenter(SampleAppPresenter(this, Routes::class.java))
        // Set the presenter to this activity binding.
        getBinding()?.presenter = getPresenter(SampleAppPresenter::class.java)
    }

    /**
     * Return this activity as the context for this view.
     */
    override fun asContext() = this
}
