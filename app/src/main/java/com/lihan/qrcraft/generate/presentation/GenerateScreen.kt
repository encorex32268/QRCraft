@file:OptIn(ExperimentalMaterial3Api::class)

package com.lihan.qrcraft.generate.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.R
import com.lihan.qrcraft.core.domain.QRCodeType
import com.lihan.qrcraft.generate.presentation.components.GenerateTypeCard
import com.lihan.qrcraft.generate.presentation.model.QRCodeTypeUi
import com.lihan.qrcraft.generate.presentation.model.toQRCodeTypeUi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import com.lihan.qrcraft.core.presentation.components.MainAdaptiveLayout
import com.lihan.qrcraft.core.presentation.navigation.TopLevelDestination
import com.lihan.qrcraft.core.ui.theme.QRCraftTheme

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun GenerateScreen(
    onItemClick: (QRCodeTypeUi) -> Unit,
    modifier: Modifier = Modifier,
    onNavigateToDestination: (TopLevelDestination) -> Unit = {}
) {
    val isWideDevice = LocalConfiguration.current.screenWidthDp >= 600

    val items = QRCodeType.entries.map { it.toQRCodeTypeUi() }

    MainAdaptiveLayout(
        selectedDestination = TopLevelDestination.GENERATE,
        onNavigateToDestination = onNavigateToDestination,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.create_qr),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
            Spacer(Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(if (isWideDevice) 3 else 2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(items){ qrCodeTypeUi ->
                    GenerateTypeCard(
                        text = stringResource(qrCodeTypeUi.stringResId),
                        imageVector = ImageVector.vectorResource(qrCodeTypeUi.iconResId),
                        iconTintColor = qrCodeTypeUi.iconTintColor,
                        iconBackgroundColor = qrCodeTypeUi.iconBackgroundColor,
                        onItemClick = {
                            onItemClick(qrCodeTypeUi)
                        }
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
private fun GenerateScreenPreview() {
    QRCraftTheme {
        GenerateScreen(
            onItemClick = {}
        )
    }
}