package com.linanqing.passwordmanager.data

import android.content.Context

interface AppContainer {
    val accountsRepository: AccountsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val accountsRepository: AccountsRepository by lazy {
        OfflineAccountsRepository(AccountDatabase.getDatabase(context).accountDao())
    }
}