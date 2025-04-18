package voice.bookOverview.views

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
internal fun BookProgress(@FloatRange(from = 0.0, to = 1.0) progress: Float,
                          bgColor: Color = MaterialTheme.colorScheme.onBackground,
                          primaryColor: Color = MaterialTheme.colorScheme.primary
) {
  val backgroundColor = bgColor.copy(alpha = 0.2f)
  val stroke = with(LocalDensity.current) { Stroke(width = 5.dp.toPx()) }
  Canvas(
    modifier = Modifier
      .size(16.dp)
      .padding(3.5.dp)
  ) {
    val startAngle = 270f
    val sweepAngle = progress * 360f
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    // Progress background
    drawArc(
      color = backgroundColor,
      startAngle = startAngle,
      sweepAngle = 360f,
      useCenter = false,
      topLeft = Offset(diameterOffset, diameterOffset),
      size = Size(arcDimen, arcDimen),
      style = stroke
    )
    // Progress
    drawArc(
      color = primaryColor.copy(alpha = 0.8f),
      startAngle = startAngle,
      sweepAngle = sweepAngle,
      useCenter = false,
      topLeft = Offset(diameterOffset, diameterOffset),
      size = Size(arcDimen, arcDimen),
      style = stroke
    )
  }
}
