package com.taiyoapp.taiyo.anime.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Screenshot(
    val original: String
) : Parcelable