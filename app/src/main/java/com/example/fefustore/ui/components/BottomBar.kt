package com.example.fefustore.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fefustore.navigation.Screen

@Composable
fun BottomBar(navController: NavController) {

    val items = listOf(
        Screen.Catalog to Icons.Default.Home,
        Screen.Cart to Icons.Default.ShoppingCart
    )

    NavigationBar (
        containerColor = Color.White,
        tonalElevation = 0.dp
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry.value?.destination?.route

        items.forEach { (screen, icon) ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Catalog.route)
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        icon,
                        contentDescription = screen.title,
                        modifier = Modifier.padding(8.dp)
                    )
                },
                label = {
                    Text(
                        text = screen.title,
                        color = if (selected) Color(0xFFA47764) else Color(0xFF9A9A9A)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color(0xFFA47764),
                    unselectedIconColor = Color(0xFF9A9A9A),
                    selectedTextColor = Color(0xFFA47764),
                    unselectedTextColor = Color(0xFF9A9A9A),
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
