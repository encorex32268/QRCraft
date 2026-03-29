package com.lihan.qrcraft.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.lihan.qrcraft.ui.theme.Link
import com.lihan.qrcraft.ui.theme.LinkBG
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun TextLinkButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(
            modifier = Modifier.background(color = LinkBG),
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Link
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun TextLinkButtonPreview() {
    QRCraftTheme {
        TextLinkButton(
            text = "https://www.google.com/maps",
            onClick = {

            }
        )
    }
}