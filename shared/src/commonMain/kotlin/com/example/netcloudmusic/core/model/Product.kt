package com.example.netcloudmusic.core.model
import kotlinx.serialization.Serializable

/**
 * 商品模型
 */
@Serializable
data class Product(
    val id: String,

    /**
     * 标题
     */
    val title: String,

    /**
     * 商品编码
     */
    val number: String? = null,

    /**
     * 封面
     */
    val icon: String? = null,

    /**
     * 详情轮播图
     *
     * 多张图片地址以英文逗号分割存储
     */
    val icons: List<String> = emptyList(),

    /**
     * 宣传视频
     */
    val video: String? = null,

    /**
     * 搜索标签
     *
     * 以空格分割
     */
    val keyword: String? = null,

    val category1Id: String? = null,
    val category2Id: String? = null,
    val category3Id: String? = null,

    val originPrice: Int = 0,

    /**
     * 价格
     */
    val price: Int = 0,


    /**
     * 会员价
     */
    val memberPrice: Int = 0,

    /**
     * 亮点
     *
     *
     * 真实项目中，如果有特别需求，也可以实现为列表
     * 我们这里主要讲解商城核心逻辑，所以这里就简单实现了
     */
    val highlight: String? = null,

    /**
     * 详情
     *
     *
     * HTML格式化富文本
     */
    val detail: String? = null,

    /**
     * 是否上架
     */
    val sale: Byte = 1,

    /**
     * 是否删除
     */
    val delete: Byte = 0,

    /**
     * 是否热门
     */
    val hot: Byte = 0,

    /**
     * 是否新品
     */
    val news: Byte = 0,

    /**
     * 是否推荐
     *
     * 主要是显示到商品详情页
     */
    val recommend: Byte = 0,

    /**
     * 好评百分比
     */
    val commentsRate: Float = 0.0f,

    /**
     * 评论数
     */
    val commentsCount: Long = 0,

    /**
     * 销量
     */
    val salesCount: Long = 0,

    /**
     * 库存
     */
    val stocksCount: Long = 0,

    /**
     * 评分
     */
    val score: Float = 0.0f,

    val stocks: List<Stock>? = null,
    val specs: List<Spec>? = null,
) {

    val priceFloat
        get() = price * 1.0 / 100

    val originPriceFloat
        get() = originPrice * 1.0 / 100

    val priceText
        get() = formatPrice(price)

    val originPriceText
        get() = formatPrice(originPrice)
}

private fun formatPrice(valueInCents: Int): String {
    val absoluteValue = valueInCents.toLong().let { if (it < 0) -it else it }
    val yuan = absoluteValue / 100
    val cents = (absoluteValue % 100).toString().padStart(2, '0')
    val sign = if (valueInCents < 0) "-" else ""
    return "$sign$yuan.$cents"
}
