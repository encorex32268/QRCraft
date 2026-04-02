@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.history.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.Modifier.Companion
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.design_system.buttons.QRCraftButton
import com.lihan.qrcraft.core.presentation.model.QRCodeHistoryUi
import com.lihan.qrcraft.core.presentation.util.ObserveAsEvents
import com.lihan.qrcraft.core.presentation.util.openShareSheet
import com.lihan.qrcraft.history.presentation.components.QRCodeHistoryItem
import com.lihan.qrcraft.ui.theme.OnSurface
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.Outline
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SurfaceHigher
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private const val Scanned = 0
private const val Generated = 1

@Composable
fun ScanHistoryScreenRoot(
    viewModel: ScanHistoryViewModel = koinViewModel()
){
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.uiEvent) { uiEvent ->
        when(uiEvent){
            is ScanHistoryUiEvent.ShareQRCode -> {
                context.openShareSheet(
                    title = uiEvent.title,
                    text = uiEvent.content
                )
            }
        }
    }

    ScanHistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}


@Composable
private fun ScanHistoryScreen(
    state: ScanHistoryState,
    onAction: (ScanHistoryAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val isWideDevice = LocalConfiguration.current.screenWidthDp >= 600

    val scope = rememberCoroutineScope()

    val pagerState = rememberPagerState(
        initialPage = Scanned,
        pageCount = { 2 }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.scan_history),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {
                HorizontalDivider(color = Outline)
            },
            indicator = {
                val tabPosition = it[pagerState.currentPage]
                SecondaryIndicator(
                    modifier = Modifier
                        .tabIndicatorOffset(tabPosition)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .wrapContentSize(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(100))
                        .width(tabPosition.width),
                    color = MaterialTheme.colorScheme.onSurface,
                    height = 2.dp
                )
            },
            tabs = {
                Tab(
                    selected = pagerState.currentPage == Scanned,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(Scanned)
                        }
                    },
                    selectedContentColor = OnSurface,
                    unselectedContentColor = OnSurfaceAlt,
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(R.string.scanned),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Tab(
                    selected = pagerState.currentPage == Generated,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(Generated)
                        }
                    },
                    selectedContentColor = OnSurface,
                    unselectedContentColor = OnSurfaceAlt
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 16.dp),
                        text = stringResource(R.string.generated),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        )
        HorizontalPager(
            state =  pagerState,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) { page ->
            when(page){
                Scanned -> {
                    if (state.scannedItems.isEmpty()){
                        EmptyView(text = stringResource(R.string.history_empty))
                    }else{
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.scannedItems){ items ->
                                QRCodeHistoryItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    type = items.type,
                                    content = items.content,
                                    timestamp = items.createdAt,
                                    title = items.title,
                                    onLongClick = {
                                        onAction(ScanHistoryAction.ItemLongClick(items.id))
                                    }
                                )
                            }
                        }
                    }
                }
                Generated -> {
                    if (state.generatedItems.isEmpty()){
                        EmptyView(text = stringResource(R.string.history_empty))
                    }else{
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(state.generatedItems){ items ->
                                QRCodeHistoryItem(
                                    modifier = Modifier.fillMaxWidth(),
                                    type = items.type,
                                    content = items.content,
                                    timestamp = items.createdAt,
                                    title = items.title,
                                    onLongClick = {
                                        onAction(ScanHistoryAction.ItemLongClick(items.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

    }

    if (state.isShowEditorBottomSheet){
        ModalBottomSheet(
            modifier = Modifier
                .then(
                    if (isWideDevice) {
                        Modifier.widthIn(max = 412.dp)
                    }else Modifier
                )
                .fillMaxWidth(),
            onDismissRequest = {},
            dragHandle = null,
            scrimColor = Color.Black.copy(alpha = 0.32f),
            containerColor = SurfaceHigher,
            shape = RoundedCornerShape(topStart = 16.dp , topEnd = 16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                QRCraftButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.share),
                    textColor = MaterialTheme.colorScheme.onSurface,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.share),
                            tint = MaterialTheme.colorScheme.onSurface,
                            contentDescription = stringResource(R.string.share)
                        )
                    },
                    onClick = {
                        onAction(ScanHistoryAction.ShareClick)
                    }
                )
                QRCraftButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(R.string.delete),
                    textColor = MaterialTheme.colorScheme.error,
                    leadingIcon = {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.trash),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = stringResource(R.string.delete)
                        )
                    },
                    onClick = {
                        onAction(ScanHistoryAction.DeleteClick)
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

@Preview
@Composable
private fun ScanHistoryScreenPreview() {
    QRCraftTheme {
        ScanHistoryScreen(
            state = ScanHistoryState(
                isShowEditorBottomSheet = false,
                scannedItems = listOf(
                    QRCodeHistoryUi(
                        type = QRCodeType.WiFi.type,
                        content = "Wifi content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false
                    ),
                    QRCodeHistoryUi(
                        type = QRCodeType.WiFi.type,
                        content = "Wifi content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false,
                        title = "Test"
                    )
                ),
                generatedItems = listOf(
                    QRCodeHistoryUi(
                        type = QRCodeType.Geolocation.type,
                        content = "Geo content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false
                    ),
                    QRCodeHistoryUi(
                        type = QRCodeType.Link.type,
                        content = "Link content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false,
                        title = "Test"
                    )
                )
            ),
            modifier = Modifier.fillMaxSize(),
            onAction = {}
        )
    }
}