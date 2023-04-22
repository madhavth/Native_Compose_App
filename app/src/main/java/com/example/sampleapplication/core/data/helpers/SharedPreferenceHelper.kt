package com.example.sampleapplication.core.data.helpers

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DataStoreHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private val dataStore = context.dataStore

    companion object {
        const val ON_BOARDING_COMPLETED = "on_boarding_completed"
        const val EXAMPLE_COUNTER = "counter_example"
    }

    private val exampleCounter = intPreferencesKey(EXAMPLE_COUNTER)

    val counterFlow = dataStore.data.map {
        it[exampleCounter] ?: 0
    }

    suspend fun changeCounterValue(newValue: Int) {
        dataStore.edit { settings ->
            settings[exampleCounter] = newValue
        }
    }


}