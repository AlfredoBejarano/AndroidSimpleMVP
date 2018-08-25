package com.alfredobejarano.simplemvp.presenter

import com.alfredobejarano.simplemvp.BuildConfig
import com.alfredobejarano.simplemvp.view.SimpleView
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * [SimplePresenter] class that allows Network operations using Retrofit.
 *
 * @param T generic containing your [Retrofit API definitions.][https://square.github.io/retrofit/]
 * @param view View reference for this presenter.
 * @param apiDefinitions Class type of your API definitions interface.
 *
 * @author Alfredo Bejarano
 * @since August 25th, 2018 - 12:37 PM
 * @version 1.0
 */
abstract class SimpleNetworkPresenter<T>(view: SimpleView?, apiDefinitions: Class<T>) : SimplePresenter(view) {
    /**
     * Reference for accessing the Retrofit API definitions.
     */
    protected var routes: T? = null

    init {
        initClient(apiDefinitions)
    }

    /**
     * Initializes the API definitions and assigns it to the routes property.
     */
    private fun initClient(apiDefinitions: Class<T>) {
        // Create a new Implementation of the Routes interface using a Retrofit instance.
        routes = buildRetrofit().create(apiDefinitions)
    }

    /**
     * Builds the retrofit instance for performing HTTP operations.
     *
     * @return The retrofit client, completely built.
     */
    private fun buildRetrofit(): Retrofit {
        return Retrofit.Builder()
                // Assign the BaseURL for this Retrofit instance requests.
                .baseUrl(getBaseURL())
                // Add a GsonConverter for mapping DTOs from JSON or XML.
                .addConverterFactory(GsonConverterFactory.create())
                // Assign a OkHTTPClient to this Retrofit instance.
                .client(buildHttpInterceptor())
                // Build the instance.
                .build()
    }

    /**
     * Enqueues a given request with a given success listener and failure listener..
     *
     * @return true if the request has been enqueued correctly.
     */
    protected fun <M> enqueueRequest(request: Call<M>?,
                                     success: (payload: M?) -> Unit,
                                     failure: (payload: Any?) -> Unit): Boolean {
        // Checks if the request has not been executed.
        return if (request?.isExecuted == false) {
            // Enqueue the request creating a new Callback instance.
            request.enqueue(object : Callback<M> {
                // Receive the data when a response has been returned.
                override fun onResponse(call: Call<M>?, response: Response<M>?) {
                    // An additional check needs to be made to know if the response was successful.
                    if (response?.isSuccessful == true) {
                        success(response.body())
                    } else {
                        failure(response)
                    }
                }

                /**
                 * Report the throwable that has been thrown by the request enqueueing.
                 */
                override fun onFailure(call: Call<M>?, t: Throwable?) = failure(t)
            })
            true
        } else {
            false
        }
    }

    /**
     * Enqueues a given request with a given success listener and failure listener..
     *
     * @return true if the request has been enqueued correctly.
     */
    protected fun <M> enqueueRequest(request: Call<M>?,
                                     success: (payload: M?) -> Unit): Boolean {
        // Checks if the request has not been executed.
        return enqueueRequest(request, success) {
            sendMessage(it)
        }
    }

    /**
     * Builds an OkHTTP interceptor that will log the body of HTTP requests
     * to LogCat only in the debug flavor.
     *
     * @return HttpInterceptor for a Retrofit client.
     */
    private fun buildHttpInterceptor(): OkHttpClient {
        // Create a logging interceptor, this will print logs from the HTTP request into LogCat.
        val interceptor = HttpLoggingInterceptor()
        //  Set the logging level to the Body of the request.
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        // Create a new OkHttpClient instance.
        val httpClient = OkHttpClient.Builder()
                // Set the read timeout to 10 seconds.
                .readTimeout(10, TimeUnit.SECONDS)
                // Set the connection timeout to 10 seconds.
                .connectTimeout(10, TimeUnit.SECONDS)
        /*
          If the app is running under debug, add the interceptor.
          This prevent HTTP calls being logged in production.
         */
        if (BuildConfig.DEBUG) {
            httpClient.addInterceptor(interceptor)
        }
        // Return the OkHTTPClient that has been built.
        return httpClient.build()
    }

    /**
     * @return Defines the base URL for this Presenter network operations.
     */
    abstract fun getBaseURL(): String
}