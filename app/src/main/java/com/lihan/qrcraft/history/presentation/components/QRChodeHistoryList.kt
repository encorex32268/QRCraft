package com.lihan.qrcraft.history.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt

@Composable
fun QRCodeHistoryList(
    items: List<QRCodeHistoryUi>,
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
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(
                items = items,
                key = { it.id }
            ) { item ->
                QRCodeHistoryItem(
                    modifier = Modifier.fillMaxWidth(),
                    type = item.type,
                    content = item.content,
                    timestamp = item.createdAt,
                    title = item.title,
                    onItemLongClick = { onItemLongClick(item.id)},
                    onItemClick = { onItemClick(item.id) }
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