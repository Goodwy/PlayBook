package voice.data

import voice.common.comparator.NaturalOrderComparator

enum class BookComparator(private val comparatorFunction: Comparator<Book>) : Comparator<Book> by comparatorFunction {

  ByLastPlayed(
    compareByDescending {
      it.content.lastPlayedAt
    },
  ),
  ByName(
    Comparator { left, right ->
      val compare = NaturalOrderComparator.stringComparator.compare(left.content.name, right.content.name)
      if (compare == 0) NaturalOrderComparator.stringComparator.compare(left.content.author, right.content.author)
      else compare
    },
  ),
  ByAuthor(
    Comparator { left, right ->
      val compare = NaturalOrderComparator.stringComparator.compare(left.content.author, right.content.author)
      if (compare == 0) NaturalOrderComparator.stringComparator.compare(left.content.name, right.content.name)
      else compare
    },
  ),
  ByLastAdded(
    compareByDescending {
      it.content.addedAt
    },
  ),
}
