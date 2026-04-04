@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.history.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.lihan.qrcraft.history.presentation.components.QRCodeHistoryList
import com.lihan.qrcraft.ui.theme.OnSurface
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.Outline
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import com.lihan.qrcraft.ui.theme.SetIsStatusBarsContentLightColor
import com.lihan.qrcraft.ui.theme.SurfaceHigher
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

private const val Scanned = 0
private const val Generated = 1

@Composable
fun ScanHistoryScreenRoot(
    navigateToPreview: (Long,String) -> Unit,
    viewModel: ScanHistoryViewModel = koinViewModel()
){
    SetIsStatusBarsContentLightColor(false)

    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()

    val screenTitle = stringResource(R.string.preview)

    ObserveAsEvents(viewModel.uiEvent) { uiEvent ->
        when(uiEvent){
            is ScanHistoryUiEvent.ShareQRCode -> {
                context.openShareSheet(
                    title = uiEvent.title,
                    text = uiEvent.content
                )
            }

            is ScanHistoryUiEvent.NavigateToPreview ->{
                navigateToPreview(uiEvent.id,screenTitle)
            }
        }
    }

    ScanHistoryScreen(
        state = state,
        onAction = viewModel::onAction
    )
}


@SuppressLint("ConfigurationScreenWidthHeight")
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

            val displayItems = if (page == Scanned) state.scannedItems else state.generatedItems

            QRCodeHistoryList(
                items = displayItems,
                onItemClick = {
                    onAction(ScanHistoryAction.ItemClick(it))
                },
                onItemLongClick = {
                    onAction(ScanHistoryAction.ItemLongClick(it))
                },
                onFavoriteClick = { id, isFavorite ->
                    onAction(ScanHistoryAction.ItemFavoriteClick(id,isFavorite))
                }
            )
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
                        isGenerated = false,
                        id = 1
                    ),
                    QRCodeHistoryUi(
                        type = QRCodeType.WiFi.type,
                        content = "Wifi content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false,
                        title = "Test",
                        id = 2
                    )
                ),
                generatedItems = listOf(
                    QRCodeHistoryUi(
                        type = QRCodeType.Geolocation.type,
                        content = "Geo content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false,
                        id = 3
                    ),
                    QRCodeHistoryUi(
                        type = QRCodeType.Link.type,
                        content = "Link content",
                        createdAt = 1750746960000L,
                        isFavorite = false,
                        isGenerated = false,
                        title = "Test",
                        id = 4
                    )
                )
            ),
            modifier = Modifier.fillMaxSize(),
            onAction = {}
        )
    }
}