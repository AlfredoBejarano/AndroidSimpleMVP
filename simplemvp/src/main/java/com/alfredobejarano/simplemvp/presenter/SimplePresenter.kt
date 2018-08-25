package com.alfredobejarano.simplemvp.presenter

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
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
     * Status value that reports if this presenter is busy or not.
     * Wrapped in [MutableLiveData] allowing a [LifecycleOwner][android.arch.lifecycle.LifecycleOwner]
     * to observe it.
     */
    val status = MutableLiveData<Status>()

    /**
     * This function will remove the reference to the given view,
     * preventing leakages of Context child classes as Views usually
     * extends from the Context class.
     */
    fun destroyView() {
        if (view != null) {
            view = null
        }
    }

    /**
     * Sends a message object to the view.
     */
    protected fun sendMessage(payload: Any?) = runOnUIThread {
        view?.displayMessage(payload)
    }

    /**
     * Executes a function in the UI thread.
     */
    private fun runOnUIThread(f: () -> Unit) =
            Handler(view?.asContext()?.mainLooper).post(f)

    /**
     * Enum class that defines the current mStatus of a presenter.
     */
    enum class Status {
        /**
         * This value defines that the presenter is doing work.
         */
        STATUS_BUSY,
        /**
         * This value defines that the presenter is done doing work.
         */
        STATUS_DONE
    }
}
