package com.arch.example.designsystem.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.arch.example.designsystem.theme.AppTheme
import java.io.Serializable

@Composable
fun AppDialog(
    title: String,
    subtitle: String? = null,
    positiveBtnText: String = stringResource(id = android.R.string.ok),
    negativeBtnText: String? = null,
    neutralBtnText: String? = null,
    onDismiss: () -> Unit = {},
    onPositiveBtnClicked: () -> Unit = {},
    onNegativeBtnClicked: () -> Unit = {},
    onNeutralBtnClicked: () -> Unit = {},
    usePlatformDefaultWidth: Boolean = true,
    content: @Composable (ColumnScope.() -> Unit)? = null
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = usePlatformDefaultWidth
        )
    ) {
        Surface(
            shape = RoundedCornerShape(size = 16.dp),
            color = MaterialTheme.colorScheme.background,
        ) {
            Column(modifier = Modifier.padding(all = 16.dp)) {
                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()) // scroll text, but not dialog buttons
                        .then(
                            if (content == null) {
                                Modifier.weight(1f, fill = false)
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.labelLarge,
                    )
                    subtitle?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
                content?.invoke(this)
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(
                    modifier = Modifier.align(Alignment.End),
                    horizontalArrangement = Arrangement.End,
                ) {
                    neutralBtnText?.let {
                        AppTextDialogButton(
                            text = neutralBtnText,
                            onClick = {
                                onNeutralBtnClicked()
                                onDismiss()
                            },
                        )
                    }
                    negativeBtnText?.let {
                        AppTextDialogButton(
                            text = negativeBtnText,
                            onClick = {
                                onNegativeBtnClicked()
                                onDismiss()
                            },
                        )
                    }
                    AppTextDialogButton(
                        text = positiveBtnText,
                        onClick = {
                            onPositiveBtnClicked()
                            onDismiss()
                        },
                    )
                }
            }
        }
    }
}


@Composable
fun <T : Serializable> AppDialogSingleChoice(
    title: String,
    subtitle: String? = null,
    positiveBtnText: String = stringResource(id = android.R.string.ok),
    negativeBtnText: String? = null,
    onDismiss: () -> Unit = {},
    onPositiveBtnClicked: (T) -> Unit = {},
    onNegativeBtnClicked: () -> Unit = {},
    onSelected: (T) -> Unit = {},
    options: List<T>,
    optionsTitles: List<String>,
    selectedOption: T? = null,
) {
    var chosenOption by rememberSaveable(selectedOption) { mutableStateOf(selectedOption) }
    AppDialog(
        title = title,
        subtitle = subtitle,
        positiveBtnText = positiveBtnText,
        negativeBtnText = negativeBtnText,
        onPositiveBtnClicked = {
            chosenOption?.let {
                onPositiveBtnClicked(it)
            }
        },
        onNegativeBtnClicked = onNegativeBtnClicked,
        onDismiss = onDismiss,
        content = {
            Spacer(modifier = Modifier.height(16.dp))
            val initialFirstVisibleItemIndex = remember {
                selectedOption?.let { options.indexOf(selectedOption).takeIf { it != -1 } } ?: 0
            }
            val state = rememberLazyListState(
                initialFirstVisibleItemIndex = initialFirstVisibleItemIndex
            )
            if (state.canScrollBackward) {
                AppHorizontalDivider()
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }
            LazyColumn(
                state = state,
                modifier = Modifier
                    .selectableGroup()
                    .fillMaxWidth()
                    .weight(1f, fill = false)
            ) {
                itemsIndexed(options) { idx, item ->
                    AppRadioButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = optionsTitles.getOrNull(idx) ?: "",
                        selected = item == chosenOption,
                        onSelected = {
                            if (item != chosenOption) {
                                chosenOption = item
                                onSelected(item)
                            }
                        },
                    )
                }
            }
            if (state.canScrollForward) {
                AppHorizontalDivider()
            } else {
                Spacer(modifier = Modifier.height(1.dp))
            }
        }
    )
}


@Composable
@Preview(heightDp = 400)
@Preview(heightDp = 400, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AppDialogPreview() {
    AppTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            AppDialog(
                title = "Title",
                subtitle = "Subtitle.",
                positiveBtnText = "OK",
                negativeBtnText = "Cancel",
                onDismiss = {}
            )
        }
    }
}

@Composable
@Preview(heightDp = 400)
@Preview(heightDp = 400, uiMode = Configuration.UI_MODE_NIGHT_YES)
fun AppDialogSingleChoicePreview() {
    AppTheme {
        Surface(modifier = Modifier.padding(24.dp)) {
            val options = listOf(
                "First choice",
                "Second choice"
            )
            AppDialogSingleChoice(
                title = "Select",
                negativeBtnText = "Cancel",
                options = options,
                optionsTitles = options,
                selectedOption = options[1]
            )
        }
    }
}