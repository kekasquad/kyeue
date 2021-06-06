package io.kekasquad.kyeue.ui.queue

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
    onPagingRetry: () -> Unit,
    onLogout: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val message = stringResourceOrNull(id = viewState.messageText)
    LaunchedEffect(snackbarHostState) {
        if (message != null) {
            snackbarHostState.showSnackbar(message)
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState),
        topBar = { QueueAppBar(onLogout = onLogout) },
        floatingActionButton = { FabQueueAdd(onQueueCreateClick = onQueueCreateClick) },
        backgroundColor = MaterialTheme.colors.surface
    ) { innerPadding ->
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
fun QueueAppBar(onLogout: () -> Unit) {
    KyeueAppBar(
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(end = 16.dp),
                text = stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1
            )
        },
        actions = {
            IconButton(
                modifier = Modifier.statusBarsPadding(),
                onClick = onLogout
            ) {
                Icon(
                    modifier = Modifier
                        .requiredWidth(24.dp)
                        .requiredHeight(24.dp),
                    tint = MaterialTheme.colors.onPrimary.copy(alpha = 0.6F),
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = ""
                )
            }
        }
    )
}