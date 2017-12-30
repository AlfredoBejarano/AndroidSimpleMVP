package com.alfredobejarano.sampleapp.presenter


import com.alfredobejarano.sampleapp.model.SampleApp
import com.alfredobejarano.sampleapp.repository.Routes
import com.alfredobejarano.simplemvp.presenter.SimplePresenter
import com.alfredobejarano.simplemvp.view.SimpleView

/**
 * Sample class of the implementation of the SimplePresenter.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
class SampleAppPresenter(view: SimpleView<SampleApp>, repository: Class<Routes>, baseURL: String) : SimplePresenter<SampleApp, Routes>(view, repository, baseURL) {
    init {
        performRequest("")
    }

    override fun performRequest(body: Any) {
        call = routes!!.getSampleApp()
        call.enqueue(this)
    }

    override fun handleUnauthorizedResponse() {
        // Do nothing.
    }

    /**
     * Example of data manipulation for the retrieved response.
     * This functions must be overriden if operations with the data needs to be made.
     * Operations on the classes that implement SimpleView is not recommended.
     */
    override fun onSuccessfulResponse(response: SampleApp?) {
        super.onSuccessfulResponse(buildText(response))
    }

    /**
     * Adds "Yout IP is: " text to an origin in a SampleApp object.
     */
    private fun buildText(response: SampleApp?): SampleApp? {
        response!!.origin = "Your IP is: " + response.origin
        return response
    }
}