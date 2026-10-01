package com.example.netcloudmusic.feature.compoent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.design.theme.LocalDividerColor
import com.example.netcloudmusic.core.design.theme.NetCloudMusicTheme
import com.example.netcloudmusic.core.design.theme.SpaceExtraMedium
import com.example.netcloudmusic.core.design.theme.SpaceMedium
import com.example.netcloudmusic.core.design.theme.SpaceSmallHeight
import com.example.netcloudmusic.core.design.theme.SpaceSmallWidth
import com.example.netcloudmusic.core.config.Config
import com.example.netcloudmusic.core.model.Product
import com.example.netcloudmusic.util.ResourceUtil
import com.example.netcloudmusic.util.StringUtil
import org.jetbrains.compose.resources.stringResource

import org.jetbrains.compose.resources.painterResource
import netcloudmusic.shared.generated.resources.Res
import netcloudmusic.shared.generated.resources.placeholder
import netcloudmusic.shared.generated.resources.price
import netcloudmusic.shared.generated.resources.product_item_info


@Composable
fun ItemProduct(
    data : Product ,
    modifier : Modifier = Modifier ,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.extraSmall)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(SpaceExtraMedium)
    ) {
        ItemProductImage(data)

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(SpaceMedium)
        ) {
            Text(
                text = data.title ,
                minLines = 2 ,
                maxLines = 2 ,
                overflow = TextOverflow.Ellipsis ,
                style = MaterialTheme.typography.titleSmall ,
                color = MaterialTheme.colorScheme.onSurface ,
            )

            SpaceSmallHeight()

            Text(
                text = data.highlight ?: " " ,
                style = MaterialTheme.typography.bodySmall ,
                color = MaterialTheme.colorScheme.onSurface ,
            )

            SpaceSmallHeight()

            Row(
                verticalAlignment = Alignment.CenterVertically ,
            ) {
                Text(
                    text = stringResource(Res.string.price, data.priceText),
                    style = MaterialTheme.typography.titleMedium ,
                    color = MaterialTheme.colorScheme.primary ,
                )

                SpaceSmallWidth()

                Text(
                    text = stringResource(Res.string.price, data.originPriceText),
                    style = MaterialTheme.typography.bodySmall.copy(
                        textDecoration = TextDecoration.LineThrough
                    ) ,
                    color = MaterialTheme.colorScheme.outline ,
                )
            }

            SpaceSmallHeight()

            Text(
                text = stringResource(
                    Res.string.product_item_info ,
                    StringUtil.formatCount(data.commentsCount) ,
                    data.commentsRate.toInt()
                ) ,
                style = MaterialTheme.typography.bodySmall ,
                color = MaterialTheme.colorScheme.outline ,
            )
        }

    }
}

@Composable
private fun ItemProductImage(data : Product) {



    if (data.icon != null)



        AsyncImage(
            model = ResourceUtil.r(data.icon),
            contentDescription = null ,
            contentScale = ContentScale.Crop ,
            modifier = Modifier
                .size(135.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(LocalDividerColor.current)
        )
    else
        Image(
            painter = painterResource(Res.drawable.placeholder) ,
            contentDescription = null ,
            contentScale = ContentScale.Crop ,
            modifier = Modifier
                .size(135.dp)
                .clip(MaterialTheme.shapes.extraSmall)
                .background(LocalDividerColor.current)
        )
}


@Preview(showBackground = true , apiLevel = 33)
@Composable
fun ItemProductPreview() {
    NetCloudMusicTheme {
        ItemProduct(
            data = Product(
                id = "20" ,
                number = "P20" ,
                title = "傻厨电煎锅多功能家用不粘锅电饼铛一体锅烙饼锅电烤锅烤肉电烤锅傻厨电煎锅多功能家用不粘锅电饼铛一体锅烙饼锅电烤锅烤肉电烤锅" ,
                icon = "mall/products/i20.png" ,
                icons = listOf(
                    "mall/products/b20.jpg" ,
                    "mall/products/b21.jpg"
                ) ,
                keyword = "小米 手机 徕卡" ,
                originPrice = 3 ,
                price = 2 ,
                memberPrice = 1 ,
                highlight = "徕卡影像 能效飞跃 小米澎湃OS " ,
                detail = "<img src=\"https://com-quick-project.oss-cn-beijing.aliyuncs.com/mall/products/d20.jpg\"/><img src=\"https://com-quick-project.oss-cn-beijing.aliyuncs.com/mall/products/d21.jpg\"/><img src=\"https://com-quick-project.oss-cn-beijing.aliyuncs.com/mall/products/d22.jpg\"/><img src=\"https://com-quick-project.oss-cn-beijing.aliyuncs.com/mall/products/d23.jpg\"/><p>这是一些html标签<p>只支持一些简单HTML标签，<a href=\"https://www.baidu.com\">这是链接</a>" ,
                sale = 1 ,
                delete = 0 ,
                hot = 1 ,
                news = 1 ,
                recommend = 1 ,
                commentsRate = 1228F ,
                commentsCount = 0 ,
                salesCount = 0 ,
                stocksCount = 0 ,
                score = 5F
            ) ,
        )
    }
}
