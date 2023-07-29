package com.linanqing.passwordmanager

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Account(var account:String,var password:String,var email: String,var phone :String):
    Parcelable
