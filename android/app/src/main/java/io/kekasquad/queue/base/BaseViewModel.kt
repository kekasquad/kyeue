package io.kekasquad.queue.base

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class BaseViewModel<
        VS : MviViewState,
        E : MviEffect,
        I : MviIntent,
        A : MviAction> : ViewModel(), MviViewModel<VS, I> {
    abstract fun initialState(): VS
    protected abstract fun intentInterpreter(intent: I): A
    protected abstract suspend fun performAction(action: A): E
    protected abstract fun stateReducer(oldState: VS, effect: E): VS

    protected val viewStateLiveData = MediatorLiveData<VS>().also {
        if (it.value == null) {
            it.value = initialState()
        }
    }

    private val _effectLiveData = MediatorLiveData<E>().also { effectLiveData ->
        viewStateLiveData.addSource(effectLiveData) {
            val newState = stateReducer(viewStateLiveData.value!!, it)
            Timber.d(newState.log())
            viewStateLiveData.value = newState
        }
    }

    private var clearLastIntentSource: (() -> Unit)? = null

    protected fun addIntermediateEffect(effect: E) {
        _effectLiveData.value = effect
    }

    override fun states(): LiveData<VS> = viewStateLiveData.distinctUntilChanged()

    override fun processIntents(intents: LiveData<I>) {
        clearLastIntentSource?.let { it() }

        _effectLiveData.addSource(intents) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    if (it !is NothingIntent) {
                        _effectLiveData.postValue(performAction(intentInterpreter(it)))
                    }
                }
            }
        }

        clearLastIntentSource = { _effectLiveData.removeSource(intents) }
    }

    override fun onCleared() {
        super.onCleared()
        clearLastIntentSource?.let { it() }
    }
}
