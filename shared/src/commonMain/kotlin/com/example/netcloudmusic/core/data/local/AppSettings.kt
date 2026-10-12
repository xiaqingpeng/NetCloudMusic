package com.example.netcloudmusic.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import io.ktor.utils.io.InternalAPI
import io.ktor.utils.io.locks.SynchronizedObject
import io.ktor.utils.io.locks.synchronized
import okio.Path.Companion.toPath

internal  const  val  dataStoreFileName = "settings.preferences_pb"

object AppSettings {
    private lateinit var dataStore: DataStore<Preferences>

    @OptIn(InternalAPI::class)
    private val lock = SynchronizedObject()

    @OptIn(InternalAPI::class)
    fun getDataStore(productPath:() -> String): DataStore<Preferences>{
        return synchronized(lock){
            if(::dataStore.isInitialized){
                dataStore
            }else{
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = {productPath().toPath()}
                ).also { dataStore=it }
            }
        }
    }
}
