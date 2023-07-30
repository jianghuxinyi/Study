package com.linanqing.passwordmanager.ui.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linanqing.passwordmanager.AccountTopAppBar
import com.linanqing.passwordmanager.R
import com.linanqing.passwordmanager.data.Account
import com.linanqing.passwordmanager.ui.AppViewModelProvider
import com.linanqing.passwordmanager.ui.navigation.NavigationDestination

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
                .background(Color(0xFFF0F2F4))
        )
    }
}

@Composable
private fun HomeBody(
    accountList: List<Account>, onItemClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(10.dp)
    ) {
        if (accountList.isEmpty()) {
            Text(
                text = stringResource(R.string.no_account_description),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 200.dp)
            )
        } else {
            AccountListUi(
                accountList = accountList,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

@Composable
private fun AccountListUi(
    accountList: List<Account>, onItemClick: (Account) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier.background(Color(0xFFFEFEFE), RoundedCornerShape(8.dp)).padding(dimensionResource(id = R.dimen.padding_small))) {
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
            Image(painter = painterResource(id = R.drawable.test), contentDescription = "应用",
            modifier=Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)))
            Column {
                Row(modifier = Modifier.fillMaxWidth().height(40.dp),horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier=Modifier.height(40.dp).padding(start = 20.dp), verticalArrangement = Arrangement.SpaceBetween) {
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
                        modifier=Modifier.size(24.dp).align(Alignment.CenterVertically))
                }
                // 底部分割线
                Divider(thickness = 0.5.dp, color = Color.Gray, modifier = Modifier.padding(start = 20.dp,top=20.dp))
            }

        }

//    Card(
//        modifier = modifier, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large)),
//            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
//        ) {
//            Row(
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Text(
//                    text = account.name,
//                    style = MaterialTheme.typography.titleLarge,
//                )
//                Spacer(Modifier.weight(1f))
//                Text(
//                    text = account.account,
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//            Text(
//                text = account.remark,
//                style = MaterialTheme.typography.titleMedium
//            )
//        }
//    }
}
