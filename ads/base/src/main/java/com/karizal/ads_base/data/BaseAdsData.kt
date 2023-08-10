package com.karizal.ads_base.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
open class BasicAdsData(
    open val name: String,
): Parcelable