package com.skipper.taskManager.ui.screens.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TaskCompletionIndicator(
    completedTasks: Int,
    totalTasks: Int,
    radius: Dp = 48.dp,
    strokeWidth: Dp = 8.dp,
    modifier: Modifier = Modifier
) {
    val percentage = if (totalTasks == 0) 0f else completedTasks / totalTasks.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = percentage,
        label = "AnimatedTaskCompletion"
    )
    val colorSchemeLocal = MaterialTheme.colorScheme.primary

    val size = radius * 2

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.size(size)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)
            val diameter = size.toPx()
            val sweep = 360 * animatedProgress

            // Background circle
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = stroke
            )

            // Foreground progress

            drawArc(
                color = colorSchemeLocal,
                startAngle = -90f,
                sweepAngle = sweep,
                useCenter = false,
                style = stroke
            )
        }

        Text(
            text = "${(animatedProgress * 100).toInt()}%",
            style = MaterialTheme.typography.titleSmall,
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
