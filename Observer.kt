package com.example.recyclertestactivity.data.models

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList

class MyFlow {
    private val _observerMap = ConcurrentHashMap<LifecycleOwner, LifecycleObserver>()
    private val _pendingCallBacks = ConcurrentHashMap<LifecycleOwner, CopyOnWriteArrayList<()-> Unit>>()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val _jobMap = ConcurrentHashMap<LifecycleOwner, CopyOnWriteArrayList<Job>>()

    private val _myFlow = MutableSharedFlow<Refresh?>(replay = 0,
        extraBufferCapacity = Refresh.entries.size,
        onBufferOverflow = BufferOverflow.SUSPEND
    )


    fun postValue(value: Refresh?) {
        if(_myFlow.subscriptionCount.value > 0) {
            _myFlow.tryEmit(value)
        }
    }

    fun addObserver(lifecycleOwner: LifecycleOwner, observer: Observer<Refresh?>): Job {
        return startCollectingForObserver(lifecycleOwner, observer)
    }

    fun removeObserver(lifecycleOwner: LifecycleOwner) {
        // cancel any existing jobs and collections.
        _jobMap[lifecycleOwner]?.let {
            it.forEach { job ->
                job.cancel()
            }
        }

        _jobMap.remove(lifecycleOwner)

        // remove all pending executions
        _pendingCallBacks.remove(lifecycleOwner)

        // remove observer added for lifecycleOwner
        _observerMap[lifecycleOwner]?.let {
            lifecycleOwner.lifecycle.removeObserver(it)
        }
        _observerMap.remove(lifecycleOwner)
    }

    private fun startCollectingForObserver(
        lifecycleOwner: LifecycleOwner,
        observer: Observer<Refresh?>
    ): Job {
        addLifeCycleObserver(lifecycleOwner)
        val job = coroutineScope.launch {
            _myFlow.collect {
                aoRefreshValue ->
                if(!isResumed(lifecycleOwner)) {
                    addPendingCallBack(lifecycleOwner) { observer.onChanged(aoRefreshValue) }
                }
                else {
                    observer.onChanged(aoRefreshValue)
                }
            }
        }

        if(_jobMap[lifecycleOwner] == null) {
            _jobMap[lifecycleOwner] = CopyOnWriteArrayList()
        }
        _jobMap[lifecycleOwner]?.add(job)
        return job
    }

    private fun addLifeCycleObserver(lifecycleOwner: LifecycleOwner) {
        if(_observerMap[lifecycleOwner] != null) {
            return
        }

        val observer = object: DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                _pendingCallBacks[lifecycleOwner]?.forEach {
                    pendingCallBack -> pendingCallBack()
                }
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                removeObserver(lifecycleOwner)
            }
        }
        _observerMap[lifecycleOwner] = observer
        lifecycleOwner.lifecycle.addObserver(observer)
    }

    private fun addPendingCallBack(
        lifecycleOwner: LifecycleOwner,
        pendingCallBack: () -> Unit
    ) {
        if(_pendingCallBacks[lifecycleOwner] == null) {
            _pendingCallBacks[lifecycleOwner] = CopyOnWriteArrayList()
        }
        _pendingCallBacks[lifecycleOwner]?.add(pendingCallBack)
    }



    private fun isResumed(lifecycleOwner: LifecycleOwner): Boolean {
        return lifecycleOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

}

enum class Refresh {
    TEST1,
    TEST2,
    TEST3,
    TEST4,
    END
}
