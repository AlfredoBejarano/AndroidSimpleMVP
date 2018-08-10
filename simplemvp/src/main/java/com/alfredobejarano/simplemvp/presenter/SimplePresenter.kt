package com.alfredobejarano.simplemvp.presenter

import android.os.Handler
import android.util.Log
import com.alfredobejarano.simplemvp.view.SimpleView

/**
 * This presenter class defines the most basic functionality for a Presenter class.
 *
 * @author Alfredo Bejarano
 * @version 2.0
 * @since 09/08/2018
 */
abstract class SimplePresenter(protected var view: SimpleView?) {
    /**
     * Sends a message to the attached view.
     * * If the view Context is not null, the message is sent to it.
     * * If the view is null (let's say, while Unit Testing), the message is printed to the LogCat.
     *
     * @param message The message object to be sent to the view.
     */
    fun sendMessage(message: Any) {
        // Check if the View's Context is not null.
        view?.asContext()?.let {
            Handler(it.mainLooper).post {
                // Send the display message if it is not null.
                view?.displayMessage(message)
            }
        } ?: run {
            // If the view Context is null, print the message String value.
            Log.d(this::class.java.simpleName, message.toString())
        }
    }

    /**
     * Reports to the view that this presenter is doing some processing work.
     * If the view is null (let's say, while Unit testing) the isWorking value
     * gets printed in the LogCat.
     *
     * @param isWorking If the presenter is working or not.
     */
    fun reportProgress(isWorking: Boolean) {
        // Check if the View's Context is not null.
        view?.asContext()?.let {
            Handler(it.mainLooper).post {
                // Report the presenter processing status to the view.
                view?.displayLoadingView(isWorking)
            }
        } ?: run {
            // If the view Context is null, print the Status to the LogCat.
            Log.d(this::class.java.simpleName, "Presenter is working: $isWorking")
        }
    }

    /**
     * Destroy the view reference for this
     * presenter for it to be garbage collected.
     */
    fun onDestroyView() {
        view = null
    }
}
