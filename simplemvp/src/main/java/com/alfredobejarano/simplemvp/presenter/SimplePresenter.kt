package com.alfredobejarano.simplemvp.presenter

import android.support.annotation.StringRes
import com.alfredobejarano.simplemvp.BuildConfig
import com.alfredobejarano.simplemvp.R
import com.alfredobejarano.simplemvp.view.SimpleView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

/**
 * This class handles all the API requests and data processing.
 *
 * @author @jacbe
 * @version 1.0
 * @since 30/12/2017
 */
abstract class SimplePresenter<RE, RP>(private val view: SimpleView<RE>, private val repository: Class<RP>, baseURL: String) : Callback<RE> {
    private val baseUrl = baseURL
    var routes: RP? = null
    lateinit var call: Call<RE>

    init {
        view.displayLoadingView(true)
        initClient()
    }

    /**
     * Initializes the client for this presenter to use.
     */
    private fun initClient() {
        val restClient: Retrofit = buildRetrofitClient()
        routes = restClient.create(repository)
    }

    /**
     * Builds a Retrofit client for making requests using GSON as the parser for objects.
     */
    private fun buildRetrofitClient(): Retrofit {
        val restClient: Retrofit.Builder = Retrofit
                .Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
        return restClient.client(buildHttpInterceptor()).build()
    }

    /**
     * Builds an HTTP interceptor to log the request and petitions into LogCat.
     */
    private fun buildHttpInterceptor(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client: OkHttpClient.Builder = OkHttpClient.Builder()

        // Is not desired to log the HTTP output in production code, so just add this logger in the release build type.
        return if (BuildConfig.DEBUG) {
            client.addInterceptor(interceptor).build()
        } else {
            client.build()
        }
    }

    /**
     * Override this method to use the RestClient and choose a method
     * defined in the Routes interface using Retrofit guidelines.
     *
     * using:
     * call = routes.<method>
     * call.enqueue(this)
     */
    abstract fun performRequest(body: Any)

    /**
     * Use this function to handle unauthorized access such as expired tokens.
     */
    abstract fun handleUnauthorizedResponse()

    /**
     * Sends a string to the view to display an error.
     */
    private fun onErrorHandling(message: String) {
        view.displayLoadingView(false)
        view.displayErrorMessage(message)
    }

    /**
     * Sends a String Res value to the view to display an error.
     */
    private fun onErrorHandling(@StringRes message: Int) {
        view.displayLoadingView(false)
        view.displayErrorMessage(message)
    }

    /**
     * Send the retrieved data to the view.
     *
     * If any operation needs to be made, this function can be overwritten.
     */
    protected open fun onSuccessfulResponse(response: RE?) {
        view.setup(response)
        view.displayLoadingView(false)
    }

    /**
     * Handle when the request has failed.
     */
    override fun onFailure(call: Call<RE>?, t: Throwable?) {
        if (t is HttpException) {
            val code = t.response().code()
            if (code == HTTP_UNAUTHORIZED) {
                handleUnauthorizedResponse()
            } else {
                onErrorHandling("HTTP " + code)
            }
        } else {
            onErrorHandling(t?.localizedMessage!!)
        }
    }

    /**
     * Handle when a response is successful.
     *
     * A response having code HTTP 200, doesn't mean it was successful at all,
     * some additional checks needs to be made.
     */
    override fun onResponse(call: Call<RE>?, response: Response<RE>?) {
        if (response != null) {
            when {
                response.isSuccessful -> onSuccessfulResponse(response.body())
                response.code() == HTTP_UNAUTHORIZED -> handleUnauthorizedResponse()
                else -> onErrorHandling(response.errorBody().toString())
            }
        } else {
            onErrorHandling(R.string.empty_response)
        }
    }
}