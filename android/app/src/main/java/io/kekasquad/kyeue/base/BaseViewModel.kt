package io.kekasquad.kyeue.base

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseViewModel<
        VS : MviViewState,
        E : MviEffect,
        I : MviIntent,
        A : MviAction,
        NE : MviNavigationEvent> : ViewModel(), MviViewModel<VS, I, NE> {
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

    protected val navigationEventLiveData = MutableLiveData<NE>()

    private var clearLastIntentSource: (() -> Unit)? = null

    protected fun addIntermediateEffect(effect: E) {
        _effectLiveData.postValue(effect)
    }

    override fun states(): LiveData<VS> = viewStateLiveData

    override fun navigationEvents(): LiveData<NE> = navigationEventLiveData

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
            } else {
                navigationEventLiveData.value = null
            }
        }

        clearLastIntentSource = { _effectLiveData.removeSource(intents) }
    }

    override fun onCleared() {
        super.onCleared()
        clearLastIntentSource?.let { it() }
    }
}
