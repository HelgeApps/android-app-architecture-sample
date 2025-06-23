package com.arch.example.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arch.example.designsystem.theme.AppTheme

@Composable
fun AppRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    selected: Boolean,
    onSelected: () -> Unit,
) {
    Row(
        modifier
            .clip(RoundedCornerShape(12.dp))
            .selectable(
                selected = selected,
                onClick = onSelected
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelected,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier
                .padding(end = 16.dp)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
        )
    }
}

@Composable
@Preview(heightDp = 100, widthDp = 200)
@Preview(heightDp = 100, widthDp = 200, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AppRadioButtonPreview() {
    AppTheme {
        Surface {
            AppRadioButton(
                text = "Choice 1",
                selected = false,
                onSelected = {}
            )
        }
    }
}

@Composable
@Preview(heightDp = 100, widthDp = 200)
@Preview(heightDp = 100, widthDp = 200, uiMode = Configuration.UI_MODE_NIGHT_YES)
private fun AppRadioButtonSelectedPreview() {
    AppTheme {
        Surface {
            AppRadioButton(
                text = "Choice 1",
                selected = true,
                onSelected = {}
            )
        }
    }
}