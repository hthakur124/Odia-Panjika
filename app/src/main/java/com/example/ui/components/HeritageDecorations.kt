package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.CreamOutline
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.VermilionDark
import com.example.ui.theme.VermilionRed
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Curved Maroon / Vermilion heritage header matching the reference Odia Panjika layout,
 * accented with a delicate Konark sun-wheel motif in the corner.
 */
@Composable
fun PattachitraHeaderContainer(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val paletteColors = com.example.ui.theme.LocalOdiaColors.current
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .drawBehind {
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(paletteColors.headerStart, paletteColors.headerEnd)
                    )
                )

                // Subtle Konark sun-wheel decorative motif in top-right
                val center = Offset(size.width * 0.92f, size.height * 0.25f)
                val outerRadius = size.minDimension * 0.56f
                val innerRadius = outerRadius * 0.34f
                val wheelColor = SaffronHighlight.copy(alpha = 0.11f)

                drawCircle(
                    color = wheelColor,
                    radius = outerRadius,
                    center = center,
                    style = Stroke(width = 2.dp.toPx())
                )
                drawCircle(
                    color = wheelColor,
                    radius = innerRadius,
                    center = center,
                    style = Stroke(width = 1.5.dp.toPx())
                )

                val spokes = 12
                for (i in 0 until spokes) {
                    val angle = (2.0 * PI / spokes) * i
                    val startX = center.x + (innerRadius * cos(angle)).toFloat()
                    val startY = center.y + (innerRadius * sin(angle)).toFloat()
                    val endX = center.x + (outerRadius * cos(angle)).toFloat()
                    val endY = center.y + (outerRadius * sin(angle)).toFloat()
                    drawLine(
                        color = wheelColor,
                        start = Offset(startX, startY),
                        end = Offset(endX, endY),
                        strokeWidth = 1.2.dp.toPx()
                    )
                }
            },
        content = content
    )
}

/**
 * Lunar badge for Purnima (Full Moon) and Amavasya (New Moon).
 */
@Composable
fun LunarPhaseDot(
    isPurnima: Boolean,
    size: Dp = 9.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val radius = this.size.minDimension / 2f
        if (isPurnima) {
            drawCircle(color = SaffronGold, radius = radius)
            drawCircle(
                color = Color.White.copy(alpha = 0.75f),
                radius = radius * 0.45f,
                center = Offset(center.x - radius * 0.2f, center.y - radius * 0.2f)
            )
        } else {
            drawCircle(color = InkPrimary, radius = radius)
            drawCircle(
                color = SaffronGold.copy(alpha = 0.9f),
                radius = radius - 0.8.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
        }
    }
}

/**
 * Golden solar horizon arc rendered inside the Sunrise/Sunset detail card.
 */
@Composable
fun SolarHorizonArc(
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
    ) {
        val paddingX = 16.dp.toPx()
        val horizonY = size.height - 8.dp.toPx()
        val arcWidth = (size.width - paddingX * 2f).coerceAtLeast(10f)
        val arcHeight = (size.height - 14.dp.toPx()) * 2f

        drawLine(
            color = CreamOutline,
            start = Offset(paddingX - 6.dp.toPx(), horizonY),
            end = Offset(size.width - paddingX + 6.dp.toPx(), horizonY),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawArc(
            brush = Brush.horizontalGradient(
                colors = listOf(SaffronGold, VermilionRed, TempleTeal)
            ),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(paddingX, horizonY - arcHeight / 2f),
            size = Size(arcWidth, arcHeight),
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
        )

        drawCircle(
            color = SaffronGold,
            radius = 5.5.dp.toPx(),
            center = Offset(paddingX, horizonY)
        )
        drawCircle(
            color = SaffronHighlight,
            radius = 4.dp.toPx(),
            center = Offset(size.width / 2f, horizonY - arcHeight / 2f)
        )
        drawCircle(
            color = VermilionRed,
            radius = 5.5.dp.toPx(),
            center = Offset(size.width - paddingX, horizonY)
        )
    }
}
