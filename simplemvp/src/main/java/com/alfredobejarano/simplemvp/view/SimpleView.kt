package com.alfredobejarano.simplemvp.view

import android.support.annotation.StringRes

/**
 * Interface that defines which methods an interface needs to have.
 *
 * RE - Generic that defines which class the response from the presenter is going to be.
 *
 * @author @AlfredoBejarano
 * @version 1.0
 * @since 30/12/2017
 */
interface SimpleView<RE> {
    /**
     * This method should be used to define the value
     * for the presenter field of the instance that implements
     * this interface.
     */
    fun attachPresenter()

    /**
     * This method should be used for using the data retrieved
     * and display it in the layout views, any processing of them
     * should be used on a Presenter method and the result of the
     * processing be displayed here.
     */
    fun setup(response: Any?)

    /**
     * This method should be used to display an error
     * using a String object as the message.
     */
    fun displayErrorMessage(message: String)

    /**
     * This method should be used to display an error
     * using a @StringRes Integer as the message.
     */
    fun displayErrorMessage(@StringRes message: Int)

    /**
     * This method should process when the view is waiting for a response
     * (ej. Presenter doing stuff in other thread).
     *
     * Displaying a designed view for "loading" is strongly recommended.
     */
    fun displayLoadingView(isVisible: Boolean)
}