package io.kekasquad.kyeue.ui.queue

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.ui.components.KyeueAppBar
import io.kekasquad.kyeue.utils.stringResourceOrNull
import io.kekasquad.kyeue.vo.inapp.Queue
import kotlinx.coroutines.launch


@Composable
fun QueuesContent(
    modifier: Modifier = Modifier,
    viewState: QueueViewState,
    onQueueCreate: () -> Unit,
    onQueueRename: () -> Unit,
    onQueueDelete: () -> Unit,
    onClick: (Queue) -> Unit,
    onQueueCreateClick: () -> Unit,
    onQueueRenameClick: (Queue) -> Unit,
    onQueueDeleteClick: (Queue) -> Unit,
    onQueueCreateDismiss: () -> Unit,
    onQueueRenameDismiss: () -> Unit,
    onQueueDeleteDismiss: () -> Unit,
    onQueueNameChange: (String) -> Unit,
    onInitialRetry: () -> Unit,
    onPagingLoading: () -> Unit,
    onPagingRetry: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = { QueueAppBar() },
        floatingActionButton = { FabQueueAdd(onQueueCreateClick = onQueueCreateClick) },
        backgroundColor = MaterialTheme.colors.surface
    ) { innerPadding ->
        val message = stringResourceOrNull(id = viewState.messageText)
        if (message != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }

        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            QueueList(
                viewState = viewState,
                onClick = onClick,
                onQueueRenameClick = onQueueRenameClick,
                onQueueDeleteClick = onQueueDeleteClick,
                onInitialRetry = onInitialRetry,
                onPagingLoading = onPagingLoading,
                onPagingRetry = onPagingRetry
            )

            when {
                viewState.isCreateDialogOpened -> {
                    CreateQueueDialog(
                        queueName = viewState.queueName,
                        onQueueNameChange = onQueueNameChange,
                        errorText = viewState.queueNameError,
                        onQueueCreate = onQueueCreate,
                        onQueueCreateDismiss = onQueueCreateDismiss
                    )
                }
                viewState.queueToRename != null -> {
                    RenameQueueDialog(
                        queue = viewState.queueToRename,
                        queueName = viewState.queueName,
                        onQueueNameChange = onQueueNameChange,
                        errorText = viewState.queueNameError,
                        onQueueRename = onQueueRename,
                        onQueueRenameDismiss = onQueueRenameDismiss
                    )
                }
                viewState.queueToDelete != null -> {
                    DeleteQueueDialog(
                        queue = viewState.queueToDelete,
                        onQueueDelete = onQueueDelete,
                        onQueueDeleteDismiss = onQueueDeleteDismiss
                    )
                }
            }

        }
    }
}

@Composable
fun FabQueueAdd(
    modifier: Modifier = Modifier,
    onQueueCreateClick: () -> Unit
) {
    FloatingActionButton(
        modifier = modifier.navigationBarsPadding(),
        backgroundColor = MaterialTheme.colors.primary,
        onClick = { onQueueCreateClick() }
    ) {
        Icon(
            Icons.Rounded.Add,
            "",
            tint = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun QueueAppBar() {
    KyeueAppBar(
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                text = stringResource(id = R.string.app_name),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onPrimary,
                textAlign = TextAlign.Center
            )
        }
    )
}