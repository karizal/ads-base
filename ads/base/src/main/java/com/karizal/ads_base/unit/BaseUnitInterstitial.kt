package com.karizal.ads_base.unit

import android.app.Activity
import android.content.Context
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.InterstitialContract
import com.karizal.ads_base.pref.AdsBasePref

class BaseUnitInterstitial(
    private val isEnabled: Boolean = true,
    private val isDebug: Boolean = false,
    private val key: String,
    private val channels: Array<out InterstitialContract>,
) {
    private var channelQueue: MutableSet<String> = mutableSetOf()
    private var failedChannels: MutableSet<String> = mutableSetOf()

    fun onCreate(activity: Activity?) {
        activity?.let {
            failedChannels = mutableSetOf()
            channels.forEach { channel ->
                channelQueue.add(channel.name)
                channel.initialize(it, isDebug, ::onInitializeOK, ::onInitializeError)
            }
        }
    }

    private fun onInitializeOK(channel: String) {
        failedChannels.remove(channel)
    }

    private fun onInitializeError(channel: String) {
        failedChannels.add(channel)
    }

    private fun possibleToShow(channel: String): Boolean {
        return failedChannels.firstOrNull { it == channel } == null
    }

    fun show(activity: Activity?, interval: Int, onHide: () -> Unit = {}) {
        activity ?: return
        if (channelQueue.isEmpty()) {
            onCreate(activity)
        }

        if (isEnabled && shouldShow(activity, interval)) {
            showInterstitial(activity, onHide)
            return
        }
        onHide.invoke()
    }

    fun showForce(activity: Activity?, onHide: () -> Unit = {}) {
        activity ?: return
        if (channelQueue.isEmpty()) {
            onCreate(activity)
        }
        if (isEnabled) {
            showInterstitial(activity, onHide)
        } else {
            onHide.invoke()
        }
    }

    private fun showInterstitial(activity: Activity, onHide: () -> Unit) {
        if (channelQueue.isEmpty()) {
            return onHide.invoke()
        }

        val channel = channelQueue.first()

        channelQueue.remove(channel)

        val selectedChannel = when (channel) {
            AdsBaseConst.admob -> {
                channels.find { it.name == AdsBaseConst.admob }
            }

            AdsBaseConst.fans -> {
                channels.find { it.name == AdsBaseConst.fans }
            }

            AdsBaseConst.applovin -> {
                channels.find { it.name == AdsBaseConst.applovin }
            }

            AdsBaseConst.appodeal -> {
                channels.find { it.name == AdsBaseConst.appodeal }
            }

            AdsBaseConst.unity -> {
                channels.find { it.name == AdsBaseConst.unity }
            }

            AdsBaseConst.startio -> {
                channels.find { it.name == AdsBaseConst.startio }
            }

            else -> null
        }

        selectedChannel?.show(
            activity,
            ::possibleToShow,
            onHide,
            onFailure = {
                showInterstitial(activity, onHide)
            }
        )
    }

    private fun shouldShow(activity: Activity, interval: Int): Boolean {
        val savedInterval = getIntervalByKey(activity)
        saveIntervalByKey(activity, savedInterval + 1)

        if (savedInterval == 0 || savedInterval % interval == 0) {
            return true
        }

        return false
    }

    private fun getIntervalByKey(activity: Activity): Int {
        return AdsBasePref(activity).getInt("${AdsBaseConst.PREF_INTERVAL_BY_KEY}-$key")
    }

    private fun saveIntervalByKey(activity: Activity, interval: Int) {
        AdsBasePref(activity).saveInt("${AdsBaseConst.PREF_INTERVAL_BY_KEY}-$key", interval)
    }

    companion object {
        fun clearInterval(context: Context?) {
            context ?: return
            AdsBasePref(context).clearContains(AdsBaseConst.PREF_INTERVAL_BY_KEY)
        }
    }
}