package com.example.fefustore.ui.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fefustore.data.cart.CartRepository
import com.example.fefustore.data.mock.mockProducts
import com.example.fefustore.data.model.Product
import com.example.fefustore.ui.product.ProductBottomSheet

@Composable
fun CatalogScreen(
    onOpenCart: () -> Unit = {}
) {
    val categories = listOf("Новинки", "Джинсы", "Футболки")
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    // якорь, чтобы экран реагировал на изменения корзины
    val sizes = CartRepository.items.size

    var selectedProduct by remember { mutableStateOf<Product?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {

        Spacer(Modifier.height(8.dp))

        CategoryChipsRow(
            categories = categories,
            selected = selectedCategory,
            onSelect = { selectedCategory = it }
        )

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 90.dp)
        ) {
            items(
                items = mockProducts,
                key = { it.id }
            ) { product ->

                val count = CartRepository.totalQuantity(product.id)
                val defaultSize = "M" // пока так

                ProductRow(
                    product = product,
                    countInCart = count,
                    onAdd = { CartRepository.addToCart(product, defaultSize) },
                    onRemove = { CartRepository.decrement(product, defaultSize) },
                    onClick = { selectedProduct = product }
                )
            }
        }
    }

    selectedProduct?.let { product ->
        ProductBottomSheet(
            product = product,
            onDismiss = { selectedProduct = null }
        )
    }
}

@Composable
private fun CategoryChipsRow(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .horizontalScroll(rememberScrollState()),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { title ->
            val isSelected = title == selected

            Surface(
                shape = RoundedCornerShape(18.dp),
                color = if (isSelected) Color(0xFF6B564D) else Color(0xFFF2F2F2),
                modifier = Modifier
                    .padding(end = 10.dp)
                    .height(34.dp),
                onClick = { onSelect(title) }
            ) {
                Box(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title,
                        color = if (isSelected) Color.White else Color(0xFF2B2B2B)
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductRow(
    product: Product,
    countInCart: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(140.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        ProductImage(product = product)

        Spacer(Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = product.description,
                color = Color(0xFF9A9A9A),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )

            Spacer(Modifier.height(12.dp))

            // плашка снизу под текстом
            if (countInCart == 0) {
                PricePill(price = product.price, onClick = onAdd)
            } else {
                CounterPill(
                    count = countInCart,
                    onMinus = onRemove,
                    onPlus = onAdd
                )
            }
        }
    }
}

@Composable
private fun ProductImage(product: Product) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.Transparent
    ) {
        androidx.compose.foundation.Image(
            painter = androidx.compose.ui.res.painterResource(id = product.imageResId),
            contentDescription = product.title,
            modifier = Modifier.size(110.dp),
            contentScale = androidx.compose.ui.layout.ContentScale.Fit
        )
    }
}

@Composable
private fun PricePill(
    price: Int,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFF6EFEB)
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "${formatPrice(price)} ₽",
                color = Color(0xFF623A29),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CounterPill(
    count: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = Color(0xFFE0E0E0)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "−",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .wrapContentSize()
                    .then(Modifier),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B564D)
            )
            // кликабельность отдельно, чтобы было как кнопки
            Spacer(Modifier.width(2.dp))
            Text(
                text = "−",
                modifier = Modifier
                    .clickable { onMinus() }
                    .padding(horizontal = 2.dp),
                color = Color.Transparent
            )

            Text(
                text = count.toString(),
                modifier = Modifier.padding(horizontal = 10.dp),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF6B564D)
            )

            Text(
                text = "+",
                modifier = Modifier
                    .clickable { onPlus() }
                    .padding(horizontal = 8.dp),
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6B564D)
            )
        }
    }
}

private fun formatPrice(value: Int): String {
    val s = value.toString()
    val sb = StringBuilder()
    for (i in s.indices) {
        sb.append(s[i])
        val left = s.length - 1 - i
        if (left > 0 && left % 3 == 0) sb.append(' ')
    }
    return sb.toString()
}

