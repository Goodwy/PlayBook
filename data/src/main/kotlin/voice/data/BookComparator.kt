package voice.data

import voice.common.comparator.NaturalOrderComparator

enum class BookComparator(
  private val comparatorFunction: Comparator<Book>,
) : Comparator<Book> by comparatorFunction {

  ByLastPlayed(
    compareByDescending {
      it.content.lastPlayedAt
    },
  ),
  ByName(
    Comparator { left, right ->
      NaturalOrderComparator.stringComparator.compare(left.content.name, right.content.name)
    },
  ),
  ByAuthor(
    Comparator { left, right ->
      NaturalOrderComparator.stringComparator.compare(left.content.author, right.content.author)
    },
  ),
  ByLastAdded(
    compareByDescending<Book> {
      it.content.addedAt
    },
  ),
}
