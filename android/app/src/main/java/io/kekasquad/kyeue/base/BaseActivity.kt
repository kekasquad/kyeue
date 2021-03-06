package io.kekasquad.kyeue.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class BaseActivity<
        VS : MviViewState,
        I : MviIntent,
        NE : MviNavigationEvent> : AppCompatActivity(), MviView<VS, I, NE> {
    protected abstract val layoutResourceId: Int
    protected abstract val viewModel: MviViewModel<VS, I, NE>

    protected abstract fun initViews()

    protected abstract fun backStackIntent(): I?
    protected abstract fun initialIntent(): I?

    protected val _intentLiveData = MediatorLiveData<I>()

    override fun intents(): LiveData<I> = _intentLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        viewModel.navigationEvents().observe(this, {
            if (it != null) {
                navigator(it)
            }
        })
        viewModel.states().observe(this, {
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