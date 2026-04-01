package com.lihan.qrcraft.generate.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lihan.qrcraft.ui.theme.OnSurface
import com.lihan.qrcraft.ui.theme.OnSurfaceAlt
import com.lihan.qrcraft.ui.theme.QRCraftTheme

@Composable
fun CreateQRTextField(
    state: TextFieldState,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = keyboardType
    ),
    keyboardActionHandler: KeyboardActionHandler? = null
) {
    BasicTextField(
        state = state,
        decorator = { innerField ->
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterStart
            ){
                if (state.text.toString().isEmpty()){
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = OnSurfaceAlt
                    )
                }
                innerField()
            }
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(color = OnSurface),
        keyboardOptions = keyboardOptions,
        onKeyboardAction = keyboardActionHandler,
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(
                color = MaterialTheme.colorScheme.surface,
                RoundedCornerShape(20.dp)
            )
            .fillMaxWidth()
            .padding(vertical = 18.dp, horizontal = 16.dp)
    )

}


@Preview(showBackground = true)
@Composable
private fun CreateQRTextFieldPreview() {
    QRCraftTheme {
        CreateQRTextField(
            state = TextFieldState(
                initialText = ""
            ),
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth()
        )
    }
}