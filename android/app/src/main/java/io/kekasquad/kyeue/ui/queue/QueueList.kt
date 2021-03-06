package io.kekasquad.kyeue.ui.queue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.navigationBarsHeight
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.ui.components.ItemPagingLoading
import io.kekasquad.kyeue.ui.components.ItemPagingLoadingError
import io.kekasquad.kyeue.ui.components.ItemSingleLoading
import io.kekasquad.kyeue.ui.components.ItemSingleLoadingError
import io.kekasquad.kyeue.ui.theme.colorGray
import io.kekasquad.kyeue.vo.inapp.Queue
import io.kekasquad.kyeue.vo.inapp.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map

@Composable
fun QueueList(
    modifier: Modifier = Modifier,
    viewState: QueueViewState,
    onClick: (Queue) -> Unit,
    onQueueRenameClick: (Queue) -> Unit,
    onQueueDeleteClick: (Queue) -> Unit,
    onInitialRetry: () -> Unit,
    onPagingLoading: () -> Unit,
    onPagingRetry: () -> Unit
) {
    val listState = rememberLazyListState()

    when {
        viewState.isInitialLoading -> {
            ItemSingleLoading()
        }
        viewState.initialLoadingError != 0 -> {
            ItemSingleLoadingError(
                errorMessage = { InitialErrorMessage(errorMessage = viewState.initialLoadingError) },
                onRetry = onInitialRetry
            )
        }
        viewState.data.isEmpty() -> {
            EmptyItem()
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(bottom = 56.dp + 32.dp)
    ) {
        items(viewState.data) { queue ->
            QueueListItem(
                queue = queue,
                currentUser = viewState.currentUser,
                onClick = onClick,
                onQueueRenameClick = onQueueRenameClick,
                onQueueDeleteClick = onQueueDeleteClick
            )
            if (queue != viewState.data.last()) {
                Divider(color = colorGray)
            }
        }

        if (viewState.isPagingLoading) {
            item {
                ItemPagingLoading()
            }
        } else if (viewState.pagingLoadingError != 0) {
            item {
                ItemPagingLoadingError(
                    errorMessage = { PagingErrorMessage(errorMessage = viewState.pagingLoadingError) },
                    onRetry = onPagingRetry
                )
            }
        }

        // empty view behind navigation bar
        item {
            Column {
                Spacer(modifier = Modifier.navigationBarsHeight())
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow {
            listState.layoutInfo.totalItemsCount - (listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                ?: 0)
        }
            .map { offset -> offset < 4 && listState.layoutInfo.totalItemsCount >= 20 }
            .distinctUntilChanged()
            .filter { it == true }
            .collect { onPagingLoading() }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun QueueListItem(
    queue: Queue,
    currentUser: User,
    onClick: (Queue) -> Unit,
    onQueueRenameClick: (Queue) -> Unit,
    onQueueDeleteClick: (Queue) -> Unit
) {
    val (backgroundColor, onBackgroundColor) =
        if (currentUser == queue.creator) {
            MaterialTheme.colors.primaryVariant to MaterialTheme.colors.onSecondary.copy(alpha = 0.6F)
        } else {
            MaterialTheme.colors.background to MaterialTheme.colors.onBackground.copy(alpha = 0.6F)
        }

    Card(
        modifier = Modifier
            .height(48.dp)
            .fillMaxWidth(),
        onClick = { onClick(queue) },
        backgroundColor = backgroundColor,
        shape = MaterialTheme.shapes.large
    ) {
        ConstraintLayout {
            val (button, text) = createRefs()

            Text(
                modifier = Modifier.constrainAs(text) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start, 16.dp)
                    if (currentUser.id == queue.creator.id) {
                        end.linkTo(button.start, 16.dp)
                    } else {
                        end.linkTo(parent.end, 16.dp)
                    }
                    width = Dimension.fillToConstraints
                },
                text = queue.name,
                color = onBackgroundColor,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            if (currentUser.id == queue.creator.id) {
                CreatorQueueMenuIcon(
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, 16.dp)
                    },
                    iconTint = onBackgroundColor,
                    onQueueRenameClick = onQueueRenameClick,
                    onQueueDeleteClick = onQueueDeleteClick,
                    queue = queue
                )
            }
        }
    }
}

@Composable
fun CreatorQueueMenuIcon(
    modifier: Modifier,
    iconTint: Color,
    queue: Queue,
    onQueueRenameClick: (Queue) -> Unit,
    onQueueDeleteClick: (Queue) -> Unit
) {
    var isMenuOpened by remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier.then(Modifier.size(24.dp)),
        onClick = { isMenuOpened = true }
    ) {
        Icon(
            Icons.Rounded.MoreVert,
            "",
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
        DropdownMenu(
            expanded = isMenuOpened,
            modifier = Modifier.background(MaterialTheme.colors.surface),
            onDismissRequest = { isMenuOpened = false }
        ) {
            DropdownMenuItem(onClick = {
                isMenuOpened = false
                onQueueRenameClick(queue)
            }) {
                Text(
                    text = stringResource(R.string.text_queue_menu_rename),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
            DropdownMenuItem(onClick = {
                isMenuOpened = false
                onQueueDeleteClick(queue)
            }) {
                Text(
                    text = stringResource(R.string.text_queue_menu_delete),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}

@Composable
fun EmptyItem() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val content = createRef()

        Text(
            modifier = Modifier.constrainAs(content) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            text = stringResource(R.string.text_queue_empty_list),
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun InitialErrorMessage(errorMessage: Int) {
    Text(
        text = stringResource(id = errorMessage),
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h6
    )
}

@Composable
fun PagingErrorMessage(errorMessage: Int) {
    Text(
        text = stringResource(id = errorMessage),
        color = MaterialTheme.colors.onSurface,
        style = MaterialTheme.typography.h6
    )
}