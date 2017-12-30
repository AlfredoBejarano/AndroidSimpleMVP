package com.alfredobejarano.sampleapp.repository

import com.alfredobejarano.sampleapp.model.SampleApp
import retrofit2.Call
import retrofit2.http.GET

/**
 * Example of how the request methods needs to be built.
 *
 * It uses Retrofit.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
interface Routes {
    @GET("get")
    fun getSampleApp(): Call<SampleApp>
}