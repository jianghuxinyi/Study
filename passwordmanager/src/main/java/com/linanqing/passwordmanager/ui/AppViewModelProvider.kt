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

package com.linanqing.passwordmanager.ui

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.linanqing.passwordmanager.AccountApplication
import com.linanqing.passwordmanager.ui.account.AccountDetailsViewModel
import com.linanqing.passwordmanager.ui.account.AccountEditViewModel
import com.linanqing.passwordmanager.ui.account.AccountEntryViewModel
import com.linanqing.passwordmanager.ui.app.AppListViewModel
import com.linanqing.passwordmanager.ui.home.HomeViewModel

/**
 * Provides Factory to create instance of ViewModel for the entire Inventory app
 */
object AppViewModelProvider {
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            AccountEditViewModel(
                this.createSavedStateHandle(),
                accountApplication().container.accountsRepository
            )
        }
        // Initializer for ItemEntryViewModel
        initializer {
            AccountEntryViewModel(accountApplication().container.accountsRepository)
        }

        initializer {
            AppListViewModel()
        }
        // Initializer for ItemDetailsViewModel
        initializer {
            AccountDetailsViewModel(
                this.createSavedStateHandle(),
                accountApplication().container.accountsRepository
            )
        }

        // Initializer for HomeViewModel
        initializer {
            HomeViewModel(accountApplication().container.accountsRepository)
        }
    }
}

/**
 * Extension function to queries for [Application] object and returns an instance of
 * [InventoryApplication].
 */
fun CreationExtras.accountApplication(): AccountApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AccountApplication)
