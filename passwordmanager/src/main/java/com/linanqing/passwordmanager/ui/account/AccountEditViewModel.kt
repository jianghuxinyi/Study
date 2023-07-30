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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linanqing.passwordmanager.data.AccountsRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * ViewModel to retrieve and update an item from the [ItemsRepository]'s data source.
 */
class AccountEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: AccountsRepository
) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var accountUiState by mutableStateOf(AccountUiState())
        private set

    private val accountId: Int = checkNotNull(savedStateHandle[AccountEditDestination.accountIdArg])

    init {
        viewModelScope.launch {
            accountUiState = itemsRepository.getAccountStream(accountId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    /**
     * Update the item in the [ItemsRepository]'s data source
     */
    suspend fun updateItem() {
        if (validateInput(accountUiState.accountDetails)) {
            itemsRepository.updateAccount(accountUiState.accountDetails.toAccount())
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: AccountDetails) {
        accountUiState =
            AccountUiState(accountDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: AccountDetails = accountUiState.accountDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && account.isNotBlank() && password.isNotBlank()
        }
    }
}
