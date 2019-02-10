package br.com.spellnet.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal

@Parcelize
data class Card(val name: String) : Parcelable

@Parcelize
data class CardPricing(val card: Card, val minPrice: BigDecimal?, val midPrice: BigDecimal?, val maxPrice: BigDecimal?) : Parcelable