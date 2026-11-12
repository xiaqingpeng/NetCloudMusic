# NetCloudMusic

> 基于 Kotlin Multiplatform 和 Compose Multiplatform 构建的跨平台商城客户端示例。

![Platform](https://img.shields.io/badge/platform-Android%20%7C%20iOS-brightgreen)
![Language](https://img.shields.io/badge/language-Kotlin-blue)
![UI](https://img.shields.io/badge/UI-Compose%20Multiplatform-4285F4)

## 简介

NetCloudMusic 是一个面向 Android 和 iOS 的 Kotlin Multiplatform 应用示例。项目将主要业务逻辑、页面和 UI 组件放在共享模块中，在两个平台复用同一套 Compose Multiplatform 界面，同时保留 Android 和 iOS 各自的应用入口。

项目当前实现的是一个完整的商城业务流程，覆盖商品发现、分类浏览、登录注册、购物车、收货地址、订单确认、支付和个人中心等场景，也包含 GitHub 登录、城市选择、主题设置和短视频内容入口。

## 功能模块

| 模块 | 功能 |
| --- | --- |
| 启动与引导 | 启动页、首次使用引导、跳过倒计时和启动状态保存 |
| 发现首页 | 商品内容展示、商品详情入口和首页内容浏览 |
| 分类浏览 | 商品分类、分类详情和分类下的商品列表 |
| 短视频 | 短视频内容入口 |
| 商品 | 商品详情、规格选择、库存选择和购买数量设置 |
| 购物车 | 商品列表、数量管理、商品选择和批量结算 |
| 登录与账号 | 账号登录、验证码登录、注册、忘记密码和登录态管理 |
| GitHub 登录 | GitHub OAuth 登录和 GitHub 个人资料展示 |
| 收货地址 | 地址列表、新增和编辑地址、省市区选择 |
| 订单 | 订单列表、订单详情、订单状态筛选和待支付订单处理 |
| 支付 | 确认订单、选择收货地址、创建订单和支付流程 |
| 个人中心 | 用户信息、订单入口、地址入口、设置、关于页面 |
| 设置 | 主题模式和应用偏好设置 |
| 本地数据 | DataStore 偏好存储、Room KMP 数据库和 SQLite Bundled |
| 多平台 | Android 与 iOS 共用业务层和 Compose UI |

## 技术栈

- **语言**: Kotlin 2.4.20
- **跨平台**: Kotlin Multiplatform
- **UI**: Compose Multiplatform 1.12.1、Material 3
- **导航**: AndroidX Navigation Compose
- **状态与架构**: ViewModel、Repository、依赖容器和按功能拆分的页面模块
- **网络**: Ktor Client、Kotlinx Serialization、Android OkHttp、iOS Darwin
- **图片加载**: Coil 3 + Ktor 网络加载器
- **本地存储**: DataStore Preferences、Room 3 KMP、SQLite Bundled
- **异步与时间**: Kotlin Coroutines、AtomicFu、Kotlinx DateTime
- **Android 构建**: Android Gradle Plugin 9.2.1、compileSdk 37、minSdk 33、Java 11
- **iOS 构建**: Xcode 工程 + Shared 静态 Framework

## 项目结构

```text
NetCloudMusic/
├── androidApp/                         # Android 应用入口、Manifest 和平台配置
│   └── src/main/kotlin/.../MainActivity.kt
├── iosApp/                             # iOS 应用入口和 Xcode 工程
│   ├── iosApp/                         # SwiftUI 容器和资源
│   └── iosApp.xcodeproj/               # Xcode 工程
├── shared/                             # Android 与 iOS 共享模块
│   ├── src/commonMain/                 # 共享业务逻辑、Compose UI 和资源
│   │   └── kotlin/com/example/netcloudmusic/
│   │       ├── core/                   # 配置、网络、数据库、会话、Repository 和公共 UI
│   │       ├── feature/                # 各业务功能页面和导航
│   │       └── ui/                     # App 级 Compose 入口和主题
│   ├── src/androidMain/                # Android 平台实现
│   ├── src/iosMain/                    # iOS 平台实现
│   ├── src/appleMain/                  # Apple 平台共享实现
│   ├── src/nativeMain/                 # Native 平台共享实现
│   └── schemas/                        # Room 数据库 schema
├── build.gradle.kts                    # 根项目插件配置
├── settings.gradle.kts                 # 模块和仓库配置
├── gradle/libs.versions.toml           # 依赖与插件版本目录
└── gradlew                             # Gradle Wrapper
```

## 环境要求

- Android Studio，建议使用支持当前 Kotlin、Compose Multiplatform 和 Android Gradle Plugin 版本的版本
- JDK 11
- Android SDK Platform 37
- Android SDK Build Tools 以及 API 33 或更高版本的模拟器/设备
- Xcode（仅构建和运行 iOS 时需要）
- CocoaPods 不是当前项目的必需依赖，iOS 应用通过 Xcode 工程链接 `Shared` Framework

## 配置

### Android 后端环境

Android 应用定义了两个环境 flavor：

- `dev`: 使用远程开发服务 `https://quick-server-sp.ixuea.com/`
- `local`: 使用本地服务 `http://192.168.2.100:8080/`

应用还定义了两个渠道 flavor：`default` 和 `play`。常用的开发构建变体为 `devDefaultDebug`。

### GitHub OAuth

GitHub 登录配置可以写入项目根目录的 `local.properties`，也可以通过环境变量提供：

```properties
github.clientId=your_github_client_id
github.clientSecret=your_github_client_secret
```

对应的环境变量为：

```bash
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

请不要将真实的 Client Secret 提交到 Git 仓库。未配置 GitHub OAuth 时，商城的其他功能仍可使用，但 GitHub 登录流程无法正常完成。

## 快速开始

### 1. 获取项目

```bash
git clone <repository-url>
cd netcloudmusic
```

### 2. Android 运行

使用 Android Studio 打开项目，等待 Gradle 同步完成后，选择 Android 模拟器或实体设备运行。

也可以使用 Gradle 构建开发环境的 Debug APK：

```bash
./gradlew :androidApp:assembleDevDefaultDebug
```

构建产物位于：

```text
androidApp/build/outputs/apk/devDefault/debug/
```

如果需要连接本地后端，可以构建 `local` 环境：

```bash
./gradlew :androidApp:assembleLocalDefaultDebug
```

### 3. iOS 运行

1. 使用 Xcode 打开 `iosApp/iosApp.xcodeproj`。
2. 选择 iOS 模拟器或已连接的设备。
3. 编译并运行 `iosApp` Scheme。

iOS 入口由 SwiftUI 提供容器，实际页面通过 `Shared` Framework 中的 `MainViewController` 加载共享 Compose UI。

## 构建与测试

常用 Gradle 命令：

```bash
# 查看所有可用任务
./gradlew tasks

# 构建 Android 开发 Debug 包
./gradlew :androidApp:assembleDevDefaultDebug

# 构建 Android 本地服务 Debug 包
./gradlew :androidApp:assembleLocalDefaultDebug

# 执行共享模块的测试任务
./gradlew :shared:test
```

具体可用任务会随 Kotlin、Android Gradle Plugin 和目标平台配置变化，可通过 `./gradlew tasks --all` 查看完整列表。

## 架构说明

项目将跨平台代码集中在 `shared` 模块中：

- `feature` 按业务场景拆分页面、ViewModel 和导航定义。
- `core/network` 封装 Ktor 网络客户端和会话请求。
- `core/data` 提供 Repository 与本地数据访问能力。
- `core/database` 使用 Room KMP 和 SQLite Bundled 管理跨平台数据库。
- `core/session` 和 DataStore 负责用户会话及应用偏好持久化。
- `core/design` 与 `ui` 提供主题、公共组件和 Compose 应用入口。
- `androidMain`、`iosMain` 和其他平台 source set 仅承载平台相关实现。

应用的共享 Compose 入口是 `MyApp()`，从启动页开始，根据用户状态进入引导页或主界面，并通过 Navigation Compose 管理商品、账户、地址、订单和支付等页面之间的跳转。

## 当前状态

- [x] Kotlin Multiplatform 项目初始化
- [x] Android 与 iOS 平台入口
- [x] Compose Multiplatform 共享 UI
- [x] 启动页和首次使用引导
- [x] 发现首页、短视频、分类和个人中心
- [x] 商品详情和购物车
- [x] 账号登录、验证码登录、注册和忘记密码
- [x] GitHub OAuth 登录与个人资料
- [x] 收货地址与城市选择
- [x] 订单列表、订单详情和支付流程
- [x] DataStore 偏好存储
- [x] Room KMP 数据库和 SQLite Bundled
- [x] 主题模式设置

## 许可证

仓库当前未包含 `LICENSE` 文件，许可证信息以项目维护者的正式声明为准。
# NetCloudMusic
