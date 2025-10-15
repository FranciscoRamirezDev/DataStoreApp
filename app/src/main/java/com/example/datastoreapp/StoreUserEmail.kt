package com.example.datastoreapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreUserEmail(private val context: Context) {

    companion object{

        //declaracion de variable para el dataStore
        private val Context.dataStore : DataStore<Preferences> by preferencesDataStore("UserEmail")
        val USER_EMAIL = stringPreferencesKey("user_email")
    }

    //funcion para leer variable del dataStore
    val getEmail: Flow<String?> = context.dataStore.data
        .map {
            preferences ->
            preferences[USER_EMAIL]?:""
        }

    //funcion para guardar variable en el dataStore
    suspend fun saveEmail(email:String){
        context.dataStore.edit {
            preferences ->
            preferences[USER_EMAIL] = email
        }
    }
}