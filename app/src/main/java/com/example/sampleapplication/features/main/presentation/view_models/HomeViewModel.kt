package com.example.sampleapplication.features.main.presentation.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.sampleapplication.core.data.helpers.DataStoreHelper
import com.example.sampleapplication.core.domain.entities.ChannelData
import com.example.sampleapplication.core.domain.entities.NotificationData
import com.example.sampleapplication.core.presentation.helpers.NotificationHelper
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
    private val dataStoreHelper: DataStoreHelper,
    private val notificationHelper: NotificationHelper
) : AndroidViewModel(app) {

    init {
        notificationHelper.showNotification(
            NotificationData(
                title = "This is the title",
                message = "This is the message",
                notificationId = 1,
                channelData = ChannelData(
                    channelId = "channelId",
                    name = "channelName",
                    description = "channelDescription"
                )
            )
        )
    }

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