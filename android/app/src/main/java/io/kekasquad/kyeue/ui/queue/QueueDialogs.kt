package io.kekasquad.kyeue.ui.queue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.ui.components.KyueuTextField
import io.kekasquad.kyeue.utils.stringResourceOrNull
import io.kekasquad.kyeue.vo.inapp.Queue

@Composable
fun CreateQueueDialog(
    queueName: String,
    onQueueNameChange: (String) -> Unit,
    errorText: Int,
    onQueueCreate: () -> Unit,
    onQueueCreateDismiss: () -> Unit,
) {
    Dialog(onDismissRequest = { onQueueCreateDismiss() }) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 24.dp, end = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = stringResource(R.string.text_dialog_title_create_queue),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )

                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    value = queueName,
                    onValueChange = onQueueNameChange,
                    label = stringResource(R.string.hint_queue_name),
                    error = stringResourceOrNull(id = errorText)
                )

                Row {
                    DismissButton(onQueueCreateDismiss)
                    ConfirmButton(
                        text = stringResource(R.string.text_button_create_queue),
                        onClick = onQueueCreate
                    )
                }
            }
        }
    }
}

@Composable
fun RenameQueueDialog(
    queue: Queue,
    queueName: String,
    onQueueNameChange: (String) -> Unit,
    errorText: Int,
    onQueueRename: () -> Unit,
    onQueueRenameDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { onQueueRenameDismiss() }
    ) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column(
                modifier = Modifier
                    .padding(bottom = 8.dp, start = 24.dp, end = 24.dp, top = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    text = stringResource(R.string.text_dialog_title_rename_queue),
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )

                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    value = queueName,
                    onValueChange = onQueueNameChange,
                    label = stringResource(R.string.hint_queue_name),
                    error = stringResourceOrNull(id = errorText)
                )

                Row {
                    DismissButton(onQueueRenameDismiss)
                    ConfirmButton(
                        text = stringResource(R.string.text_button_rename_queue),
                        onClick = onQueueRename
                    )
                }
            }
        }
    }
}

@Composable
fun DeleteQueueDialog(
    queue: Queue,
    onQueueDelete: () -> Unit,
    onQueueDeleteDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onQueueDeleteDismiss() },
        title = {
            Text(
                text = stringResource(R.string.text_dialog_title_delete_queue),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = R.string.text_dialog_desc_delete_queue,
                    queue.name
                ),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        },
        confirmButton = {
            ConfirmButton(
                text = stringResource(R.string.text_button_delete_queue),
                onClick = onQueueDelete
            )
        },
        dismissButton = { DismissButton(onQueueDeleteDismiss) }
    )
}

@Composable
private fun ConfirmButton(
    text: String,
    onClick: () -> Unit
) {
    TextButton(
        modifier = Modifier.padding(start = 16.dp),
        onClick = { onClick() }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DismissButton(onClick: () -> Unit) {
    TextButton(
        onClick = { onClick() }
    ) {
        Text(
            text = stringResource(R.string.text_button_dismiss_dialog),
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}