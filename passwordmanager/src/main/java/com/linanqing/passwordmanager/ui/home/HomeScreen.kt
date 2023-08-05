package com.linanqing.passwordmanager.ui.home

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linanqing.passwordmanager.AccountApplication.Companion.colorArray
import com.linanqing.passwordmanager.AccountTopAppBar
import com.linanqing.passwordmanager.R
import com.linanqing.passwordmanager.data.Account
import com.linanqing.passwordmanager.ui.AppViewModelProvider
import com.linanqing.passwordmanager.utils.biometricUtils
import com.linanqing.passwordmanager.ui.navigation.NavigationDestination
import com.linanqing.passwordmanager.utils.BiometricCallback
import com.linanqing.passwordmanager.utils.promptInfo

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            AccountTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
                scrollBehavior = scrollBehavior,
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToItemEntry,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.account_entry_title)
                )
            }
        },
    ) { innerPadding ->

        HomeBody(
            accountList = homeUiState.accountList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun HomeBody(
    accountList: List<Account>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (accountList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_account_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 200.dp)
            )
        } else {
            val ctx = LocalContext.current
            AccountListUi(
                accountList = accountList,
                onItemClick = {
                    biometricUtils(ctx,object : BiometricCallback {
                        override fun success() {
                            onItemClick(it.id)
                        }

                        override fun error() {

                        }

                        override fun failed() {

                        }

                    }).authenticate(promptInfo)
                     },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun AccountListUi(
    accountList: List<Account>, onItemClick: (Account) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        items(items = accountList, key = {it.id}) { item: Account ->
            AccountItem(account = item,
                modifier = Modifier
                    .padding(dimensionResource(id = R.dimen.padding_small))
                    .clickable { onItemClick(item) })
        }
    }
}

@Composable
private fun AccountItem(
    account: Account, modifier: Modifier = Modifier
) {
        Row(
            modifier =modifier
        ) {
            // 应用图标
            AppIcon(
                str = account.name,
                0.dp,
                modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp)), shape = RoundedCornerShape(8.dp),
                iconClick = {}
            )
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier= Modifier
                        .height(40.dp)
                        .padding(start = 20.dp), verticalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = account.name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = account.account,
                            style = MaterialTheme.typography.bodyMedium
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
@Composable
fun AppIcon(str:String, shadowElevation: Dp, modifier: Modifier, shape: Shape, iconClick: ()-> Unit){
    // 避免出现负数
    val index = (str.hashCode() and Int.MAX_VALUE) % 20
    val ctx = LocalContext.current
    Surface(
        onClick = iconClick,
        shadowElevation = shadowElevation,
        modifier = modifier,
        color = Color(colorArray[index]),
        shape = shape,

    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            if (str.isNotEmpty()){
                Text(
                    text = str[0].toString(),
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,

                )
            }
        }


    }
}
