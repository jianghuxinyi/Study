package com.linanqing.passwordmanager.ui.app

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PixelFormat
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linanqing.passwordmanager.AccountTopAppBar
import com.linanqing.passwordmanager.R
import com.linanqing.passwordmanager.data.App
import com.linanqing.passwordmanager.ui.AppViewModelProvider
import com.linanqing.passwordmanager.ui.navigation.NavigationDestination


object AppListScreenDestination : NavigationDestination {
    override val route = "select_app"
    override val titleRes = R.string.select_app
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    navController: NavHostController,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AppListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AccountTopAppBar(
                title = stringResource(AppListScreenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                scrollBehavior = scrollBehavior,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        AppListBody(modifier = modifier
            .padding(innerPadding)
            .fillMaxSize(), viewModel = viewModel, navController)
    }
}


@Composable
private fun AppListBody(modifier:Modifier,viewModel: AppListViewModel,navController: NavHostController){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        val ctx = LocalContext.current
        var apps = remember { mutableStateListOf<App>() }
        AppList(modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small)),
            apps,navController
        )
        LaunchedEffect(Unit) {
            viewModel.getAllApp(ctx,apps)
        }

    }

}

@Composable
fun AppList(modifier: Modifier,appList:List<App>,navController: NavHostController){
    LazyColumn(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))){
        items(appList){app->
            AppItem(app, modifier = Modifier
                .padding(dimensionResource(id = R.dimen.padding_small)),navController)
        }
    }
}

@Composable
fun AppItem(app:App,modifier: Modifier,navController: NavHostController){

    val ctx = LocalContext.current
    val info = ctx.packageManager.getPackageInfo(app.packName,PackageManager.GET_ACTIVITIES)
    val iconDrawable =info.applicationInfo.loadIcon(ctx.packageManager)
    val iconBitmap = drawableToBitamp(iconDrawable)//(iconDrawable as BitmapDrawable).bitmap

    Surface(onClick = {
        navController.previousBackStackEntry?.savedStateHandle?.set("icon",app.packName)
        navController.popBackStack()
    } ) {
        Row(
            modifier =modifier,
        ) {
            // 应用图标
            Image(bitmap = iconBitmap!!.asImageBitmap(), contentDescription = "", modifier = Modifier.size(40.dp))
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier= Modifier
                        .height(40.dp)
                        .padding(start = 20.dp), verticalArrangement = Arrangement.Center) {
                        Text(
                            text = app.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                    // 右侧进入按钮
                    Image(painter = painterResource(id = R.drawable.baseline_chevron_right_24), contentDescription = "应用",
                        modifier= Modifier
                            .size(24.dp)
                            .align(Alignment.CenterVertically))
                }
                // 底部分割线
                Divider(thickness = 0.5.dp, color = Color.Gray, modifier = Modifier.padding(start = 20.dp,top=20.dp))
            }

        }
    }

//
//    Row(modifier = Modifier.fillMaxWidth()) {
//        Text(text = app.name)
//        Image(bitmap = app.bitmap.asImageBitmap(), contentDescription = "", modifier = Modifier.size(40.dp))
//    }
}


private fun drawableToBitamp(drawable: Drawable): Bitmap? {
    val bitmap = Bitmap.createBitmap(
        drawable.intrinsicWidth, drawable.intrinsicHeight,
        if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
    )
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
    drawable.draw(canvas)
    return bitmap
}
