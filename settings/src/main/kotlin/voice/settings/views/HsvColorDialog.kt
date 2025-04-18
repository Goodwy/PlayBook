package voice.settings.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.github.skydoves.colorpicker.compose.AlphaTile
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
internal fun HsvColorDialog(
  title: String? = null,
  checkedItemId: Int = -1,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit,
) {
  var selectedColor: Int? by remember { mutableStateOf(null) }
  var selectedColorHex by remember { mutableStateOf("") }
  if (checkedItemId != -1) selectedColor = checkedItemId

  var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }
  val configuration = LocalConfiguration.current
  LaunchedEffect(configuration) {
    snapshotFlow { configuration.orientation }
      .collect { orientation = it }
  }

  AlertDialog(
    onDismissRequest = {
      onDismiss()
    },
    confirmButton = {
      ConfirmButton(
        enabled = selectedColor != null,
        onConfirm = {
          onSelected(selectedColor!!)
          onDismiss()
        },
      )
    },
    dismissButton = {
      DismissButton(onDismiss)
    },
    title = {
      if (title != null) Text(
        text = title,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
      )
    },
    text = {
      when (orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
          Column (
            //verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
          ) {
            Text(
              modifier = Modifier
                .padding(horizontal = 2.dp),
              text = stringResource(id = CommonR.string.pref_basic_color_summary),
              textAlign = TextAlign.Center,
              fontSize = 13.sp,
              lineHeight = 13.sp,
            )
            val controller = rememberColorPickerController()
            controller.setWheelColor(Color.DarkGray)
            HsvColorPicker(
              modifier = Modifier
                .heightIn(min = 200.dp, max = 300.dp)
                .wrapContentWidth()
                .padding(10.dp),
              controller = controller,
              onColorChanged = { colorEnvelope: ColorEnvelope ->
                selectedColor = colorEnvelope.color.toArgb()
                selectedColorHex = colorEnvelope.hexCode
              }
            )
            BrightnessSlider(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(24.dp)
                .clip(RoundedCornerShape(16.dp)),
              controller = controller,
              wheelColor = Color.DarkGray,
            )
            Spacer(modifier = Modifier.size(24.dp))
            Row(
              horizontalArrangement = Arrangement.Center,
              verticalAlignment = Alignment.CenterVertically,
            ) {
              Box(
                modifier = Modifier
                  .size(80.dp)
                  .clip(RoundedCornerShape(24.dp))
                  .background(color = MaterialTheme.colorScheme.onSurface),
                contentAlignment = Alignment.Center,
              ) {
                AlphaTile(
                  modifier = Modifier
                    .size(80.dp)
                    .padding(1.dp)
                    .clip(RoundedCornerShape(23.dp)),
                  controller = controller,
                  tileEvenColor = Color(checkedItemId)
                )
              }
              Spacer(modifier = Modifier.size(24.dp))
              Column (
                verticalArrangement = Arrangement.SpaceBetween,
              ) {
                Spacer(modifier = Modifier.size(4.dp))
                Text(text = "#" + selectedColorHex)
                Spacer(modifier = Modifier.size(12.dp))
                Button(
                  shape = RoundedCornerShape(12.dp),
                  contentPadding = PaddingValues(
                    start = 14.dp,
                    top = 4.dp,
                    end = 14.dp,
                    bottom = 4.dp
                  ),
                  onClick = {
                    onSelected(Color(red = 76, green = 134, blue = 203).toArgb())
                    onDismiss()
                  },
                ) {
                  Text(
                    text = stringResource(id = CommonR.string.pref_default_theme),
                    overflow = TextOverflow.Ellipsis,
                  )
                }
              }
            }
          }
        }
        else -> {
          Row {
            val controller = rememberColorPickerController()
            controller.setWheelColor(Color.DarkGray)
            Column (
              modifier = Modifier.weight(0.8f).fillMaxHeight(),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              HsvColorPicker(
                modifier = Modifier
                  .heightIn(min = 200.dp, max = 300.dp)
                  .wrapContentWidth()
                  .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                  selectedColor = colorEnvelope.color.toArgb()
                  selectedColorHex = colorEnvelope.hexCode
                }
              )
            }
            Column (
              modifier = Modifier.weight(1f).fillMaxHeight(),
              verticalArrangement = Arrangement.Center,
              horizontalAlignment = Alignment.CenterHorizontally,
            ) {
              Text(
                text = stringResource(id = CommonR.string.pref_basic_color_summary),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                lineHeight = 12.sp,
              )
              Spacer(modifier = Modifier.size(16.dp))
              BrightnessSlider(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(horizontal = 26.dp)
                  .height(24.dp)
                  .clip(RoundedCornerShape(16.dp)),
                controller = controller,
                wheelColor = Color.DarkGray,
              )
              Spacer(modifier = Modifier.size(20.dp))
              Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
              ) {
                Box(
                  modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = MaterialTheme.colorScheme.onSurface),
                  contentAlignment = Alignment.Center,
                ) {
                  AlphaTile(
                    modifier = Modifier
                      .size(80.dp)
                      .padding(1.dp)
                      .clip(RoundedCornerShape(23.dp)),
                    controller = controller,
                    tileEvenColor = Color(checkedItemId)
                  )
                }
                Spacer(modifier = Modifier.size(24.dp))
                Column (
                  verticalArrangement = Arrangement.SpaceBetween,
                ) {
                  Spacer(modifier = Modifier.size(4.dp))
                  Text(text = "#" + selectedColorHex)
                  Spacer(modifier = Modifier.size(12.dp))
                  Button(
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(
                      start = 14.dp,
                      top = 4.dp,
                      end = 14.dp,
                      bottom = 4.dp
                    ),
                    onClick = {
                      onSelected(Color(red = 76, green = 134, blue = 203).toArgb())
                      onDismiss()
                    },
                  ) {
                    Text(
                      text = stringResource(id = CommonR.string.pref_default_theme),
                      overflow = TextOverflow.Ellipsis,
                    )
                  }
                }
              }
            }
          }
        }
      }

    },
    properties = DialogProperties(usePlatformDefaultWidth = orientation == Configuration.ORIENTATION_PORTRAIT)
  )
}

@Composable
private fun ConfirmButton(enabled: Boolean, onConfirm: () -> Unit) {
  TextButton(
    enabled = enabled,
    onClick = onConfirm,
  ) {
    Text(text = stringResource(id = StringsR.string.dialog_confirm))
  }
}

@Composable
private fun DismissButton(onDismiss: () -> Unit) {
  TextButton(
    onClick = {
      onDismiss()
    },
  ) {
    Text(text = stringResource(id = StringsR.string.dialog_cancel))
  }
}
