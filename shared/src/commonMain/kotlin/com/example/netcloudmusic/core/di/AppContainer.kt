package com.example.netcloudmusic.core.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.netcloudmusic.core.data.local.createDataStore
import com.example.netcloudmusic.core.data.repository.AddressRepository
import com.example.netcloudmusic.core.data.repository.CartRepository
import com.example.netcloudmusic.core.data.repository.CityRepository
import com.example.netcloudmusic.core.data.repository.MeRepository
import com.example.netcloudmusic.core.data.repository.OrderRepository
import com.example.netcloudmusic.core.data.repository.ProductRepository
import com.example.netcloudmusic.core.data.repository.ThemeRepository
import com.example.netcloudmusic.core.data.repository.UserRepository
import com.example.netcloudmusic.core.database.CityDatabase
import com.example.netcloudmusic.core.database.RoomDatabaseFactory
import com.example.netcloudmusic.core.network.KtorNetworkDataSource
import com.example.netcloudmusic.core.network.NetworkDataSource
import com.example.netcloudmusic.core.platform.appVersionCode
import com.example.netcloudmusic.core.platform.appVersionName

/**
 * KMP 共享依赖容器。
 *
 * Compose Android 工程使用 Hilt 管理这些依赖；KMP 代码不能依赖 Android/Hilt，
 * 因此在 commonMain 中提供等价的应用级单例容器，Android 和 iOS 共用同一套对象。
 */
class AppContainer(
    networkDataSource: NetworkDataSource = KtorNetworkDataSource(),
    dataStore: DataStore<Preferences>,
    cityDatabase: CityDatabase,
) {
    val productRepository: ProductRepository = ProductRepository(networkDataSource)
    val userRepository: UserRepository = UserRepository(networkDataSource)
    val orderRepository: OrderRepository = OrderRepository(networkDataSource)
    val categoryRepository: com.example.netcloudmusic.core.data.repository.CategoryRepository = com.example.netcloudmusic.core.data.repository.CategoryRepository(networkDataSource)
    val commonRepository: com.example.netcloudmusic.core.data.repository.CommonRepository = com.example.netcloudmusic.core.data.repository.CommonRepository(networkDataSource)
    val cartRepository: CartRepository = CartRepository(networkDataSource)
    val themeRepository: ThemeRepository = ThemeRepository(dataStore)
    val addressRepository: AddressRepository = AddressRepository(networkDataSource)
    val meRepository: MeRepository = MeRepository()
    val cityRepository: CityRepository = CityRepository(cityDatabase.cityDao())
}

object AppContainerProvider {
    private const val CITY_DB_NAME = "city.db"
    private var appContainer: AppContainer? = null

    /** 平台 context（Android 为 applicationContext，iOS 为 null），供各 ViewModel 使用 */
    var appContext: Any? = null
        private set

    /** 应用版本名（initialize 时从平台注入） */
    var versionName: String = ""
        private set

    /** 应用版本号（initialize 时从平台注入） */
    var versionCode: String = ""
        private set

    val instance: AppContainer
        get() = checkNotNull(appContainer) {
            "AppContainerProvider must be initialized before accessing instance"
        }

    fun initialize(
        context: Any? = null,
        networkDataSource: NetworkDataSource = KtorNetworkDataSource(),
    ) {
        if (appContainer == null) {
            appContext = context
            versionName = appVersionName(context)
            versionCode = appVersionCode(context)
            val cityDatabase = RoomDatabaseFactory(context).createBuilder(CITY_DB_NAME)
                .setDriver(BundledSQLiteDriver())
                .build()
            appContainer = AppContainer(
                networkDataSource = networkDataSource,
                dataStore = createDataStore(context),
                cityDatabase = cityDatabase,
            )
        }
    }
}
