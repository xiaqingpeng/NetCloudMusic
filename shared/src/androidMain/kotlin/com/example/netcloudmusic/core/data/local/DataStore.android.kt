package com.example.netcloudmusic.core.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    require(context is Context) { "Android Context is required to create DataStore" }
    return AppSettings.getDataStore {
        context.applicationContext.filesDir.resolve(dataStoreFileName).absolutePath
    }
}
