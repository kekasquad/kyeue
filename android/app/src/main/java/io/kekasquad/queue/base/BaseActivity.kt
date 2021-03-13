package io.kekasquad.queue.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

abstract class BaseActivity<
        VS : MviViewState,
        I : MviIntent> : AppCompatActivity(), MviView<VS, I> {
    protected abstract val layoutResourceId: Int
    protected abstract val viewModel: MviViewModel<VS, I>

    /**
     * Set this intent while activity in back stack.
     * When we return to this activity, action linked to this intent will be executed.
     * If we don't want to execute any action, pass into this function intent of type NothingIntent
     */
    protected abstract fun backStackIntent(): I
    protected abstract fun initialIntent(): I?
    protected abstract fun initViews()

    protected val _intentLiveData = MediatorLiveData<I>()

    override fun intents(): LiveData<I> = _intentLiveData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        initViews()
        viewModel.states().observe(this) {
            render(it)
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