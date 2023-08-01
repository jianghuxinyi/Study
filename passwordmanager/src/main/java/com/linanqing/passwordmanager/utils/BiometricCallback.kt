package com.linanqing.passwordmanager.utils

interface BiometricCallback {
    fun success()
    fun error()
    fun failed()
}