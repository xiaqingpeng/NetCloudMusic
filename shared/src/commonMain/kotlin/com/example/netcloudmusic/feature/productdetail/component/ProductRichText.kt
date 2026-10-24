package com.example.netcloudmusic.feature.productdetail.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.netcloudmusic.core.util.ResourceUtil

/**
 * 商品详情 HTML 渲染器。
 *
 * Compose 参考工程使用 HtmlText（Jsoup + Android 实现），但 Jsoup 不能放进
 * commonMain。这里使用 KMP 兼容的轻量解析器，覆盖商品详情接口常见的
 * p、br、strong、b、em、i、u、s、h1~h3、ul、ol、li、img、hr 标签。
 * 图片仍然使用 Coil 3，因此 Android 和 iOS 都会通过同一套 Compose UI 加载。
 */
@Composable
fun ProductRichText(
    html: String,
    modifier: Modifier = Modifier,
) {
    val blocks = parseProductHtml(html)
    Column(modifier = modifier.fillMaxWidth()) {
        blocks.forEach { block ->
            when (block) {
                is RichBlock.Text -> {
                    if (block.value.isNotEmpty()) {
                        Text(
                            text = block.value,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                is RichBlock.Image -> {
                    AsyncImage(
                        model = ResourceUtil.r(block.url),
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                RichBlock.Divider -> HorizontalDivider()
                RichBlock.Break -> Spacer(Modifier.height(8.dp))
            }
        }
    }
}

private sealed interface RichBlock {
    data class Text(val value: AnnotatedString) : RichBlock
    data class Image(val url: String) : RichBlock
    data object Break : RichBlock
    data object Divider : RichBlock
}

private data class TextStyleState(
    val bold: Boolean = false,
    val italic: Boolean = false,
    val underline: Boolean = false,
    val lineThrough: Boolean = false,
    val heading: Int = 0,
) {
    fun spanStyle(): SpanStyle = SpanStyle(
        fontWeight = if (bold) FontWeight.Bold else null,
        fontStyle = if (italic) FontStyle.Italic else null,
        textDecoration = when {
            underline && lineThrough -> TextDecoration.combine(
                listOf(TextDecoration.Underline, TextDecoration.LineThrough)
            )
            underline -> TextDecoration.Underline
            lineThrough -> TextDecoration.LineThrough
            else -> null
        },
        fontSize = when (heading) {
            1 -> 22.sp
            2 -> 20.sp
            3 -> 18.sp
            else -> 16.sp
        },
    )
}

private fun parseProductHtml(html: String): List<RichBlock> {
    if (html.isBlank()) return emptyList()

    val blocks = mutableListOf<RichBlock>()
    var text = AnnotatedString.Builder()
    val styles = ArrayDeque<TextStyleState>()
    styles.addLast(TextStyleState())

    fun flushText() {
        if (text.length > 0) {
            blocks += RichBlock.Text(text.toAnnotatedString())
            text = AnnotatedString.Builder()
        }
    }

    fun append(value: String) {
        val normalized = decodeHtmlEntities(value)
        if (normalized.isNotEmpty()) {
            text.pushStyle(styles.last().spanStyle())
            text.append(normalized)
            text.pop()
        }
    }

    val tokenRegex = Regex("(?is)<[^>]*>|[^<]+")
    tokenRegex.findAll(html).forEach { match ->
        val token = match.value
        if (!token.startsWith("<")) {
            append(token.replace(Regex("\\s+"), " "))
            return@forEach
        }

        val tag = token.trim().removePrefix("<").removeSuffix(">").trim()
        if (tag.startsWith("!--")) return@forEach
        val closing = tag.startsWith("/")
        val name = tag
            .removePrefix("/")
            .removeSuffix("/")
            .trim()
            .substringBefore(' ')
            .lowercase()

        if (closing) {
            if (name in setOf("strong", "b", "em", "i", "u", "s", "strike", "h1", "h2", "h3")) {
                if (styles.size > 1) styles.removeLast()
            }
            if (name in setOf("p", "div", "section", "li", "h1", "h2", "h3")) {
                flushText()
                blocks += RichBlock.Break
            }
            return@forEach
        }

        when (name) {
            "img" -> {
                flushText()
                Regex("""(?i)\bsrc\s*=\s*["']([^"']+)["']""").find(tag)
                    ?.groupValues?.getOrNull(1)
                    ?.takeIf { it.isNotBlank() }
                    ?.let { blocks += RichBlock.Image(it) }
            }
            "br" -> {
                flushText()
                blocks += RichBlock.Break
            }
            "hr" -> {
                flushText()
                blocks += RichBlock.Divider
            }
            "p", "div", "section", "li", "h1", "h2", "h3" -> {
                flushText()
                if (name == "li") append("• ")
                val previous = styles.last()
                styles.addLast(
                    previous.copy(
                        bold = previous.bold || name in setOf("h1", "h2", "h3"),
                        heading = if (name.startsWith("h")) name.last() - '0' else previous.heading,
                    )
                )
            }
            "strong", "b", "em", "i", "u", "s", "strike" -> {
                val previous = styles.last()
                styles.addLast(
                    previous.copy(
                        bold = previous.bold || name in setOf("strong", "b"),
                        italic = previous.italic || name in setOf("em", "i"),
                        underline = previous.underline || name == "u",
                        lineThrough = previous.lineThrough || name in setOf("s", "strike"),
                    )
                )
            }
            "ul", "ol" -> {
                flushText()
                blocks += RichBlock.Break
            }
        }
    }
    flushText()
    return blocks
}

private fun decodeHtmlEntities(value: String): String =
    value.replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")
        .replace("&quot;", "\"")
        .replace("&#39;", "'")
