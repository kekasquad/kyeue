package io.kekasquad.kyeue.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class BaseFragment<
        VS : MviViewState,
        I : MviIntent,
        NE : MviNavigationEvent> : Fragment(), MviView<VS, I, NE> {
    protected abstract val layoutResourceId: Int
    protected abstract val viewModel: MviViewModel<VS, I, NE>

    protected abstract fun initViews()

    protected abstract fun backStackIntent(): I?
    protected abstract fun initialIntent(): I?

    protected val _intentLiveData = MediatorLiveData<I>()

    override fun intents(): LiveData<I> = _intentLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(
        layoutResourceId,
        container,
        false
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.navigationEvents().observe(viewLifecycleOwner, {
            if (it != null) {
                navigator(it)
            }
        })
        viewModel.states().observe(viewLifecycleOwner, {
            if (it != null) {
                render(it)
            }
        })
        viewModel.processIntents(intents())
        if (savedInstanceState == null && _intentLiveData.value == null) {
            _intentLiveData.value = initialIntent()
        }
    }

    override fun onPause() {
        super.onPause()
        _intentLiveData.value = backStackIntent()
    }
}