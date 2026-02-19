package com.example.fefustore.navigation

sealed class Screen(val route: String, val title: String) {
    object Catalog : Screen("catalog", "Товары")
    object Cart : Screen("cart", "Корзина")
    object Product : Screen("product", "Товар")
}
