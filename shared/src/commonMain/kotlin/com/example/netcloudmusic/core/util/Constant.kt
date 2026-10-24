package com.example.netcloudmusic.core.util

/**
 * 常量类
 */
object Constant {
    const val VALUE_NO = -1
    const val VALUE_NO_STRING = "-1"
    const val VALUE0 = 0
    const val VALUE1 = 1
    const val VALUE10 = 10
    const val VALUE20 = 20
    const val VALUE30 = 30
    const val VALUE40 = 40

    const val HEADER_AUTH = "Authorization"

    //region 平台
    /**
     * android
     */
    const val ANDROID = 0

    /**
     * ios
     */
    const val IOS = 10

    /**
     * web
     */
    const val WEB = 20

    /**
     * wap
     */
    const val WAP = 30
    //endregion

    //region 支付渠道
    /**
     * 支付宝
     */
    const val ALIPAY = 10

    /**
     * 微信
     */
    const val WECHAT = 20

    /**
     * 花呗分期
     */
    const val HUABEI_STAGE = 30
    //endregion

    //region 订单状态
    /**
     * 待支付
     */
    const val WAIT_PAY = 0

    /**
     * 订单关闭
     */
    const val CLOSE = 10

    /**
     * 待发货
     */
    const val WAIT_SHIPPED = 20

    /**
     * 待收货
     */
    const val WAIT_RECEIVED = 30

    /**
     * 待评价
     */
    const val WAIT_COMMENT = 40

    /**
     * 完成
     */
    const val COMPLETE = 50
    //endregion

    //html压缩：https://www.fulimama.com/formathtml/
    const val HTML_DATA_START =
        "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title></title><style>* {word-wrap: break-word;white-space: normal;margin: 0;padding: 0;color: #222;}body{font-family: -apple-system,helvetica,sans-serif;line-height: 1.71;font-size: 17px;}img {max-width: 100%;}</style></head><body>"
    const val HTML_DATA_END = "</body></html>"

    const val ADDRESS = 100
    const val PROFILE = 130
    const val OTHER = 110
    const val SETTING = 120
    const val FEED = 130
    const val ABOUT = 140

    const val STATUS = "status"
    const val PAGE = "page"

    /**
     * 搜索接口查询关键字
     */
    const val QUERY = "query"

    const val STYLE_CODE_LOGIN = VALUE0
    const val STYLE_FORGOT_PASSWORD = VALUE10
}