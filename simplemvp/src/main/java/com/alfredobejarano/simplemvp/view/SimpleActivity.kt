package com.alfredobejarano.simplemvp.view

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import com.alfredobejarano.simplemvp.R
import com.alfredobejarano.simplemvp.presenter.SimplePresenter

/**
 * Simple [AppCompatActivity] class that implements [SimpleView].
 * @param T Type of the ViewDataBinding for this activity.
 *
 * @author Alfredo Bejarano
 * @since August 10th, 2018 - 12:18 AM
 * @version 3.0
 */
abstract class SimpleActivity<T : ViewDataBinding> : AppCompatActivity(), SimpleView {
    /**
     * Snackbar that will display messages.
     */
    private var mMessagesSnackbar: Snackbar? = null

    /**
     * HashMap that will contain BasePresenter classes for this view.
     */
    private val mPresenters: HashMap<String, SimplePresenter> = hashMapOf()

    /**
     * Reference to the [ViewDataBinding] for this view,
     * allowing binding from presenter classes or values.
     */
    protected var mBinding: T? = null

    /**
     * Creates this activity, and inflates the activity layout inside
     * the base layout FrameLayout, allowing access to the loading view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the content view for this activity as the one defined in layout_base.xml.
        setContentView(R.layout.layout_base)
        // Then, proceed to inflate this view data binding inside the base_content view.
        mBinding = DataBindingUtil.inflate(layoutInflater, getLayoutId(), findViewById(R.id.base_content), true)
        // Start this view.
        startView()
    }

    /**
     * Destroys this activity and detaches the presenters for this activity.
     */
    override fun onDestroy() {
        super.onDestroy()
        detachPresenters()
    }

    /**
     * Sets the visibility of a given loading view or indicator defines in this View.
     *
     *  * **true** should be used for displaying the loading indicator.
     *  * **false** should be used for hiding the loading indicator.
     *
     *
     * @param displayView Boolean value for displaying or hiding the view.
     */
    override fun displayLoadingView(displayView: Boolean) {
        getLoadingView()?.visibility = if (displayView) View.VISIBLE else View.GONE
    }

    /**
     * This method should check which class the message parameter
     * is and notify to the user accordingly.
     *
     * @param message The data to be displayed to the user.
     */
    override fun displayMessage(message: Any) {
        when (message) {
            is Int -> mMessagesSnackbar?.setText(message)
            is String -> mMessagesSnackbar?.setText(message)
            is Throwable -> mMessagesSnackbar?.setText(message.localizedMessage)
            else -> mMessagesSnackbar?.setText(message.toString())
        }
        displayLoadingView(false)
        mMessagesSnackbar?.show()
    }

    /**
     * Initializes the Snackbar message object and then proceeds to setup the view.
     */
    private fun startView() {
        // Initialize the messages snackbar.
        mMessagesSnackbar = Snackbar.make(findViewById(android.R.id.content), "", Snackbar.LENGTH_SHORT)
        // Initialize this view widgets.
        setup()
        // Executes pending bindings, if defined within setup().
        mBinding?.executePendingBindings()
    }

    /**
     * Destroys all view references from the presenters.
     */
    private fun detachPresenters() {
        // Destroy every single reference to this view.
        mPresenters.forEach {
            it.value.onDestroyView()
        }
        // Clear the presenters list.
        mPresenters.clear()
    }

    /**
     * Adds a SimplePresenter class to the list of presenters for this view.
     */
    protected fun attachPresenter(vararg presenters: SimplePresenter) = presenters.forEach {
        mPresenters[it.javaClass.simpleName] = it
    }

    /**
     * Retrieves a given presenter class from the list.
     * @return The desired presenter class, if it exists.
     */
    protected fun <P : SimplePresenter> getPresenter(presenterClass: Class<P>): P? {
        var presenter: P? = null
        if (mPresenters.containsKey(presenterClass.simpleName)) {
            presenter = presenterClass.cast(mPresenters[presenterClass.simpleName])
        }
        return presenter
    }

    /**
     * This function returns the delegated view that will indicate progress.
     */
    protected fun getLoadingView(): ViewGroup? = findViewById(R.id.loading_content)

    /**
     * This function will return the layout ID for this activity content.
     */
    abstract fun getLayoutId(): Int
}