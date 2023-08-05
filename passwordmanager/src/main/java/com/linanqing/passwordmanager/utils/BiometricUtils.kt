package com.linanqing.passwordmanager.utils

import android.content.Context
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

val promptInfo by lazy {
    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("身份验证")
        //.setSubtitle("请进行身份验证")
        .setNegativeButtonText("取消")
        .build()
    promptInfo
}

fun biometricUtils(ctx:Context,callback: BiometricCallback): BiometricPrompt {
    val executor = ContextCompat.getMainExecutor(ctx)
    val biometricPrompt = BiometricPrompt(ctx as FragmentActivity, executor,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int,
                                               errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
//                Toast.makeText(ctx,
//                    "Authentication Tips: $errString", Toast.LENGTH_SHORT)
//                    .show()
                callback.error()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
//                Toast.makeText(ctx,
//                    "Authentication Succ!", Toast.LENGTH_SHORT)
//                    .show()
                callback.success()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
//                Toast.makeText(ctx, "Authentication failed",
//                    Toast.LENGTH_SHORT)
//                    .show()
                callback.failed()
            }
        })

    return biometricPrompt
}

interface BiometricCallback {
    fun success()
    fun error()
    fun failed()
}