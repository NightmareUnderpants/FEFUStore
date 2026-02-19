package com.example.fefustore.ui.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.fefustore.data.cart.CartRepository
import com.example.fefustore.data.model.CartItem
import com.example.fefustore.ui.product.SuccessBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onCheckout: () -> Unit = {}
) {
    val items = CartRepository.items

    val total = items.sumOf { it.product.price * it.quantity }

    var showSuccessSheet by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF5F5F5),
        topBar = {
            Surface(
                color = Color.White,
                shadowElevation = 4.dp
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "Корзина",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            items.clear()
                        }) {
                            Icon(
                                imageVector = Icons.Outlined.Delete,
                                contentDescription = "Очистить",
                                tint = Color(0xFFB0B0B0),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomSummaryBar(
                total = total,
                isEmpty = items.isEmpty(),
                onCheckout = {
                    if (items.isNotEmpty()) {
                        showSuccessSheet = true
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF5F5F5))
        ) {
            if (items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Корзина пуста 🛒", color = Color(0xFF8E8E8E))
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = 8.dp, bottom = 120.dp)
                ) {
                    items(
                        items = items,
                        key = { it.product.id + "_" + it.size }
                    ) { cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onRemoveRow = { CartRepository.removeFromCart(cartItem) },
                            onPlus = { CartRepository.addToCart(cartItem.product, cartItem.size) },
                            onMinus = { CartRepository.decrement(cartItem.product, cartItem.size) }
                        )
                        Divider(color = Color(0xFFF0F0F0), thickness = 1.dp)
                    }
                }
            }
        }
    }

    if (showSuccessSheet) {
        SuccessBottomSheet(
            onDismiss = {
                showSuccessSheet = false
                CartRepository.items.clear()
                onCheckout()
            }
        )
    }
}

@Composable
private fun BottomSummaryBar(
    total: Int,
    isEmpty: Boolean,
    onCheckout: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Итого",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${formatPrice(total)} ₽",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isEmpty) Color(0xFFCCCCCC) else Color(0xFFA47764)
                ),
                enabled = !isEmpty
            ) {
                Text(
                    text = "Перейти к оформлению",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

public fun formatPrice(value: Int): String {
    // 17969 -> "17 969"
    val s = value.toString()
    val sb = StringBuilder()
    for (i in s.indices) {
        sb.append(s[i])
        val left = s.length - 1 - i
        if (left > 0 && left % 3 == 0) sb.append(' ')
    }
    return sb.toString()
}
