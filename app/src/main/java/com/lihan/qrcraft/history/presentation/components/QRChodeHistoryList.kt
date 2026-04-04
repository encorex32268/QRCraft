package com.lihan.qrcraft.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun QRCodeHistoryList(
    items: List<QRCodeHistoryUi>,
    onFavoriteClick: (Long,Boolean) -> Unit,
    onItemLongClick: (Long) -> Unit,
    onItemClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    if (items.isEmpty()) {
        EmptyView(
            text = stringResource(R.string.history_empty),
            modifier = modifier.fillMaxSize()
        )
    } else {

        val listState = rememberLazyListState()
        val scope = rememberCoroutineScope()

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            0f to Color.Black,
                            0.75f to Color.Black,
                            1f to Color.Transparent
                        ),
                        blendMode = BlendMode.DstIn)
                },
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp),

        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                QRCodeHistoryItem(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(),
                    type = item.type,
                    content = item.content,
                    timestamp = item.createdAt,
                    title = item.title,
                    isFavorite = item.isFavorite,
                    onItemLongClick = { onItemLongClick(item.id)},
                    onItemClick = { onItemClick(item.id) },
                    onFavoriteClick = {
                        onFavoriteClick(
                            item.id,
                            item.isFavorite
                        )
                        scope.launch {
                            listState.animateScrollToItem(0)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun EmptyView(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = OnSurfaceAlt
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QRCodeHistoryListPreview() {
    QRCraftTheme {
        QRCodeHistoryList(
            items = (0..20).map {
                QRCodeHistoryUi(
                    id = it.toLong(),
                    title = "Google Maps",
                    type = QRCodeType.entries.random().type,
                    content = "Content ${it}",
                    createdAt = System.currentTimeMillis(),
                    isGenerated = false,
                    isFavorite = Random.nextBoolean()
                )
            },
            onFavoriteClick = { _, _ -> },
            onItemLongClick = {},
            onItemClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QRCodeHistoryListEmptyPreview() {
    QRCraftTheme {
        QRCodeHistoryList(
            items = emptyList(),
            onFavoriteClick = { _, _ -> },
            onItemLongClick = {},
            onItemClick = {}
        )
    }
}