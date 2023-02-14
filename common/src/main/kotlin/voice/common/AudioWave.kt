package voice.common

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun AudioWave(
	isPlaying: Boolean,
	modifier: Modifier = Modifier
) {

	val transition1 = rememberInfiniteTransition()
	val transition2 = rememberInfiniteTransition()
	val transition3 = rememberInfiniteTransition()
  val transition4 = rememberInfiniteTransition()
  val circle = RoundedCornerShape(25)

	val fraction1 by transition1.animateFloat(
		initialValue = 0.2f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			repeatMode = RepeatMode.Reverse,
			animation = tween(
				durationMillis = 1200,
				easing = LinearEasing
			)
		)
	)
	val fraction2 by transition2.animateFloat(
		initialValue = 0.2f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			repeatMode = RepeatMode.Reverse,
			animation = tween(
				durationMillis = 800,
				easing = LinearEasing
			)
		)
	)
	val fraction3 by transition3.animateFloat(
		initialValue = 0.2f,
		targetValue = 1f,
		animationSpec = infiniteRepeatable(
			repeatMode = RepeatMode.Reverse,
			animation = tween(
				durationMillis = 1000,
				easing = LinearEasing
			)
		)
	)
  val fraction4 by transition4.animateFloat(
    initialValue = 0.2f,
    targetValue = 1f,
    animationSpec = infiniteRepeatable(
      repeatMode = RepeatMode.Reverse,
      animation = tween(
        durationMillis = 600,
        easing = LinearEasing
      )
    )
  )

	Row(
		verticalAlignment = Alignment.Bottom,
		horizontalArrangement = Arrangement.SpaceEvenly,
		modifier = modifier
			.size(
				width = 20.dp,
				height = 10.dp
			)
	) {
		Box(
			modifier = Modifier
				.width(3.dp)
				.fillMaxHeight(if (!isPlaying) 0.3f else fraction1)
				.clip(circle)
				.background(MaterialTheme.colorScheme.primary)
		)
		Box(
			modifier = Modifier
				.width(3.dp)
				.fillMaxHeight(if (!isPlaying) 0.5f else fraction2)
				.clip(circle)
				.background(MaterialTheme.colorScheme.primary)
		)
		Box(
			modifier = Modifier
				.width(3.dp)
				.fillMaxHeight(if (!isPlaying) 0.3f else fraction3)
				.clip(circle)
				.background(MaterialTheme.colorScheme.primary)
		)
    Box(
      modifier = Modifier
        .width(3.dp)
        .fillMaxHeight(if (!isPlaying) 0.2f else fraction4)
        .clip(circle)
        .background(MaterialTheme.colorScheme.primary)
    )
	}
}
