package io.kekasquad.queue.queue

import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment

@AndroidEntryPoint
class QueueFragment : BaseFragment<QueueViewState, QueueIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_queue
    override val viewModel: QueueViewModel by viewModels()

    override fun backStackIntent(): QueueIntent = QueueIntent.InitialIntent(
        arguments?.getString(QUEUE_ID, "") ?: ""
    )

    override fun initialIntent(): QueueIntent? = QueueIntent.QueueNothingIntent

    override fun initViews() {

    }

    override fun render(viewState: QueueViewState) {

    }

    companion object {
        fun newInstance(id: String, name: String) = QueueFragment().also {
            it.arguments = bundleOf(
                QUEUE_ID to id,
                QUEUE_NAME to name
            )
        }

        private const val QUEUE_ID = "QUEUE_ID"
        private const val QUEUE_NAME = "QUEUE_NAME"
    }

}