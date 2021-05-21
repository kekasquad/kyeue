package io.kekasquad.kyeue.base

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import io.kekasquad.kyeue.ui.theme.KyeueTheme

abstract class BaseActivity<
        VS : MviViewState,
        I : MviIntent> : AppCompatActivity(), MviView<VS, I> {
    protected abstract val viewModel: MviViewModel<VS, I>

    protected abstract fun backStackIntent(): I?
    protected abstract fun initialIntent(): I?

    protected val _intentLiveData = MediatorLiveData<I>()

    override fun intents(): LiveData<I> = _intentLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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