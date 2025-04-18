package voice.bookOverview.views

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import voice.common.compose.ImmutableFile
import voice.data.Book
import voice.logging.core.Logger
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds
import voice.strings.R as StringsR
import voice.common.R as CommonR

@Composable
fun BookDetailsDialog(
  cover: ImmutableFile?,
  book: Book,
  onDismiss: () -> Unit,
) {
  Dialog(
    onDismissRequest = onDismiss,
    content = {
      val selectBookTitle = book.content.name
      val selectBookChaptersSize = book.content.chapters.size
      val selectBookDuration = book.duration / 1000

      Column(
        modifier = Modifier
          .wrapContentHeight()
          .fillMaxWidth()
          .padding(16.dp)
          .background(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(24.dp),
          ),
      ) {
        //header
        Row(
          modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 12.dp),
          verticalAlignment = Alignment.Top,
        ) {
          Row(
            modifier = Modifier
              .wrapContentHeight()
              .padding(top = 4.dp, bottom = 8.dp)
              .weight(1f),
            verticalAlignment = Alignment.CenterVertically,
          ) {
            Cover(cover = cover, size = 56.dp, cornerRadius = 8.dp)
            Text(
              modifier = Modifier
                .heightIn(min = 20.dp, max = 56.dp)
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
                .padding(top = 3.dp),
              text = selectBookTitle,
              fontSize = 16.sp,
              lineHeight = 16.sp,
              overflow = TextOverflow.Ellipsis,
              maxLines = 3,
            )
          }
          Box(
            modifier = Modifier
              .size(32.dp)
              .combinedClickable(
                onClick = { onDismiss() },
                indication = ripple(bounded = false, radius = 20.dp),
                interactionSource = remember { MutableInteractionSource() },
              ),
            contentAlignment = Alignment.Center,
          ) {
            Icon(
              modifier = Modifier
                .size(32.dp)
                .alpha(0.2f),
              imageVector = Icons.Rounded.Circle,
              contentDescription = stringResource(id = StringsR.string.close),
              tint = contentColorFor(MaterialTheme.colorScheme.background)
            )
            Icon(
              modifier = Modifier
                .size(32.dp)
                .padding(5.dp),
              imageVector = Icons.Rounded.Close,
              contentDescription = stringResource(id = StringsR.string.close),
              tint = contentColorFor(MaterialTheme.colorScheme.background)
            )
          }
        }

        val scrollState = rememberScrollState()
        Box(modifier = Modifier.padding(bottom = 10.dp)) {
          Column(
            modifier = Modifier
              .verticalScroll(scrollState)
              .padding(bottom = 6.dp),
          ) {
            Spacer(modifier = Modifier.size(6.dp))
            Card(
              modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .fillMaxWidth(),
              shape = RoundedCornerShape(8.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHighest),
              elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
              book.content.author?.let {
                BookDetailsRow(
                  title = stringResource(CommonR.string.change_book_author),
                  value = it
                )
                HorizontalDivider(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  thickness = Dp.Hairline
                )
              }
              BookDetailsRow(
                title = stringResource(CommonR.string.length),
                value = selectBookDuration.seconds.toString()
              )
              if (selectBookChaptersSize > 1) {
                val chaptersSize = pluralStringResource(
                  id = CommonR.plurals.chapters_count,
                  count = selectBookChaptersSize,
                  selectBookChaptersSize
                )
                HorizontalDivider(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  thickness = Dp.Hairline
                )
                BookDetailsRow(value = chaptersSize)
              }

              if (book.duration != book.duration - book.position) {
                HorizontalDivider(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  thickness = Dp.Hairline
                )
                val remainingTime = ((book.duration - book.position) / 1000).seconds.toString()
                BookDetailsRow(
                  title = stringResource(CommonR.string.remaining_time),
                  value = remainingTime
                )
              }

              HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = Dp.Hairline
              )
              val context = LocalContext.current
              val justNowThreshold = 1.minutes
              val addedAt = if (ChronoUnit.MILLIS.between(book.content.addedAt, Instant.now()).milliseconds < justNowThreshold) {
                stringResource(StringsR.string.bookmark_just_now)
              } else {
                DateUtils.getRelativeDateTimeString(
                  context,
                  book.content.addedAt.toEpochMilli(),
                  justNowThreshold.inWholeMilliseconds,
                  2.days.inWholeMilliseconds,
                  0,
                ).toString()
              }
              BookDetailsRow(
                title = stringResource(StringsR.string.migration_detail_content_added_at),
                value = addedAt
              )

              if (book.content.lastPlayedAt != Instant.EPOCH) {
                HorizontalDivider(
                  modifier = Modifier.padding(horizontal = 16.dp),
                  thickness = Dp.Hairline
                )
                val lastPlayedAt = if (ChronoUnit.MILLIS.between(book.content.lastPlayedAt, Instant.now()).milliseconds < justNowThreshold) {
                  stringResource(StringsR.string.bookmark_just_now)
                } else {
                  DateUtils.getRelativeDateTimeString(
                    context,
                    book.content.lastPlayedAt.toEpochMilli(),
                    justNowThreshold.inWholeMilliseconds,
                    2.days.inWholeMilliseconds,
                    0,
                  ).toString()
                }
                BookDetailsRow(
                  title = stringResource(CommonR.string.last_played_at),
                  value = lastPlayedAt
                )
              }

              val path = book.id.toUri().pathSegments
                .let { segments ->
                  val result = segments.lastOrNull()?.removePrefix("primary:")
                  if (result.isNullOrEmpty()) {
                    Logger.e("Could not determine path for $segments")
                    segments.joinToString(separator = "\"")
                  } else {
                    result
                  }
                }
              BookDetailsRow(
                title = stringResource(CommonR.string.path),
                value = path
              )
            }
          }
        }
      }
    },
    properties = DialogProperties(usePlatformDefaultWidth = false),
  )
}

@Composable
internal fun BookDetailsRow(
  title: String? = null,
  value: String,
  paddingStart: Dp = 16.dp,
  paddingEnd: Dp = 16.dp,
  paddingTop: Dp = 6.dp,
  paddingBottom: Dp = 6.dp,
//  click: () -> Unit
) {
  Row(
    modifier = Modifier
      .heightIn(min = 42.dp)
      .fillMaxWidth()
//      .clickable {
//        click()
//      }
      .padding(start = paddingStart, end = paddingEnd, top = paddingTop, bottom = paddingBottom),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically
  ) {
    if (title != null) {
      Text(
        modifier = Modifier.alpha(0.6f),
        text = title,
        lineHeight = 15.sp,
      )
      Spacer(modifier = Modifier.size(16.dp))
    }
    Text(
      modifier = Modifier.fillMaxWidth(),
      text = value,
      lineHeight = 15.sp,
      overflow = TextOverflow.Ellipsis,
      textAlign = TextAlign.End
    )
  }
}
