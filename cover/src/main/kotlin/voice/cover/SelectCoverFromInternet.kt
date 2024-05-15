package voice.cover

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import voice.common.BookId
import voice.common.compose.SharedComponent
import voice.common.compose.VoiceTheme
import voice.common.compose.rememberScoped
import voice.common.rootComponentAs
import voice.cover.api.SearchResponse

@Composable
fun SelectCoverFromInternet(
  bookId: BookId,
  onCloseClick: () -> Unit,
) {
  val viewModel = rememberScoped(bookId.value) {
    rootComponentAs<SelectCoverFromInternetViewModel.Factory.Provider>()
      .factory
      .create(bookId)
  }

  val sink = MutableSharedFlow<SelectCoverFromInternetViewModel.Events>(extraBufferCapacity = 1)
  SelectCoverFromInternet(
    viewState = viewModel.viewState(sink),
    onCloseClick = onCloseClick,
    onCoverClick = {
      sink.tryEmit(SelectCoverFromInternetViewModel.Events.CoverClick(it))
    },
    onRetry = {
      sink.tryEmit(SelectCoverFromInternetViewModel.Events.Retry)
    },
    onQueryChange = {
      sink.tryEmit(SelectCoverFromInternetViewModel.Events.QueryChange(it))
    },
  )
}

@Composable
private fun SelectCoverFromInternet(
  preview: Boolean = false,
  viewState: SelectCoverFromInternetViewModel.ViewState,
  onCloseClick: () -> Unit,
  onCoverClick: (SearchResponse.ImageResult) -> Unit,
  onRetry: () -> Unit,
  onQueryChange: (String) -> Unit,
) {
  val paddings = if (preview) "0;0;0;0"
  else {
    remember { rootComponentAs<SharedComponent>().paddingPref.flow }.collectAsState(initial = "0;0;0;0", context = Dispatchers.Unconfined).value
  }
  val top = paddings.substringBefore(';').toInt()
  //val bottom = paddings.substringAfter(';').substringBefore(';').toInt()
  val start = paddings.substringBeforeLast(';').substringAfterLast(';').toInt()
  val end = paddings.substringAfterLast(';').toInt()
  Box(
    modifier = Modifier
      .background(MaterialTheme.colorScheme.background)
      .padding(start.dp, 0.dp, end.dp, 0.dp),
  )
  {
    CoverSearchBar(
      topPadding = top.dp,
      onCloseClick = onCloseClick,
      onQueryChange = onQueryChange,
      viewState = viewState,
    )
    CoverContents(
      viewState = viewState,
      onCoverClick = onCoverClick,
      onRetry = onRetry,
    )
  }
}

@Preview
@Composable
private fun ErrorPreview() {
  VoiceTheme(true) {
    Surface {
      SelectCoverFromInternet(
        preview = true,
        viewState = SelectCoverFromInternetViewModel.ViewState.Error("Searching..."),
        onCloseClick = {},
        onCoverClick = {},
        onRetry = {},
        onQueryChange = {},
      )
    }
  }
}

@Preview
@Composable
private fun ListPreview() {
  VoiceTheme(true) {
    Surface {
      val element = SearchResponse.ImageResult(
        width = 100,
        height = 100,
        image = "image",
        thumbnail = "thumb",
      )
      val items = MutableStateFlow(
        PagingData.from(
          buildList<SearchResponse.ImageResult> {
            repeat(10) {
              add(element)
            }
          },
        ),
      ).collectAsLazyPagingItems()
      SelectCoverFromInternet(
        preview = true,
        viewState = SelectCoverFromInternetViewModel.ViewState.Content(
          items = items,
          query = "Search Term",
        ),
        onCloseClick = {},
        onCoverClick = {},
        onRetry = {},
        onQueryChange = {},
      )
    }
  }
}
