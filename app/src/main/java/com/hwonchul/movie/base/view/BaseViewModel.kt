package com.hwonchul.movie.base.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hwonchul.movie.util.SingleLiveEvent
import kotlinx.coroutines.launch

abstract class BaseViewModel<D : UiData, S : UiState>(
    initUiData: D,
    initialState: S,
) : ViewModel() {

    private val _uiData = SingleLiveEvent(initUiData)
    val uiData: LiveData<D> = _uiData

    private val _uiState = SingleLiveEvent(initialState)
    val uiState: LiveData<S> = _uiState

    // 상속된 클래스만 허용하도록 -> protected
    protected fun setData(data: D.() -> D) {
        viewModelScope.launch {
            _uiData.value = _uiData.value!!.data()
        }
    }

    protected fun setState(state: S.() -> S) {
        viewModelScope.launch {
            _uiState.value = _uiState.value!!.state()
        }
    }
}