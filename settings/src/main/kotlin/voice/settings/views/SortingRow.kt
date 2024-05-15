package voice.settings.views

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.SwapVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import voice.common.constants.*
import voice.common.R as CommonR
import voice.strings.R as StringsR

@Composable
internal fun SortingRow(sorting: Int, openSortingDialog: () -> Unit) {
  SettingsRow(
    title = stringResource(CommonR.string.pref_sorting),
    value = when (sorting) {
      SORTING_NAME -> stringResource(StringsR.string.migration_detail_content_name)
      SORTING_LAST -> stringResource(CommonR.string.pref_sorting_last)
      SORTING_AUTHOR -> stringResource(StringsR.string.cover_search_author)
      else -> stringResource(StringsR.string.migration_detail_content_name)+"/"+stringResource(CommonR.string.pref_sorting_last)
    },
    click = openSortingDialog
  )
}

@Composable
internal fun SortingDialog(
  checkedItemId: Int,
  onDismiss: () -> Unit,
  onSelected: (Int) -> Unit
) {
  val items = arrayListOf(
    RadioItem(SORTING_CLASSIC, stringResource(StringsR.string.migration_detail_content_name)+"/"+stringResource(CommonR.string.pref_sorting_last), Icons.Rounded.SwapVert),
    RadioItem(SORTING_NAME, stringResource(StringsR.string.migration_detail_content_name), Icons.Rounded.SortByAlpha),
    RadioItem(SORTING_LAST, stringResource(CommonR.string.pref_sorting_last), Icons.Rounded.AccessTime),
    RadioItem(SORTING_AUTHOR, stringResource(StringsR.string.cover_search_author), Icons.Rounded.Person)
  )
  RadioButtonDialog(
    title = stringResource(CommonR.string.pref_sorting),
    items = items,
    checkedItemId = checkedItemId,
    onDismiss = onDismiss,
    onSelected = onSelected
  )
}
