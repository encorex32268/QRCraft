@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.history.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.lihan.qrcraft.R
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun ScanHistoryScreen(
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 2 }
    )
    Column(
        modifier = modifier
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
            tabs = {
                Tab(
                    selected = true,
                    onClick = {}
                ) {
                    Text("Tab1")
                }
                Tab(
                    selected = true,
                    onClick = {}
                ) {
                    Text("Tab2")
                }
            }
        )
        HorizontalPager(
            state =  pagerState
        ) { page ->
            when(page){
                0 -> {

                }
                1 -> {

                }
            }
        }

    }
}

@Preview
@Composable
private fun ScanHistoryScreenPreview() {
    QRCraftTheme {
        ScanHistoryScreen()
    }
}