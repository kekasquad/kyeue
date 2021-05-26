package io.kekasquad.kyeue.ui.queue

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.kyeue.R
import io.kekasquad.kyeue.base.BaseFragment

@AndroidEntryPoint
class QueueFragment : BaseFragment<QueueViewState, QueueIntent, QueueNavigationEvent>() {
    override val viewModel: QueueViewModel by viewModels()

    override fun backStackIntent(): QueueIntent = QueueIntent.QueueNothingIntent

    override fun initialIntent(): QueueIntent = QueueIntent.InitialLoadingIntent

    override val render: @Composable ((QueueViewState) -> Unit) = { viewState ->
        ProvideWindowInsets {
            QueuesContent(
                modifier = Modifier,
                viewState = viewState,
                onQueueCreate = { _intentLiveData.value = QueueIntent.CreateQueueIntent },
                onQueueRename = { _intentLiveData.value = QueueIntent.RenameQueueIntent },
                onQueueDelete = { _intentLiveData.value = QueueIntent.DeleteQueueIntent },
                onClick = { _intentLiveData.value = QueueIntent.OpenQueueDetailsIntent(it) },
                onQueueCreateClick = { _intentLiveData.value = QueueIntent.OpenCreateDialogIntent },
                onQueueRenameClick = {
                    _intentLiveData.value = QueueIntent.OpenRenameDialogIntent(it)
                },
                onQueueDeleteClick = {
                    _intentLiveData.value = QueueIntent.OpenDeleteDialogIntent(it)
                },
                onQueueCreateDismiss = {
                    _intentLiveData.value = QueueIntent.DismissCreateDialogIntent
                },
                onQueueRenameDismiss = {
                    _intentLiveData.value = QueueIntent.DismissRenameDialogIntent
                },
                onQueueDeleteDismiss = {
                    _intentLiveData.value = QueueIntent.DismissDeleteDialogIntent
                },
                onQueueNameChange = {
                    _intentLiveData.value = QueueIntent.InputQueueNameIntent(it)
                },
                onInitialRetry = { _intentLiveData.value = QueueIntent.RetryInitialLoadingIntent },
                onPagingLoading = { _intentLiveData.value = QueueIntent.PagingLoadingIntent },
                onPagingRetry = { _intentLiveData.value = QueueIntent.RetryPagingLoadingIntent }
            )
        }
    }

    override fun navigator(navigationEvent: QueueNavigationEvent) {
        when (navigationEvent) {
            is QueueNavigationEvent.NavigateToQueueDetails ->
                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        }
    }
}