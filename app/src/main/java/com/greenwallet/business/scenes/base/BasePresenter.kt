package com.greenwallet.business.scenes.base

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.util.*

/**
 * Base class for all Presenters.
 *
 *
 * A Presenter handles events from Views, updates Views, processes data, ...
 *
 *
 */

abstract class BasePresenter<T, U> : BaseViewModel<T> {

    protected var views: MutableSet<T> = HashSet()

    @Nullable
    protected var activityHandler: U? = null

    /**
     * Sets the Views current UI state to represent the Presenters state and subscribes the View to upcoming UI events.
     *
     * @param view View to subscribe
     */
    override fun subscribeView(view: T) {
        views.add(view)
        onViewSubscribed(view)
    }

    /**
     * After calling the given View will no longer get UI updates.
     *
     * @param view View
     */
    override fun disposeView(view: T) {
        views.remove(view)
    }

    /**
     * Sets the given Object to handle Application updates.
     * Usually this will be an Activity and the Presenter will use it to start other Activities.
     * Activities should call this in there onStart() method.
     *
     * @param handler handler
     */
    fun subscribeHandler(handler: U) {
        this.activityHandler = handler
        onActivityHandlerSubscribed()
    }

    /**
     * Should be called in Activity.onStop()
     *
     * @see BasePresenter.subscribeHandler
     */
    fun disposeHandler() {
        this.activityHandler = null
    }

    /**
     * Will be called whenever a View registers.
     * The Presenter should update the View to represent its current state.
     *
     * @param view View
     */
    protected abstract fun onViewSubscribed(@NotNull view: T)

    /**
     * Will be called whenever the Handler registers.
     * The Presenter should check if there are any updates for the Handler are available.
     * For Example the User pressed on "login" and left the Activity during the network call.
     * When he enters again and the Login-Request was successful he should be forwarded to the dashboard.
     */
    protected abstract fun onActivityHandlerSubscribed()
}
