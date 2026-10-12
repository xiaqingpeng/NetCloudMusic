package com.example.netcloudmusic.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.netcloudmusic.core.data.local.createDataStore
import com.example.netcloudmusic.core.data.repository.ThemeRepository
import com.example.netcloudmusic.core.data.repository.ProductRepository
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.network.KtorNetworkDataSource
import com.example.netcloudmusic.core.network.NetworkDataSource

/**
 * KMP 共享依赖容器。
 *
 * Compose Android 工程使用 Hilt 管理这些依赖；KMP 代码不能依赖 Android/Hilt，
 * 因此在 commonMain 中提供等价的应用级单例容器，Android 和 iOS 共用同一套对象。
 */
class AppContainer(
    networkDataSource: NetworkDataSource = KtorNetworkDataSource(),
    dataStore: DataStore<Preferences>,
) {
    val productRepository: ProductRepository = ProductRepository(networkDataSource)
    val userRepository: UserRepository = UserRepository(networkDataSource)
    val themeRepository: ThemeRepository = ThemeRepository(dataStore)
}

object AppContainerProvider {
    private var appContainer: AppContainer? = null

    val instance: AppContainer
        get() = checkNotNull(appContainer) {
            "AppContainerProvider must be initialized before accessing instance"
        }

    fun initialize(
        context: Any? = null,
        networkDataSource: NetworkDataSource = KtorNetworkDataSource(),
    ) {
        if (appContainer == null) {
            appContainer = AppContainer(networkDataSource, createDataStore(context))
        }
    }
}
