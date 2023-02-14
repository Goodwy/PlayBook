package voice.bookOverview.views

import androidx.annotation.FloatRange
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
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
import voice.bookOverview.R
import voice.bookOverview.overview.BookOverviewCategory
import voice.bookOverview.overview.BookOverviewItemViewState
import voice.common.BookId
import voice.common.compose.ImmutableFile
import voice.common.compose.LongClickableCard
import voice.common.compose.plus
import voice.common.recomposeHighlighter
import java.text.DecimalFormat

@Composable
internal fun ListBooks(
  contentPadding: PaddingValues,
  books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
) {
  LazyColumn(
    verticalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = contentPadding + PaddingValues(top = 8.dp, start = 10.dp, end = 10.dp, bottom = 146.dp),
  ) {
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
        )
      }
      items(
        items = books,
        key = { it.id.value },
        contentType = { "item" },
      ) { book ->
        ListBookRow(
          book = book,
          onBookClick = onBookClick,
          onBookLongClick = onBookLongClick,
        )
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
) {
  val keyboardController = LocalSoftwareKeyboardController.current
  val scope = rememberCoroutineScope()
  LongClickableCard(
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
    modifier = modifier
      .recomposeHighlighter()
      .fillMaxWidth(),
  ) {
    Column {
      Row {
        CoverImage(book.cover)
        Column(
          modifier = Modifier
            .height(79.dp)
            .padding(start = 12.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
          verticalArrangement = Arrangement.SpaceEvenly
        ) {
          if (book.author != null) {
            Text(
              text = book.author.toUpperCase(LocaleList.current),
              style = MaterialTheme.typography.labelSmall,
              overflow = TextOverflow.Ellipsis,
              maxLines = 1,
            )
          }
          Text(
            text = book.name,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 16.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
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
                //tint = MaterialTheme.colorScheme.primary
              )
              BookProgress(progress = book.progress, primaryColor = MaterialTheme.colorScheme.onBackground)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Text(
              text = DecimalFormat("0").format(book.progress * 100).toString() + "%",
              style = MaterialTheme.typography.bodySmall,
            )
            Spacer(modifier = Modifier.size(16.dp))
            Icon(
              modifier = Modifier.size(16.dp),
              //imageVector = Icons.Rounded.Schedule,
              painter = painterResource(id = R.drawable.ic_time_left),
              contentDescription = null,
              //tint = MaterialTheme.colorScheme.primary,
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(
              text = book.remainingTime,
              style = MaterialTheme.typography.bodySmall,
            )
          }
        }
      }

      /*if (book.progress > 0.05) {
        LinearProgressIndicator(
          modifier = Modifier.fillMaxWidth(),
          progress = book.progress,
        )
      }*/
    }
  }
}

@Composable
private fun CoverImage(cover: ImmutableFile?) {
  AsyncImage(
    modifier = Modifier
      .recomposeHighlighter()
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
    placeholder = painterResource(id = R.drawable.album_art),
    error = painterResource(id = R.drawable.album_art),
    contentDescription = null,
  )
}
