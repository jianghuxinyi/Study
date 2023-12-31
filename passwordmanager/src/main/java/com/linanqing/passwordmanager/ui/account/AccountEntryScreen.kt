/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linanqing.passwordmanager.ui.account

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.linanqing.passwordmanager.AccountTopAppBar
import com.linanqing.passwordmanager.R
import com.linanqing.passwordmanager.ui.AppViewModelProvider
import com.linanqing.passwordmanager.ui.home.AppIcon
import com.linanqing.passwordmanager.ui.navigation.NavigationDestination
import com.linanqing.passwordmanager.ui.theme.StudyTheme
import com.linanqing.passwordmanager.utils.BiometricCallback
import com.linanqing.passwordmanager.utils.biometricUtils
import com.linanqing.passwordmanager.utils.promptInfo
import kotlinx.coroutines.launch

object AccountEntryDestination : NavigationDestination {
    override val route = "account_entry"
    override val titleRes = R.string.account_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountEntryScreen(
    navController: NavHostController,
    navigateToAppEntry:() -> Unit,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: AccountEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val ctx = LocalContext.current
    navController.currentBackStackEntry?.savedStateHandle?.getLiveData<String>("icon")?.observe(
        LocalLifecycleOwner.current){
        viewModel.accountUiState.accountDetails.icon = it
    }

    Scaffold(
        topBar = {
            AccountTopAppBar(
                title = stringResource(AccountEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        AccountEntryBody(
            accountUiState = viewModel.accountUiState,
            onItemValueChange = viewModel::updateUiState,
            onSaveClick = {
                // Note: If the user rotates the screen very fast, the operation may get cancelled
                // and the item may not be saved in the Database. This is because when config
                // change occurs, the Activity will be recreated and the rememberCoroutineScope will
                // be cancelled - since the scope is bound to composition.
                biometricUtils(ctx,object : BiometricCallback {
                    override fun success() {
                        coroutineScope.launch {
                            viewModel.saveItem()
                            navigateBack()
                        }

                    }

                    override fun error() {

                    }

                    override fun failed() {

                    }

                }).authenticate(promptInfo)

            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            click = navigateToAppEntry
        )
    }
}

@Composable
fun AccountEntryBody(
    accountUiState: AccountUiState,
    onItemValueChange: (AccountDetails) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    click:() -> Unit
) {
    Column(
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large))
    ) {
        AccountInputForm(
            accountDetails = accountUiState.accountDetails,
            onValueChange = onItemValueChange,
            modifier = Modifier.fillMaxWidth(),
            click = click
        )
        Button(
            onClick = onSaveClick,
            enabled = accountUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.save_action))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountInputForm(
    accountDetails: AccountDetails,
    modifier: Modifier = Modifier,
    onValueChange: (AccountDetails) -> Unit = {},
    enabled: Boolean = true,
    click:() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {
        val ctx = LocalContext.current
        AppIcon(str = accountDetails.icon, shadowElevation = dimensionResource(id = R.dimen.padding_medium), modifier = Modifier.align(CenterHorizontally).size(96.dp), shape = CircleShape,
        iconClick = click)
        OutlinedTextField(
            value = accountDetails.name,
            onValueChange = { onValueChange(accountDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.account_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.account,
            onValueChange = { onValueChange(accountDetails.copy(account = it)) },
            label = { Text(stringResource(R.string.account_account_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.password,
            onValueChange = { onValueChange(accountDetails.copy(password = it)) },
            label = { Text(stringResource(R.string.account_password_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.email,
            onValueChange = { onValueChange(accountDetails.copy(email = it)) },
            label = { Text(stringResource(R.string.account_email_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.phone,
            onValueChange = { onValueChange(accountDetails.copy(phone = it)) },
            label = { Text(stringResource(R.string.account_phone_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.group,
            onValueChange = { onValueChange(accountDetails.copy(group = it)) },
            label = { Text(stringResource(R.string.account_group_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        OutlinedTextField(
            value = accountDetails.remark,
            onValueChange = { onValueChange(accountDetails.copy(remark = it)) },
            label = { Text(stringResource(R.string.account_remark_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ItemEntryScreenPreview() {
    StudyTheme {
        AccountEntryBody(accountUiState = AccountUiState(
            AccountDetails(
                name = "Item name",
                icon = "",
                account = "10.00",
                password = "5",
                email = "example@example.com",
                phone = "phone",
                group = "",
                remark = "remark"
            )
        ), onItemValueChange = {}, onSaveClick = {}, click = {})
    }
}
