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
import androidx.lifecycle.ViewModel
import com.linanqing.passwordmanager.data.Account
import com.linanqing.passwordmanager.data.AccountsRepository

/**
 * ViewModel to validate and insert items in the Room database.
 */
class AccountEntryViewModel(private val accountsRepository: AccountsRepository) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var accountUiState by mutableStateOf(AccountUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(accountDetails: AccountDetails) {
        accountUiState =
            AccountUiState(accountDetails = accountDetails, isEntryValid = validateInput(accountDetails))
    }

    /**
     * Inserts an [Item] in the Room database
     */
    suspend fun saveItem() {
        if (validateInput()) {
            accountsRepository.insertAccount(accountUiState.accountDetails.toAccount())
        }
    }

    private fun validateInput(uiState: AccountDetails = accountUiState.accountDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && account.isNotBlank() && password.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class AccountUiState(
    val accountDetails: AccountDetails = AccountDetails(),
    val isEntryValid: Boolean = false
)

data class AccountDetails(
    val id: Int = 0,
    val name: String = "",
    val account: String = "",
    val password: String = "",
    val email: String = "",
    val phone: String = "",
    val remark: String = "",

)

/**
 * Extension function to convert [ItemUiState] to [Item]. If the value of [ItemDetails.price] is
 * not a valid [Double], then the price will be set to 0.0. Similarly if the value of
 * [ItemUiState] is not a valid [Int], then the quantity will be set to 0
 */
fun AccountDetails.toAccount(): Account = Account(
    id = id,
    name = name,
    account = account,
    password = password,
    email = email,
    phone = phone,
    remark = remark,
)

/**
 * Extension function to convert [Item] to [ItemUiState]
 */
fun Account.toItemUiState(isEntryValid: Boolean = false): AccountUiState = AccountUiState(
    accountDetails = this.toAccountDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Item] to [ItemDetails]
 */
fun Account.toAccountDetails(): AccountDetails = AccountDetails(
    id = id,
    name = name,
    account = account,
    password = password,
    email = email,
    phone = phone,
    remark = remark,
)
