package com.linanqing.passwordmanager.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.linanqing.passwordmanager.ui.home.HomeScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.linanqing.passwordmanager.ui.account.AccountDetailsDestination
import com.linanqing.passwordmanager.ui.account.AccountDetailsScreen
import com.linanqing.passwordmanager.ui.account.AccountEditDestination
import com.linanqing.passwordmanager.ui.account.AccountEditScreen
import com.linanqing.passwordmanager.ui.account.AccountEntryDestination
import com.linanqing.passwordmanager.ui.account.AccountEntryScreen

import com.linanqing.passwordmanager.ui.home.HomeDestination

/**
 * Provides Navigation graph for the application.
 */
@Composable
fun AccountNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToItemEntry = { navController.navigate(AccountEntryDestination.route) },
                navigateToItemUpdate = {
                    navController.navigate("${AccountDetailsDestination.route}/${it}")
                }
            )
        }
        composable(route = AccountEntryDestination.route) {
            AccountEntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
            route = AccountDetailsDestination.routeWithArgs,
            arguments = listOf(navArgument(AccountDetailsDestination.itemIdArg) {
                type = NavType.IntType
            })
        ) {
            AccountDetailsScreen(
                navigateToEditItem = { navController.navigate("${AccountEditDestination.route}/$it") },
                navigateBack = { navController.navigateUp() }
            )
        }
        composable(
            route = AccountEditDestination.routeWithArgs,
            arguments = listOf(navArgument(AccountEditDestination.accountIdArg) {
                type = NavType.IntType
            })
        ) {
            AccountEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
