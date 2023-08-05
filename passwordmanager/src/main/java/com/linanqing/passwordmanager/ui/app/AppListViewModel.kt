package com.linanqing.passwordmanager.ui.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.linanqing.passwordmanager.data.Account
import com.linanqing.passwordmanager.data.App
import com.linanqing.passwordmanager.ui.home.HomeUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

class AppListViewModel : ViewModel() {

    //var apps by mutableStateListOf<App>()

    //val appUiState: StateFlow<AppUiState> = getAllApp.map {  }

    fun getAllApp(ctx: Context,appList: SnapshotStateList<App>) {
        val packList = ctx.packageManager.getInstalledPackages(0)
        for (item in packList) {
            if (item.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0){
                continue
            }
            try {
                val appName = item.applicationInfo.loadLabel(ctx.packageManager).toString()
                val iconDrawable = item.applicationInfo.loadIcon(ctx.packageManager)
                val iconBitmap = (iconDrawable as BitmapDrawable).bitmap
                appList.add(App(appName, iconBitmap))
            } catch (e: Exception) {
            }
        }
    }

    //data class AppUiState(val aooList: List<App> = listOf())
}

