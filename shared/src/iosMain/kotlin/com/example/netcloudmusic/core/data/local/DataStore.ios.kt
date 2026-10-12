package com.example.netcloudmusic.core.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import platform.Foundation.NSHomeDirectory

actual fun createDataStore(context: Any?): DataStore<Preferences> {
    return AppSettings.getDataStore {
        "${NSHomeDirectory()}/Documents/$dataStoreFileName"
    }
}
