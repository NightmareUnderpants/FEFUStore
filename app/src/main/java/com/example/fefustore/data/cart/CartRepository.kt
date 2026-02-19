package com.example.fefustore.data.cart

import androidx.compose.runtime.mutableStateListOf
import com.example.fefustore.data.model.CartItem
import com.example.fefustore.data.model.Product

object CartRepository {

    val items = mutableStateListOf<CartItem>()

    fun addToCart(product: Product, size: String) {
        val existing = items.find { it.product.id == product.id && it.size == size }

        if (existing != null) {
            val index = items.indexOf(existing)
            items[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            items.add(CartItem(product = product, size = size, quantity = 1))
        }
    }

    // ⬇️ уменьшить на 1 (и удалить строку, если стало 0)
    fun decrement(product: Product, size: String) {
        val existing = items.find { it.product.id == product.id && it.size == size } ?: return
        val index = items.indexOf(existing)
        val newQ = existing.quantity - 1
        if (newQ <= 0) items.removeAt(index)
        else items[index] = existing.copy(quantity = newQ)
    }

    // ⬇️ сколько всего этого товара в корзине (по всем размерам)
    fun totalQuantity(productId: String): Int =
        items.filter { it.product.id == productId }.sumOf { it.quantity }

    fun removeFromCart(cartItem: CartItem) {
        items.remove(cartItem)
    }
}

