package com.linanqing.passwordmanager.ui.app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linanqing.passwordmanager.AccountTopAppBar
import com.linanqing.passwordmanager.R
import com.linanqing.passwordmanager.data.App
import com.linanqing.passwordmanager.ui.AppViewModelProvider
import com.linanqing.passwordmanager.ui.account.AccountEntryBody
import com.linanqing.passwordmanager.ui.account.AccountEntryDestination
import com.linanqing.passwordmanager.ui.navigation.NavigationDestination
import com.linanqing.passwordmanager.utils.BiometricCallback
import com.linanqing.passwordmanager.utils.biometricUtils
import com.linanqing.passwordmanager.utils.promptInfo
import kotlinx.coroutines.launch

object AppListScreenDestination : NavigationDestination {
    override val route = "select_app"
    override val titleRes = R.string.select_app
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AppListViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    var apps = remember { mutableStateListOf<App>() }
    LaunchedEffect(Unit) {
        apps.addAll(viewModel.getAllApp(ctx))
    }
    Scaffold(
        topBar = {
            AccountTopAppBar(
                title = stringResource(AppListScreenDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->


        AppList(modifier = Modifier
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
            .fillMaxWidth(),
            apps
            )
    }
}

@Composable
fun AppList(modifier: Modifier,appList:List<App>){
    LazyColumn(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))){
        items(appList){app->
            AppItem(app)
        }
    }
}

@Composable
fun AppItem(app:App){
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = app.name)
        Image(bitmap = app.bitmap.asImageBitmap(), contentDescription = "", modifier = Modifier.size(40.dp))
    }
}