package voice.bookOverview.views

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.BookOverviewItemViewState
import voice.bookOverview.overview.BookOverviewViewState
import voice.common.BookId
import voice.common.compose.ImmutableFile
import java.text.DecimalFormat
import voice.common.R as CommonR

@Composable
internal fun ListBooks(
  books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
  showPermissionBugCard: Boolean,
  onPermissionBugCardClick: () -> Unit,
  currentBook: BookId?,
  sortBooks: (Int, Int, Int) -> Unit,
  viewState: BookOverviewViewState,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = PaddingValues(top = 8.dp, start = 10.dp, end = 10.dp, bottom = 146.dp),
  ) {
    if (showPermissionBugCard) {
      item {
        PermissionBugCard(onPermissionBugCardClick)
      }
    }
    books.forEach { (category, books) ->
      if (books.isEmpty()) return@forEach
      stickyHeader(
        key = category,
        contentType = "header",
      ) {
        Header(
          modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 8.dp, horizontal = 12.dp),
          category = category,
          sortBooks = sortBooks,
          viewState = viewState,
        )
      }
      items(
        items = books,
        key = { it.id.value },
        contentType = { "item" },
      ) { book ->
        val isCurrentBook = book.id == currentBook
        ListBookRow(
          book = book,
          onBookClick = onBookClick,
          onBookLongClick = onBookLongClick,
          isCurrentBook = isCurrentBook,
        )
      }
      item {
        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
      }
    }
  }
}

@Composable
internal fun ListBookRow(
  book: BookOverviewItemViewState,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
  modifier: Modifier = Modifier,
  hideKeyboard: Boolean = false,
  isCurrentBook: Boolean = false,
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val scope = rememberCoroutineScope()
  Card(
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f)),
    modifier = modifier
      .fillMaxWidth()
      .combinedClickable(
        onClick = {
          if (hideKeyboard)
            scope.launch {
              keyboardController?.hide()
              delay(100)
              onBookClick(book.id)
            }
          else onBookClick(book.id)
        },
        onLongClick = {
          if (hideKeyboard)
            scope.launch {
              keyboardController?.hide()
              delay(100)
              onBookLongClick(book.id)
            }
          else onBookLongClick(book.id)
        },
      ),
  ) {
    Column {
      Row {
        CoverImage(book.cover)
        Column(
          modifier = Modifier
            .height(79.dp)
            .padding(start = 12.dp, end = 8.dp),
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          if (book.author != null) {
            Text(
              text = book.author.toUpperCase(LocaleList.current),
              style = MaterialTheme.typography.labelSmall,
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
              color = if (isCurrentBook) MaterialTheme.colorScheme.primary else Color.Unspecified,
            )
          }
          Text(
            modifier = Modifier
              .padding(bottom = 2.dp)
              .heightIn(min = 14.dp, max = 38.dp),
            text = book.name,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 14.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            color = if (isCurrentBook) MaterialTheme.colorScheme.primary else Color.Unspecified,
          )
          Row(
            modifier = Modifier
              .wrapContentSize(),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Box(contentAlignment = Alignment.Center) {
              Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Rounded.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (isCurrentBook) MaterialTheme.colorScheme.primary else LocalContentColor.current,
              )
              BookProgress(
                progress = book.progress,
                bgColor = if (isCurrentBook) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                primaryColor = if (isCurrentBook) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
              )
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
              text = DecimalFormat("0").format(book.progress * 100).toString() + "%",
              style = MaterialTheme.typography.bodySmall,
              color = if (isCurrentBook) MaterialTheme.colorScheme.primary else Color.Unspecified,
            )
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
              modifier = Modifier.size(16.dp),
              //imageVector = Icons.Rounded.Schedule,
              painter = painterResource(id = CommonR.drawable.ic_time_left),
              contentDescription = null,
              tint = if (isCurrentBook) MaterialTheme.colorScheme.primary else LocalContentColor.current,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
              text = book.remainingTime,
              style = MaterialTheme.typography.bodySmall,
              color = if (isCurrentBook) MaterialTheme.colorScheme.primary else Color.Unspecified,
            )
          }
        }
      }

      /*if (book.progress > 0.05) {
        LinearProgressIndicator(
          modifier = Modifier.fillMaxWidth(),
          progress = { book.progress },
        )
      }*/
    }
  }
}

@Composable
private fun CoverImage(cover: ImmutableFile?) {
  AsyncImage(
    modifier = Modifier
      .padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
      .size(63.dp) //76.dp
      .shadow(
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        clip = true,
        ambientColor = MaterialTheme.colorScheme.onBackground,
        spotColor = MaterialTheme.colorScheme.onBackground,
      )
      .clip(RoundedCornerShape(8.dp)),
    contentScale = ContentScale.Crop,
    model = cover?.file,
    placeholder = painterResource(id = CommonR.drawable.album_art),
    error = painterResource(id = CommonR.drawable.album_art),
    contentDescription = null,
  )
}
