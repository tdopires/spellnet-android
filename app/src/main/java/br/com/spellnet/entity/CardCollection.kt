package br.com.spellnet.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardCollection(
    val haveList: List<CardQuantity>
) : Parcelable