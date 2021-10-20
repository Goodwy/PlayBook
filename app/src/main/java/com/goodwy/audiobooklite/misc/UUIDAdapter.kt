package com.goodwy.audiobooklite.misc

import de.paulwoitaschek.flowpref.android.PrefAdapter
import java.util.UUID

object UUIDAdapter : PrefAdapter<UUID> {

  override fun fromString(string: String): UUID {
    return UUID.fromString(string)
  }

  override fun toString(value: UUID): String {
    return value.toString()
  }
}
