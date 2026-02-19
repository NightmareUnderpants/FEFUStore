package com.example.fefustore.navigation

import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.*
import com.example.fefustore.ui.cart.CartScreen
import com.example.fefustore.ui.catalog.CatalogScreen
import com.example.fefustore.ui.components.BottomBar

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Catalog.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Catalog.route) {
                CatalogScreen()
            }
            composable(Screen.Cart.route) {
                CartScreen(
                    onCheckout = {
                        navController.navigate(Screen.Catalog.route) {
                            popUpTo(Screen.Catalog.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }

    }
}
