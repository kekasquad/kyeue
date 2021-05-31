package io.kekasquad.kyeue.ui.queuedetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.insets.navigationBarsHeight
import io.kekasquad.kyeue.ui.components.ItemSingleLoading
import io.kekasquad.kyeue.ui.components.ItemSingleLoadingError
import io.kekasquad.kyeue.ui.components.KyeueAppBar
import io.kekasquad.kyeue.ui.theme.colorGray
import io.kekasquad.kyeue.vo.inapp.User

@Composable
fun QueueDetailsContent(
    modifier: Modifier = Modifier,
    viewState: QueueDetailsViewState,
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit,
    onUserEnterQueue: () -> Unit,
    onUserLeaveQueue: (User) -> Unit,
    onUserSkipTurn: (User) -> Unit,
    onUserMoveToTheEnd: (User) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        backgroundColor = MaterialTheme.colors.surface,
        topBar = {
            QueueDetailsAppBar(
                viewState = viewState,
                onNavigateBack = onNavigateBack,
                onUserEnterQueue = onUserEnterQueue,
                onUserLeaveQueue = onUserLeaveQueue
            )
        }
    ) {
        QueueDetailsMemberList(
            viewState = viewState,
            onRetry = onRetry,
            onUserLeaveQueue = onUserLeaveQueue,
            onUserSkipTurn = onUserSkipTurn,
            onUserMoveToTheEnd = onUserMoveToTheEnd
        )
    }
}

@Composable
fun QueueDetailsAppBar(
    modifier: Modifier = Modifier,
    viewState: QueueDetailsViewState,
    onNavigateBack: () -> Unit,
    onUserEnterQueue: () -> Unit,
    onUserLeaveQueue: (User) -> Unit
) {
    val isActionButtonsVisible = !viewState.currentUser.isTeacher
            && viewState.queue != null
            && viewState.queue.creator != viewState.currentUser
    val isAddToQueue = viewState.queue?.members?.contains(viewState.currentUser)

    KyeueAppBar(
        modifier = modifier,
        navigation = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    modifier = modifier
                        .requiredWidth(24.dp)
                        .requiredHeight(24.dp),
                    tint = MaterialTheme.colors.onPrimary,
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = ""
                )
            }
        },
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 16.dp),
                text = viewState.queue?.name ?: "",
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onPrimary,
                maxLines = 1
            )
        },
        actions = {
            if (isActionButtonsVisible) {
                if (isAddToQueue == false) {
                    IconButton(onClick = onUserEnterQueue) {
                        Icon(
                            modifier = modifier
                                .requiredWidth(24.dp)
                                .requiredHeight(24.dp),
                            tint = MaterialTheme.colors.onPrimary,
                            imageVector = Icons.Outlined.PersonAdd,
                            contentDescription = ""
                        )
                    }
                } else {
                    IconButton(onClick = { onUserLeaveQueue(viewState.currentUser) }) {
                        Icon(
                            modifier = modifier
                                .requiredWidth(24.dp)
                                .requiredHeight(24.dp),
                            tint = MaterialTheme.colors.onPrimary,
                            imageVector = Icons.Outlined.Logout,
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QueueDetailsMemberList(
    modifier: Modifier = Modifier,
    viewState: QueueDetailsViewState,
    onRetry: () -> Unit,
    onUserLeaveQueue: (User) -> Unit,
    onUserSkipTurn: (User) -> Unit,
    onUserMoveToTheEnd: (User) -> Unit
) {
    val listState = rememberLazyListState()

    when {
        viewState.isLoading -> {
            ItemSingleLoading()
        }
        viewState.loadingErrorMessage != 0 -> {
            ItemSingleLoadingError(
                errorMessage = { InitialErrorMessage(errorMessage = viewState.loadingErrorMessage) },
                onRetry = onRetry
            )
        }
        viewState.queue?.members?.isEmpty() ?: false -> {
            EmptyItem()
        }
    }

    LazyColumn(
        modifier = modifier,
        state = listState
    ) {
        if (viewState.queue?.members?.isNotEmpty() == true) {
            stickyHeader {
                HeaderItem(name = "Current")
            }
            item {
                MemberItem(
                    currentUser = viewState.currentUser,
                    member = viewState.queue.members.last(),
                    queueCreatorId = viewState.queue.creator.id,
                    onUserLeaveQueue = onUserLeaveQueue,
                    onUserSkipTurn = onUserSkipTurn,
                    onUserMoveToTheEnd = onUserMoveToTheEnd
                )
            }
        }
        if (viewState.queue?.members?.size ?: 0 > 1) {
            stickyHeader {
                HeaderItem(name = "Ongoing")
            }
            items(viewState.queue!!.members.reversed().drop(1)) { member ->
                MemberItem(
                    currentUser = viewState.currentUser,
                    member = member,
                    queueCreatorId = viewState.queue.creator.id,
                    onUserLeaveQueue = onUserLeaveQueue,
                    onUserSkipTurn = onUserSkipTurn,
                    onUserMoveToTheEnd = onUserMoveToTheEnd
                )
                if (member != viewState.queue.members[0]) {
                    Divider(color = colorGray)
                }
            }
        }

        // empty view behind navigation bar
        item {
            Column {
                Spacer(modifier = Modifier.navigationBarsHeight())
            }
        }
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
fun EmptyItem(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.Center),
            text = "This queue is empty",
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HeaderItem(
    modifier: Modifier = Modifier,
    name: String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = name,
            color = MaterialTheme.colors.onSurface,
            style = MaterialTheme.typography.subtitle2
        )
    }
}

@Composable
fun MemberItem(
    modifier: Modifier = Modifier,
    queueCreatorId: String,
    currentUser: User,
    member: User,
    onUserLeaveQueue: (User) -> Unit,
    onUserSkipTurn: (User) -> Unit,
    onUserMoveToTheEnd: (User) -> Unit
) {
    val isIconVisible = currentUser.id == queueCreatorId
            || currentUser.isTeacher
            || member.id == currentUser.id
    val (backgroundColor, onBackgroundColor) =
        if (currentUser.id == member.id) {
            MaterialTheme.colors.primaryVariant to MaterialTheme.colors.onSecondary
        } else {
            MaterialTheme.colors.background to MaterialTheme.colors.onBackground
        }

    Card(
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth(),
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
                    if (isIconVisible) {
                        end.linkTo(button.start, 16.dp)
                    } else {
                        end.linkTo(parent.end, 16.dp)
                    }
                    width = Dimension.fillToConstraints
                },
                text = member.username,
                color = onBackgroundColor,
                style = MaterialTheme.typography.subtitle1,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1
            )

            if (isIconVisible) {
                MemberActionIcon(
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, 16.dp)
                    },
                    iconTint = onBackgroundColor,
                    member = member,
                    onUserLeaveQueue = onUserLeaveQueue,
                    onUserSkipTurn = onUserSkipTurn,
                    onUserMoveToTheEnd = onUserMoveToTheEnd
                )
            }
        }
    }
}

@Composable
fun MemberActionIcon(
    modifier: Modifier = Modifier,
    iconTint: Color,
    member: User,
    onUserLeaveQueue: (User) -> Unit,
    onUserSkipTurn: (User) -> Unit,
    onUserMoveToTheEnd: (User) -> Unit
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
                onUserSkipTurn(member)
            }) {
                Text(
                    text = "Skip turn",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
            DropdownMenuItem(onClick = {
                isMenuOpened = false
                onUserMoveToTheEnd(member)
            }) {
                Text(
                    text = "Go to the End",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
            DropdownMenuItem(onClick = {
                isMenuOpened = false
                onUserLeaveQueue(member)
            }) {
                Text(
                    text = "Leave",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }
    }
}