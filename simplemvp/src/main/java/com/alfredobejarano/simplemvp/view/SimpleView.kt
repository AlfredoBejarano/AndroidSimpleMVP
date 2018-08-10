package com.alfredobejarano.simplemvp.view

import android.content.Context


/**
 * Interface that defines which methods a View
 * needs to have for ths architecture to work.
 *
 * @author Alfredo Bejarano
 * @version 3.0
 * @since 09/08/2018
 */
interface SimpleView {
    /**
     * This method should be used for assigning listeners or callbacks to
     * widgets used by this View layout and attaching a presenter class.
     */
    fun setup()

    /**
     * Method that will define this view as a Context instance.
     *
     * @return The context in one the view is currently existing.
     */
    fun asContext(): Context?

    /**
     * Sets the visibility of a given loading view or indicator defines in this View.
     *
     *  * **true** should be used for displaying the loading indicator.
     *  * **false** should be used for hiding the loading indicator.
     *
     *
     * @param displayView Boolean value for displaying or hiding the view.
     */
    fun displayLoadingView(displayView: Boolean)

    /**
     * This method should check which class the message parameter
     * is and notify to the user accordingly.
     *
     * @param message The data to be displayed to the user.
     */
    fun displayMessage(message: Any)
}