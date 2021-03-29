package io.kekasquad.queue.queue

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment

@AndroidEntryPoint
class QueueFragment : BaseFragment<QueueViewState, QueueIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_queue
    override val viewModel: QueueViewModel by viewModels()

    override fun backStackIntent(): QueueIntent {
        TODO("Not yet implemented")
    }

    override fun initialIntent(): QueueIntent? {
        TODO("Not yet implemented")
    }

    override fun initViews() {

    }

    override fun render(viewState: QueueViewState) {

    }

}