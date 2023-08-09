package com.karizal.ads_base.contract

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.ViewGroup

interface BannerContract {
    val name: String
    var isDebug: Boolean
    var activity: Activity?
    fun initialize(activity: Activity, isDebug: Boolean) {
        this.isDebug = isDebug
        this.activity = activity
        Log.i(getClassName(), "initialize ${getClassName()}")
    }

    fun fetch(
        container: ViewGroup,
        preparing: () -> Unit,
        possibleToLoad: () -> Boolean,
        onSuccessLoaded: (channel: String) -> Unit,
        onFailedLoaded: () -> Unit = {}
    )

    fun hideView(container: ViewGroup) {
        container.visibility = View.GONE
    }

    fun prepareContainerView(container: ViewGroup) {
        container.visibility = View.VISIBLE
    }

    fun getClassName(): String = this::class.java.simpleName

}