package com.alfredobejarano.simplemvp.view

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alfredobejarano.simplemvp.R
import com.alfredobejarano.simplemvp.presenter.SimplePresenter


/**
 * Simple [Fragment] class that implements [SimpleView].
 * @param T Type of the ViewDataBinding for this fragment.
 *
 * @author Alfredo Bejarano
 * @since August 10th, 2018 - 12:18 AM
 * @version 3.0
 */
abstract class SimpleFragment<T : ViewDataBinding> : SimpleView, Fragment() {
    /**
     * Reference to the content view of this fragment.
     */
    private var mContent: View? = null

    /**
     * Observer that will display the loading view if a Presenter is busy.
     */
    private val mLoadingObserver = Observer<SimplePresenter.Status> {
        displayLoadingView(it == SimplePresenter.Status.STATUS_BUSY)
    }

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
    private var mBinding: T? = null

    /**
     * Creates this fragment view using the layout defined in layout_base.xml
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate this fragment root.
        val root = inflater.inflate(R.layout.layout_base, container, false) as ViewGroup
        // Now, inflate the layout content that will make the ViewDataBinding object.
        mBinding = DataBindingUtil.inflate(inflater, getLayoutId(), root.findViewById<ViewGroup>(R.id.base_content), true)
        // Set the binding reference LifeCycleOwner.
        mBinding?.setLifecycleOwner(this)
        // Assign the mContent value as the root of the fragment.
        mContent = root
        // Return the inflated view.
        return root
    }

    /**
     * After creating the view, inflate the DataBinding for this fragment
     * into the base_content ViewGroup and proceed to setup this view.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Start the view.
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
    override fun displayMessage(message: Any?) {
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
        mMessagesSnackbar = Snackbar.make(mContent!!, "", Snackbar.LENGTH_SHORT)
        // Initialize this view widgets.
        setup()
    }

    /**
     * Destroys all view references from the presenters.
     */
    private fun detachPresenters() {
        // Check if there are presenters attached to this view.
        if (mPresenters.isNotEmpty()) {
            // Destroy every single reference to this view.
            mPresenters.forEach {
                it.value.destroyView()
            }
            // Clear the presenters list.
            mPresenters.clear()
        }
    }

    /**
     * Adds a SimplePresenter class to the list of presenters for this view.
     */
    protected fun attachPresenter(vararg presenters: SimplePresenter) {
        // Check if at least a SimplePResenter object has been sent.
        if (presenters.isNotEmpty()) {
            // Add the presenters to the mPresenters HashMap.
            presenters.forEach {
                // Add the current presenter to the list.
                mPresenters[it.javaClass.simpleName] = it
                // Observe the status property of the presenter.
                observe(it.status, mLoadingObserver)
            }
        }
    }

    /**
     * Retrieves a given presenter class from the list.
     * @return The desired presenter class, if it exists.
     */
    protected fun <P : SimplePresenter> getPresenter(presenterClass: Class<P>): P? {
        val presenterClassName = presenterClass.simpleName
        return if (mPresenters.containsKey(presenterClassName)) {
            val foundPresenter = mPresenters[presenterClassName]
            presenterClass.cast(foundPresenter)
        } else {
            null
        }
    }

    /**
     * This function returns the delegated view that will indicate progress.
     */
    protected fun getLoadingView(): ViewGroup? = mContent?.findViewById(R.id.loading_content)

    /**
     * Observes changes from a [MutableLiveData] object.
     *
     * @param liveData The object to be observed.
     * @param observer How the changed values are going to behave.
     * @param <T>      Generic defining the type of data the liveData object has.*/
    protected fun <T> observe(liveData: MutableLiveData<T>, observer: Observer<T>) {
        liveData.observe(this, observer)
    }

    /**
     * This function will return the layout ID for this activity content.
     */
    abstract fun getLayoutId(): Int

    /**
     * Returns the [ViewDataBinding] object for this class.
     */
    fun getBinding(): T? = mBinding
}
