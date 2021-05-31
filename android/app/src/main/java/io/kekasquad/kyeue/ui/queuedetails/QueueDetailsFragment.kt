package io.kekasquad.kyeue.ui.queuedetails

import androidx.compose.runtime.Composable
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.google.accompanist.insets.ProvideWindowInsets
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.kyeue.base.BaseFragment
import io.kekasquad.kyeue.nav.Coordinator
import javax.inject.Inject

@AndroidEntryPoint
class QueueDetailsFragment :
    BaseFragment<QueueDetailsViewState, QueueDetailsIntent, QueueDetailsNavigationEvent>() {
    override val viewModel: QueueDetailsViewModel by viewModels()

    @Inject
    lateinit var coordinator: Coordinator

    override fun backStackIntent(): QueueDetailsIntent =
        QueueDetailsIntent.QueueDetailsNothingIntent

    override fun initialIntent(): QueueDetailsIntent = QueueDetailsIntent.InitialIntent(
        arguments?.getString(QUEUE_ID_KEY) ?: "",
        null
    )

    override val render: @Composable (QueueDetailsViewState) -> Unit = {
        ProvideWindowInsets {
            QueueDetailsContent(
                viewState = it,
                onRetry = { _intentLiveData.value = QueueDetailsIntent.RetryInitialLoadingIntent },
                onNavigateBack = {
                    _intentLiveData.value = QueueDetailsIntent.BackButtonClickIntent
                },
                onUserEnterQueue = { _intentLiveData.value = QueueDetailsIntent.EnterQueueIntent },
                onUserLeaveQueue = {
                    _intentLiveData.value = QueueDetailsIntent.LeaveUserIntent(it)
                },
                onUserSkipTurn = {
                    _intentLiveData.value = QueueDetailsIntent.UserSkipTurnIntent(it)
                },
                onUserMoveToTheEnd = {
                    _intentLiveData.value = QueueDetailsIntent.UserToTheEndIntent(it)
                }
            )
        }
    }

    override fun navigator(navigationEvent: QueueDetailsNavigationEvent) {
        when (navigationEvent) {
            QueueDetailsNavigationEvent.NavigateBackEvent -> coordinator.pop()
        }
    }

    companion object {
        private const val QUEUE_ID_KEY = "queueId"

        fun newInstance(queueId: String) = QueueDetailsFragment().apply {
            arguments = bundleOf(QUEUE_ID_KEY to queueId)
        }
    }

}