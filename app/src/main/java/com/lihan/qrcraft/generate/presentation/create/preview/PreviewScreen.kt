@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.generate.presentation.create.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.core.presentation.components.ScanResultCard
import com.lihan.qrcraft.core.presentation.util.openShareSheet
import com.lihan.qrcraft.scan.presentation.result.ScanResultAction
import com.lihan.qrcraft.ui.theme.OnOverlay
import com.lihan.qrcraft.ui.theme.QRCraftTheme
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun PreviewScreenRoot(
    onBack: () -> Unit,
    viewModel: PreviewViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    PreviewScreen(
        state = state,
        onAction = { action ->
            when(action){
                PreviewAction.BackClick -> onBack()
                PreviewAction.ShareClick -> {
                    context.openShareSheet(
                        title = QRCodeType.getQRCodeType(state.type).name,
                        text = state.content)
                }
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun PreviewScreen(
    state: PreviewState,
    onAction: (PreviewAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface)
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.preview),
                    style = MaterialTheme.typography.titleMedium,
                    color = OnOverlay
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        onAction(PreviewAction.BackClick)
                    }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.arrow_left),
                        contentDescription = null,
                        tint = OnOverlay
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )
        Spacer(Modifier.height(28.dp))
        if (state.type != null){
            ScanResultCard(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .widthIn(max = 480.dp)
                    .padding(16.dp),
                type = state.type,
                content = state.content,
                onShare = {
                    onAction(PreviewAction.ShareClick)
                },
                onCopy = {
                    onAction(PreviewAction.CopyClick)
                }
            )
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun PreviewScreenPreview() {
    QRCraftTheme {
        PreviewScreen(
            state = PreviewState(
                type = QRCodeType.Text.type,
                content = ""
            ),
            onAction = {}
        )
    }
}