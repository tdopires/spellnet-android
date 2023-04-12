package br.com.spellnet.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CardCollection(
    val haveList: List<CardQuantity>
) : Parcelable