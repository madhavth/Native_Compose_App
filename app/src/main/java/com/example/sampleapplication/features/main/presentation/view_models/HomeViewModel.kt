package com.example.sampleapplication.features.main.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.sampleapplication.core.data.helpers.DataStoreHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val app: Application,
    private val dataStoreHelper: DataStoreHelper
) : AndroidViewModel(app) {

    private val _counterFlow = dataStoreHelper.counterFlow

    val counter: StateFlow<Int> = _counterFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = 0
    )

    fun addCounter() {
        viewModelScope.launch {
            dataStoreHelper.changeCounterValue(counter.value + 1)
        }
    }
}