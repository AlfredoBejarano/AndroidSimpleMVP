package com.alfredobejarano.sampleapp.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Example of a Kotlin class for JSON serialization.
 *
 * It uses GSON.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
data class DeviceIP(
        @Expose
        @SerializedName("origin")
        var origin: String,
        @Expose
        @SerializedName("url")
        val url: String
)