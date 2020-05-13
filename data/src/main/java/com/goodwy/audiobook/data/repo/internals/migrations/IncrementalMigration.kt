package com.goodwy.audiobook.data.repo.internals.migrations

import androidx.room.migration.Migration

abstract class IncrementalMigration(from: Int) : Migration(from, from + 1)
