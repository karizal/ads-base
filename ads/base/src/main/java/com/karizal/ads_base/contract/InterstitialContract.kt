package com.karizal.ads_base.contract

import android.app.Activity
import android.util.Log

interface InterstitialContract {
    val name: String
    var isDebug: Boolean
    var activity: Activity?
    var onInitializeOK: (name: String) -> Unit
    var onInitializeError: (name: String) -> Unit
    fun initialize(
        activity: Activity,
        isDebug: Boolean,
        onInitializeOK: (name: String) -> Unit,
        onInitializeError: (name: String) -> Unit
    ) {
        this.isDebug = isDebug
        this.activity = activity
        this.onInitializeOK = onInitializeOK
        this.onInitializeError = onInitializeError
        Log.i(getClassName(), "initialize ${getClassName()}")
    }

    fun show(
        activity: Activity,
        possibleToShow: (channel: String) -> Boolean,
        onHide: () -> Unit, onFailure: (activity: Activity) -> Unit
    )

    fun getClassName(): String = this::class.java.simpleName
}