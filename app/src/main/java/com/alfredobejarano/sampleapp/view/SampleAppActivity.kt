package com.alfredobejarano.sampleapp.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View.GONE
import android.view.View.VISIBLE
import com.alfredobejarano.sampleapp.R
import com.alfredobejarano.sampleapp.model.SampleApp
import com.alfredobejarano.sampleapp.presenter.SampleAppPresenter
import com.alfredobejarano.sampleapp.repository.Routes
import com.alfredobejarano.simplemvp.view.SimpleView
import kotlinx.android.synthetic.main.activity_sample_app.*

/**
 * Sample class of the implementation of the SimpleView.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
class SampleAppActivity : AppCompatActivity(), SimpleView<SampleApp> {
    override fun attachPresenter() {
        SampleAppPresenter(this, Routes::class.java, "https://httpbin.org/")
    }

    override fun setup(response: SampleApp?) {
        response_text_view.text = response?.origin
    }

    override fun displayErrorMessage(message: String) {
        response_text_view.text = message
    }

    override fun displayErrorMessage(message: Int) {
        response_text_view.setText(message)
    }

    override fun displayLoadingView(isVisible: Boolean) {
        loading.visibility = if (isVisible) VISIBLE else GONE
        response_text_view.visibility = if (isVisible) GONE else VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample_app)
        attachPresenter()
    }
}
