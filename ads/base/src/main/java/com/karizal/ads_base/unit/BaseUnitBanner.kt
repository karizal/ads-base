package com.karizal.ads_base.unit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.karizal.ads_base.AdsBaseConst
import com.karizal.ads_base.contract.BannerContract
import com.karizal.ads_base.databinding.BaseUnitBannerBinding

class BaseUnitBanner @JvmOverloads constructor(
    private val channels: Array<out BannerContract> = arrayOf(),
) : Fragment() {

    private var forceAdsChannel: String? = null
    private var channelQueue: MutableSet<String> = mutableSetOf()

    private var safeBinding: BaseUnitBannerBinding? = null
    private var channelLoaded: String? = null
    private var isDebug = false

    private var forceRetryAttempt = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forceRetryAttempt = 0

        activity?.let {
            channels.forEach { channel ->
                channelQueue.add(channel.name)
                channel.initialize(it, isDebug)
            }
        }

        fetch()
    }

    private fun fetch() {
        if (channelQueue.isEmpty()) return
        val adContainer = safeBinding?.relativeBanner ?: return

        forceAdsChannel?.let { channel ->
            if (forceRetryAttempt > 3) {
                return
            }
            forceAdsChannel += 1
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
            selectedChannel?.fetch(
                adContainer,
                ::preparing,
                ::possibleToLoad,
                ::onSuccessFetch,
                ::onFailedFetch
            )
            return
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

        selectedChannel?.fetch(
            adContainer,
            ::preparing,
            ::possibleToLoad,
            ::onSuccessFetch,
            ::onFailedFetch
        )
    }

    private fun preparing() {
        channelLoaded = null
    }

    private fun possibleToLoad(): Boolean {
        return channelLoaded == null
    }

    private fun onSuccessFetch(channel: String) {
        channelLoaded = channel
    }

    private fun onFailedFetch() {
        fetch()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        safeBinding = BaseUnitBannerBinding.inflate(inflater, container, false)
        return safeBinding?.root!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        safeBinding = null
    }

    companion object {
        fun init(
            activity: FragmentActivity,
            resId: Int,
            isDebug: Boolean = false,
            vararg channels: BannerContract,
            forceAdsChannel: String? = null
        ) {
            val frag = BaseUnitBanner(channels)
            frag.isDebug = isDebug
            if (isDebug && forceAdsChannel != null) {
                frag.forceAdsChannel = forceAdsChannel
            }
            with(activity) {
                supportFragmentManager.beginTransaction().replace(resId, frag)
                    .commit()
            }
        }
    }
}