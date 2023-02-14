package voice.bookOverview.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
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
import voice.common.compose.LongClickableCard
import voice.common.compose.plus
import java.text.DecimalFormat
import kotlin.math.roundToInt

@Composable
internal fun GridBooks(
  books: ImmutableMap<BookOverviewCategory, List<BookOverviewItemViewState>>,
  contentPadding: PaddingValues,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
) {
  val cellCount = gridColumnCount()
  LazyVerticalGrid(
    columns = GridCells.Fixed(cellCount),
    verticalArrangement = Arrangement.spacedBy(8.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    contentPadding = contentPadding + PaddingValues(start = 12.dp, end = 12.dp, top = 8.dp, bottom = 102.dp),
  ) {
    books.forEach { (category, books) ->
      if (books.isEmpty()) return@forEach
      item(
        span = { GridItemSpan(maxLineSpan) },
        key = category,
        contentType = "header",
      ) {
        Header(
          modifier = Modifier.padding(top = 8.dp, bottom = 4.dp, start = 8.dp, end = 8.dp),
          category = category,
        )
      }
      items(
        items = books,
        key = { it.id },
        contentType = { "item" },
      ) { book ->
        GridBook(
          book = book,
          onBookClick = onBookClick,
          onBookLongClick = onBookLongClick,
        )
      }
    }
  }
}

@Composable
internal fun GridBook(
  book: BookOverviewItemViewState,
  onBookClick: (BookId) -> Unit,
  onBookLongClick: (BookId) -> Unit,
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
    modifier = Modifier.fillMaxWidth(),
  ) {
    Column {
      Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.BottomStart
      ) {
        AsyncImage(
          modifier = Modifier
            .aspectRatio(1f, true) //4F / 3F
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .shadow(
              elevation = 4.dp,
              shape = RoundedCornerShape(8.dp),
              clip = true,
              ambientColor = MaterialTheme.colorScheme.onBackground,
              spotColor = MaterialTheme.colorScheme.onBackground,
            )
            .clip(RoundedCornerShape(8.dp)),
          contentScale = ContentScale.Crop,
          model = book.cover?.file,
          placeholder = painterResource(id = R.drawable.album_art),
          error = painterResource(id = R.drawable.album_art),
          contentDescription = null,
        )
        Row(
          modifier = Modifier
            .wrapContentSize()
            .padding(start = 16.dp, bottom = 6.dp)
            .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
            .padding(start = 2.dp, end = 3.dp, top = 2.dp, bottom = 2.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box(contentAlignment = Alignment.Center) {
            Icon(
              modifier = Modifier.size(16.dp),
              imageVector = Icons.Rounded.RadioButtonUnchecked,
              contentDescription = null,
              tint = Color.White
            )
            BookProgress(progress = book.progress, bgColor = Color.White, primaryColor = Color.White)
          }
          Spacer(modifier = Modifier.size(4.dp))
          Text(
            text = DecimalFormat("0").format(book.progress * 100).toString() + "%",
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
          )
        }
      }
      Text(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 6.dp),
        text = book.name,
        lineHeight = 16.sp,
        overflow = TextOverflow.Ellipsis,
        maxLines = 3,
        style = MaterialTheme.typography.bodyMedium,
      )
      Row(
        modifier = Modifier
          .wrapContentSize()
          .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          modifier = Modifier.size(14.dp),
          //imageVector = Icons.Rounded.Schedule,
          painter = painterResource(id = R.drawable.ic_time_left),
          contentDescription = null,
          tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
          text = book.remainingTime,
          style = MaterialTheme.typography.bodySmall,
        )
      }

      /*if (book.progress > 0F) {
        LinearProgressIndicator(
          modifier = Modifier.fillMaxWidth(),
          progress = book.progress,
        )
      }*/
    }
  }
}

@Composable
internal fun gridColumnCount(): Int {
  val displayMetrics = LocalContext.current.resources.displayMetrics
  val widthPx = displayMetrics.widthPixels.toFloat()
  val desiredPx = with(LocalDensity.current) {
    180.dp.toPx()
  }
  val columns = (widthPx / desiredPx).roundToInt()
  return columns.coerceAtLeast(2)
}
