package com.example.netcloudmusic.core.data.repository

import com.example.netcloudmusic.feature.me.model.ButtonControl
import com.example.netcloudmusic.feature.me.model.OrderButtonItem
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.mall_after_sales
import netcloudmusic.shared.generated.resources.mall_wait_comment
import netcloudmusic.shared.generated.resources.mall_wait_pay
import netcloudmusic.shared.generated.resources.mall_wait_received

/**
 * "我的"页面本地数据仓库（纯静态配置，无网络请求）
 */
class MeRepository {

    /**
     * 更多服务按钮列表（16 项）
     */
    fun getMoreServiceButtons(): List<ButtonControl> = listOf(
        ButtonControl(id = 1,  icon = "mall/icons/wdzc.png", title = "我的资产",   key = "my_assets"),
        ButtonControl(id = 2,  icon = "mall/icons/sq.png",   title = "收货地址",   key = "receiving_address"),
        ButtonControl(id = 3,  icon = "mall/icons/sq.png",   title = "个人资料",   key = "my_profile"),
        ButtonControl(id = 4,  icon = "mall/icons/wdqb.png", title = "我的钱包",   key = "my_wallet"),
        ButtonControl(id = 5,  icon = "mall/icons/bqhc.png", title = "补贴场馆",   key = "subsidy_venue"),
        ButtonControl(id = 6,  icon = "mall/icons/wqdc.png", title = "问卷调查",   key = "questionnaire_survey"),
        ButtonControl(id = 7,  icon = "mall/icons/gfkf.png", title = "官方客服",   key = "office_customer_service"),
        ButtonControl(id = 8,  icon = "mall/icons/hyzl.png", title = "好友助力",   key = "friend_assistance"),
        ButtonControl(id = 9,  icon = "mall/icons/fqfw.png", title = "分期服务",   key = "installment_services"),
        ButtonControl(id = 10, icon = "mall/icons/yhts.png", title = "用户投诉",   key = "user_complaints"),
        ButtonControl(id = 11, icon = "mall/icons/wdzc.png", title = "我的资产",   key = "my_assets"),
        ButtonControl(id = 12, icon = "mall/icons/sq.png",   title = "商城社区",   key = "mall_community"),
        ButtonControl(id = 13, icon = "mall/icons/wdqb.png", title = "关于",       key = "about"),
        ButtonControl(id = 14, icon = "mall/icons/bqhc.png", title = "提及",       key = "mention"),
        ButtonControl(id = 15, icon = "mall/icons/bqhc.png", title = "其他功能",   key = "other_function"),
        ButtonControl(id = 16, icon = "mall/icons/bqhc.png", title = "设置",       key = "setting"),
    )

    /**
     * 订单状态按钮列表（4 项）
     */
    fun getOrderButtons(): List<OrderButtonItem> = listOf(
        OrderButtonItem(icon = Res.drawable.mall_wait_pay,      title = "待付款"),
        OrderButtonItem(icon = Res.drawable.mall_wait_received, title = "待收货"),
        OrderButtonItem(icon = Res.drawable.mall_wait_comment,  title = "待评价"),
        OrderButtonItem(icon = Res.drawable.mall_after_sales,   title = "退款/售后"),
    )
}
