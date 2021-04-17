package io.kekasquad.queue.queues

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.kekasquad.queue.R
import io.kekasquad.queue.base.BaseFragment

@AndroidEntryPoint
class QueuesFragment : BaseFragment<QueuesViewState, QueuesIntent>() {
    override val layoutResourceId: Int
        get() = R.layout.fragment_queues
    override val viewModel: QueuesViewModel by viewModels()

    override fun backStackIntent(): QueuesIntent = QueuesIntent.QueuesNothingIntent

    override fun initialIntent(): QueuesIntent = QueuesIntent.InitialIntent

    override fun initViews() {

    }

    override fun render(viewState: QueuesViewState) {

    }
}