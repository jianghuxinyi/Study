package com.linanqing.passwordmanager.ui.app

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
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

    fun getAllApp(ctx: Context,appList: SnapshotStateList<App>) {
        val pm = ctx.packageManager
        val packList = pm.getInstalledPackages(0)
        for (item in packList) {
            if (item.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0){
                continue
            }
            try {
                val packName = item.applicationInfo.packageName
                val info = pm.getApplicationInfo(packName, PackageManager.GET_META_DATA)
                val appName = pm.getApplicationLabel(info).toString()
                appList.add(App(appName, packName))
            } catch (e: Exception) {
            }
        }
    }
}

