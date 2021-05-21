package io.kekasquad.kyeue.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<
        VS : MviViewState,
        E : MviEffect,
        I : MviIntent,
        A : MviAction> : ViewModel(), MviViewModel<VS, I> {
    abstract fun initialState(): VS
    protected abstract fun intentInterpreter(intent: I): A
    protected abstract suspend fun performAction(action: A): E
    protected abstract fun stateReducer(oldState: VS, effect: E): VS

    protected val viewStateLiveData: MediatorLiveData<VS> by lazy {
        MediatorLiveData<VS>().also {
            if (it.value == null) {
                it.value = initialState()
            }
        }
    }

    protected val _effectLiveData: MediatorLiveData<E> by lazy {
        MediatorLiveData<E>().also { effectLiveData ->
            viewStateLiveData.addSource(effectLiveData) {
                val newState = stateReducer(viewStateLiveData.value!!, it)
                viewStateLiveData.value = newState
            }
        }
    }

    private var clearLastIntentSource: (() -> Unit)? = null

    protected fun addIntermediateEffect(effect: E) {
        _effectLiveData.postValue(effect)
    }

    override fun states(): LiveData<VS> = viewStateLiveData

    override fun processIntents(intents: LiveData<I>) {
        clearLastIntentSource?.let { it() }

        _effectLiveData.addSource(intents) {
            if (it !is NothingIntent) {
                val action = intentInterpreter(it)
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        _effectLiveData.postValue(performAction(action))
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
