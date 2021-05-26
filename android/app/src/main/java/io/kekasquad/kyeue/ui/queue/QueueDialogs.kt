package io.kekasquad.kyeue.ui.queue

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import io.kekasquad.kyeue.ui.components.KyueuTextField
import io.kekasquad.kyeue.ui.theme.KyeueTheme
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
                    text = "Create queue",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )

                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    value = queueName,
                    onValueChange = onQueueNameChange,
                    label = "Name",
                    error = stringResourceOrNull(id = errorText)
                )

                Row {
                    DismissButton(onQueueCreateDismiss)
                    ConfirmButton(text = "Create", onClick = onQueueCreate)
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
                    text = "Rename queue",
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.onSurface
                )

                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    value = queueName,
                    onValueChange = onQueueNameChange,
                    label = "Name",
                    error = stringResourceOrNull(id = errorText)
                )

                Row {
                    DismissButton(onQueueRenameDismiss)
                    ConfirmButton(text = "Rename") { onQueueRename() }
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
                text = "Delete queue",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface
            )
        },
        text = {
            Text(
                text = "Are you sure you want to delete \"${queue.name}\" queue",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface
            )
        },
        confirmButton = { ConfirmButton(text = "Delete") { onQueueDelete() } },
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
            text = "Dismiss",
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )
    }
}