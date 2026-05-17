package com.example.hrapp.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "hr_prefs")

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val TOKEN = stringPreferencesKey(Constants.TOKEN_KEY)
    private val ROLE  = stringPreferencesKey(Constants.ROLE_KEY)
    private val USER_ID = stringPreferencesKey(Constants.USER_ID_KEY)

    val token: Flow<String?> = context.dataStore.data.map { it[TOKEN] }
    val role: Flow<String?>  = context.dataStore.data.map { it[ROLE] }
    val userId: Flow<String?> = context.dataStore.data.map { it[USER_ID] }

    suspend fun saveToken(token: String) {
        context.dataStore.edit { it[TOKEN] = token }
    }

    suspend fun saveRole(role: String) {
        context.dataStore.edit { it[ROLE] = role }
    }

    suspend fun saveUserId(id: String) {
        context.dataStore.edit { it[USER_ID] = id }
    }

    suspend fun clearAll() {
        context.dataStore.edit { it.clear() }
    }
}