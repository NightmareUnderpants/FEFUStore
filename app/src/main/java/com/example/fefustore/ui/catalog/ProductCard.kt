package com.example.fefustore.ui.catalog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.fefustore.R
import com.example.fefustore.data.model.Product
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.draw.clip

@Composable
fun ProductCard(
    product: Product,
    countInCart: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 📷 Картинка без обрезания
        Image(
            painter = painterResource(id = product.imageResId),
            contentDescription = product.title,
            modifier = Modifier
                .size(140.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column {
                Text(
                    text = product.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )

                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    maxLines = 2
                )
            }

            // 🔥 Блок цены или каунтера
            if (countInCart == 0) {
                PriceBox(
                    price = product.price,
                    onClick = onAdd
                )
            } else {
                CounterBox(
                    count = countInCart,
                    onAdd = onAdd,
                    onRemove = onRemove
                )
            }
        }
    }
}

@Composable
fun PriceBox(
    price: Int,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(color = Color(246, 239, 235, 1))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = "$price ₽",
            style = MaterialTheme.typography.titleMedium,
            color = Color(98, 58, 41, 1),
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun CounterBox(
    count: Int,
    onAdd: () -> Unit,
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(color = Color(0xFFE0E0E0)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "-",
            modifier = Modifier
                .clickable { onRemove() }
                .padding(8.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B564D)
        )

        Text(
            text = count.toString(),
            modifier = Modifier.padding(horizontal = 12.dp),
            color = Color(0xFF6B564D),
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "+",
            modifier = Modifier
                .clickable { onAdd() }
                .padding(8.dp),
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6B564D)
        )
    }
}
