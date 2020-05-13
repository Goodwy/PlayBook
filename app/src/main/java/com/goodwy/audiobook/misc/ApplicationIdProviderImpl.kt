package com.goodwy.audiobook.misc

import com.goodwy.audiobook.BuildConfig
import com.goodwy.audiobook.common.ApplicationIdProvider
import javax.inject.Inject

class ApplicationIdProviderImpl
@Inject constructor() : ApplicationIdProvider {

  override val applicationID = BuildConfig.APPLICATION_ID
}
