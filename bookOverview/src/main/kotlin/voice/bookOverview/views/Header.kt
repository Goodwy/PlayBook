package voice.bookOverview.views

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.BookOverviewViewState
import voice.common.R
import voice.common.constants.SORTING_AUTHOR
import voice.common.constants.SORTING_LAST
import voice.common.constants.SORTING_NAME

//@Composable
//internal fun Header(
//  category: BookOverviewCategory,
//  modifier: Modifier = Modifier,
//) {
//  Text(
//    modifier = modifier,
//    text = stringResource(id = category.nameRes),
//    //style = MaterialTheme.typography.headlineSmall,
//  )
//}

@Composable
internal fun Header(
  category: BookOverviewCategory,
  modifier: Modifier = Modifier,
  sortBooks: (Int, Int, Int) -> Unit,
  viewState: BookOverviewViewState,
) {
  Box(
    modifier = modifier,
    contentAlignment = Alignment.Center
  ) {
    Text(
      modifier = Modifier.align(Alignment.CenterStart),
      text = stringResource(id = category.nameRes),
      //style = MaterialTheme.typography.headlineSmall,
    )

    var expanded by remember { mutableStateOf(false) }
    Row(
      modifier = Modifier
        .align(Alignment.CenterEnd)
        .clickable { expanded = !expanded },
      verticalAlignment = Alignment.CenterVertically,
    ) {
      val useMenuIcons = !viewState.useMenuIconsPref
      if (useMenuIcons) Text(
        text = stringResource(id = R.string.pref_sorting),
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.primary,
        maxLines = 1,
      )
      val icon = if (useMenuIcons) Icons.Rounded.ExpandMore else Icons.AutoMirrored.Rounded.Sort
      Icon(
        modifier = Modifier.padding(top = 1.dp).size(18.dp),
        imageVector = icon,
        contentDescription = stringResource(id = R.string.pref_sorting),
        tint = MaterialTheme.colorScheme.primary,
      )
    }

    DropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      val viewStateSorted = when (category) {
        BookOverviewCategory.CURRENT_BY_LAST,
        BookOverviewCategory.CURRENT_BY_NAME,
        BookOverviewCategory.CURRENT_BY_AUTHOR -> viewState.sortingCurrent

        BookOverviewCategory.NOT_STARTED_BY_LAST,
        BookOverviewCategory.NOT_STARTED_BY_NAME,
        BookOverviewCategory.NOT_STARTED_BY_AUTHOR -> viewState.sortingNotStarted

        BookOverviewCategory.FINISHED_BY_LAST,
        BookOverviewCategory.FINISHED_BY_NAME,
        BookOverviewCategory.FINISHED_BY_AUTHOR -> viewState.sortingFinished
      }

      DropdownMenuItem(
        onClick = {
          expanded = false
          sortBooksName(category, sortBooks, viewState)
        },
        text = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            RadioButton(
              onClick = {
                expanded = false
                sortBooksName(category, sortBooks, viewState)
              },
              selected = viewStateSorted == SORTING_NAME,
            )
            Text(text = stringResource(id = voice.strings.R.string.migration_detail_content_name))
          }
        },
        trailingIcon = {
          Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageVector = Icons.Rounded.SortByAlpha,
            contentDescription = stringResource(id = voice.strings.R.string.migration_detail_content_name),
          )
        },
      )

      DropdownMenuItem(
        onClick = {
          expanded = false
          sortBooksLast(category, sortBooks, viewState)
        },
        text = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            RadioButton(
              onClick = {
                expanded = false
                sortBooksLast(category, sortBooks, viewState)
              },
              selected = viewStateSorted == SORTING_LAST,
            )
            Text(text = stringResource(id = R.string.pref_sorting_last))
          }
        },
        trailingIcon = {
          Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageVector = Icons.Rounded.AccessTime,
            contentDescription = stringResource(id = R.string.pref_sorting_last),
          )
        },
      )

      DropdownMenuItem(
        onClick = {
          expanded = false
          sortBooksAuthor(category, sortBooks, viewState)
        },
        text = {
          Row(
            verticalAlignment = Alignment.CenterVertically,
          ) {
            RadioButton(
              onClick = {
                expanded = false
                sortBooksAuthor(category, sortBooks, viewState)
              },
              selected = viewStateSorted == SORTING_AUTHOR,
            )
            Text(text = stringResource(id = voice.strings.R.string.cover_search_author))
          }
        },
        trailingIcon = {
          Icon(
            modifier = Modifier.padding(horizontal = 8.dp),
            imageVector = Icons.Rounded.Person,
            contentDescription = stringResource(id = voice.strings.R.string.cover_search_author),
          )
        },
      )
    }
  }
}

private fun sortBooksName(
  category: BookOverviewCategory,
  sortBooks: (Int, Int, Int) -> Unit,
  viewState: BookOverviewViewState,
) {
  when (category) {
    BookOverviewCategory.NOT_STARTED_BY_LAST,
    BookOverviewCategory.NOT_STARTED_BY_NAME,
    BookOverviewCategory.NOT_STARTED_BY_AUTHOR -> sortBooks(SORTING_NAME, viewState.sortingFinished, viewState.sortingCurrent)

    BookOverviewCategory.FINISHED_BY_LAST,
    BookOverviewCategory.FINISHED_BY_NAME,
    BookOverviewCategory.FINISHED_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, SORTING_NAME, viewState.sortingCurrent)

    BookOverviewCategory.CURRENT_BY_LAST,
    BookOverviewCategory.CURRENT_BY_NAME,
    BookOverviewCategory.CURRENT_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, viewState.sortingFinished, SORTING_NAME)
  }
}

private fun sortBooksLast(
  category: BookOverviewCategory,
  sortBooks: (Int, Int, Int) -> Unit,
  viewState: BookOverviewViewState
) {
  when (category) {
    BookOverviewCategory.NOT_STARTED_BY_LAST,
    BookOverviewCategory.NOT_STARTED_BY_NAME,
    BookOverviewCategory.NOT_STARTED_BY_AUTHOR -> sortBooks(SORTING_LAST, viewState.sortingFinished, viewState.sortingCurrent)

    BookOverviewCategory.FINISHED_BY_LAST,
    BookOverviewCategory.FINISHED_BY_NAME,
    BookOverviewCategory.FINISHED_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, SORTING_LAST, viewState.sortingCurrent)

    BookOverviewCategory.CURRENT_BY_LAST,
    BookOverviewCategory.CURRENT_BY_NAME,
    BookOverviewCategory.CURRENT_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, viewState.sortingFinished, SORTING_LAST)
  }
}


private fun sortBooksAuthor(
  category: BookOverviewCategory,
  sortBooks: (Int, Int, Int) -> Unit,
  viewState: BookOverviewViewState
) {
  when (category) {
    BookOverviewCategory.NOT_STARTED_BY_LAST,
    BookOverviewCategory.NOT_STARTED_BY_NAME,
    BookOverviewCategory.NOT_STARTED_BY_AUTHOR -> sortBooks(SORTING_AUTHOR, viewState.sortingFinished, viewState.sortingCurrent)

    BookOverviewCategory.FINISHED_BY_LAST,
    BookOverviewCategory.FINISHED_BY_NAME,
    BookOverviewCategory.FINISHED_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, SORTING_AUTHOR, viewState.sortingCurrent)

    BookOverviewCategory.CURRENT_BY_LAST,
    BookOverviewCategory.CURRENT_BY_NAME,
    BookOverviewCategory.CURRENT_BY_AUTHOR -> sortBooks(viewState.sortingNotStarted, viewState.sortingFinished, SORTING_AUTHOR)
  }
}
