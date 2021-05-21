package io.kekasquad.kyeue.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.kekasquad.kyeue.ui.theme.KyeueTheme

abstract class BaseFragment<
        VS : MviViewState,
        I : MviIntent> : Fragment(), MviView<VS, I> {
    protected abstract val viewModel: MviViewModel<VS, I>

    protected abstract fun backStackIntent(): I?
    protected abstract fun initialIntent(): I?

    protected val _intentLiveData = MediatorLiveData<I>()

    override fun intents(): LiveData<I> = _intentLiveData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(inflater.context).apply {
        setContent {
            val viewState: VS? by viewModel.states().observeAsState()
            KyeueTheme {
                if (viewState != null) {
                    render(viewState!!)
                }
            }
        }
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