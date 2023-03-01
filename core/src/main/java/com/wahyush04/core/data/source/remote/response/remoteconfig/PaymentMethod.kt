package com.wahyush04.core.data.source.remote.response.remoteconfig

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class PaymentMethod(
    val data: List<DataItem?>? = null,
    val id: String? = null,
    val type: String? = null,
    val order: Int? = null
) : Parcelable

@Parcelize
data class DataItem(
	val name: String? = null,
	val id: String? = null,
	val order: Int? = null,
	val status: Boolean? = null
) : Parcelable

@Parcelize
data class BankType(
	val type: String? = null
) : Parcelable

