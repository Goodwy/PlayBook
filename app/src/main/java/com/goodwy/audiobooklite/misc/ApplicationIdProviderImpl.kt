package com.goodwy.audiobooklite.misc

import com.goodwy.audiobooklite.BuildConfig
import com.goodwy.audiobooklite.common.ApplicationIdProvider
import javax.inject.Inject

class ApplicationIdProviderImpl
@Inject constructor() : ApplicationIdProvider {

  override val applicationID = BuildConfig.APPLICATION_ID
}
