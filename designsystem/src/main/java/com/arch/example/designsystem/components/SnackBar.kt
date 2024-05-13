package com.arch.example.designsystem.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.arch.example.translations.R

@Composable
fun SnackBar(
    text: String,
    modifier: Modifier = Modifier,
    buttonText: String? = null,
    onBtnClick: (() -> Unit)? = null
) {
    Snackbar(
        action = {
            onBtnClick?.let {
                Button(onClick = onBtnClick) {
                    Text(buttonText ?: stringResource(id = R.string.retry_text))
                }
            }
        },
        modifier = modifier.padding(8.dp)
    ) { Text(text = text) }
}