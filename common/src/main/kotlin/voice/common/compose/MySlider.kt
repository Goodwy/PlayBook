package voice.common.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp

/**
 * The Default thumb for [Slider] and [RangeSlider]
 *
 * @param interactionSource the [MutableInteractionSource] representing the stream of
 *   [Interaction]s for this thumb. You can create and pass in your own `remember`ed instance to
 *   observe
 * @param modifier the [Modifier] to be applied to the thumb.
 * @param colors [SliderColors] that will be used to resolve the colors used for this thumb in
 *   different states. See [SliderDefaults.colors].
 * @param enabled controls the enabled state of this slider. When `false`, this component will
 *   not respond to user input, and it will appear visually disabled and disabled to
 *   accessibility services.
 * @param thumbSize the size of the thumb.
 */
@Composable
fun MyThumb(
  interactionSource: MutableInteractionSource,
  modifier: Modifier = Modifier,
//  colors: SliderColors = colors(),
//  enabled: Boolean = true,
  thumbSize: DpSize = DpSize(16.dp, 16.dp)
) {
  val interactions = remember { mutableStateListOf<Interaction>() }
  LaunchedEffect(interactionSource) {
    interactionSource.interactions.collect { interaction ->
      when (interaction) {
        is PressInteraction.Press -> interactions.add(interaction)
        is PressInteraction.Release -> interactions.remove(interaction.press)
        is PressInteraction.Cancel -> interactions.remove(interaction.press)
        is DragInteraction.Start -> interactions.add(interaction)
        is DragInteraction.Stop -> interactions.remove(interaction.start)
        is DragInteraction.Cancel -> interactions.remove(interaction.start)
      }
    }
  }
  Spacer(
    modifier
      .size(thumbSize)
      .shadow(2.dp, RoundedCornerShape(16.dp))
      .hoverable(interactionSource = interactionSource)
      .background(Color.White, RoundedCornerShape(8.dp))
  )
}

@Stable
internal fun SliderColors.thumbColor(enabled: Boolean): Color =
  if (enabled) thumbColor else disabledThumbColor
