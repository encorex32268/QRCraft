package com.lihan.qrcraft.scan.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.ui.theme.Primary
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun ScanFrame(
    modifier: Modifier = Modifier
) {

    val density = LocalDensity.current
    val cornerRadius = 18.dp
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    val borderStrokeWidth = 4.dp
    val borderStrokeWidthPx = with(density) { borderStrokeWidth.toPx() }
    val length = with(density){ 50.dp.toPx() }
    Canvas(
        modifier = modifier.fillMaxWidth()
    ) {
        drawQRCodeFrame(
            length = length,
            cornerRadiusPx = cornerRadiusPx,
            borderStrokeWidthPx = borderStrokeWidthPx
        )
    }



}

fun DrawScope.drawQRCodeFrame(
    length: Float,
    cornerRadiusPx: Float,
    borderStrokeWidthPx: Float
){
    val path = Path().apply {
        moveTo(x = 0f, y = length)
        arcTo(
            rect = Rect(
                left = 0f,
                top = 0f,
                right = cornerRadiusPx,
                bottom = cornerRadiusPx,
            ),
            startAngleDegrees = 180f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(x = length , y = 0f)

        moveTo(x = size.width - length, y = 0f)
        arcTo(
            rect = Rect(
                left = size.width - cornerRadiusPx,
                top = 0f,
                right = size.width,
                bottom = cornerRadiusPx
            ),
            startAngleDegrees = 270f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(x = size.width , y = length)


        moveTo(x = size.width, y = size.height - length)
        arcTo(
            rect = Rect(
                left = size.width - cornerRadiusPx,
                top = size.height - cornerRadiusPx,
                right = size.width,
                bottom = size.height
            ),
            startAngleDegrees = 0f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(x = size.width - length , y = size.height)

        moveTo(x = length, y = size.height)
        arcTo(
            rect = Rect(
                left = 0f,
                top = size.height - cornerRadiusPx,
                right = cornerRadiusPx,
                bottom = size.height
            ),
            startAngleDegrees = 90f,
            sweepAngleDegrees = 90f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = size.height - length)
    }

    drawPath(
        path = path,
        color = Primary,
        style = Stroke(
            width = borderStrokeWidthPx
        )
    )
}


@Preview(showBackground = false)
@Composable
private fun ScanFramePreview() {
    QRCraftTheme {
        ScanFrame(
            modifier = Modifier
                .size(300.dp)
                .padding(16.dp)
        )
    }
}