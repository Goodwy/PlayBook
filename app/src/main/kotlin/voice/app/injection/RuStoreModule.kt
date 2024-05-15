package voice.app.injection

import android.app.Application
import ru.rustore.sdk.billingclient.RuStoreBillingClient
import ru.rustore.sdk.billingclient.RuStoreBillingClientFactory
import ru.rustore.sdk.billingclient.presentation.BillingClientTheme
import ru.rustore.sdk.billingclient.provider.BillingClientThemeProvider
import voice.common.constants.THEME_DARK

object RuStoreModule {
    private lateinit var ruStoreBillingClient: RuStoreBillingClient

    fun install(app: Application, theme: Int) {
        ruStoreBillingClient = RuStoreBillingClientFactory.create(
            context = app,
            consoleApplicationId = "2063556850",
            deeplinkScheme = "purchase-scheme",
            themeProvider = BillingClientThemeProviderImpl(theme)
        )
    }

    class BillingClientThemeProviderImpl(private val theme: Int): BillingClientThemeProvider {
        override fun provide(): BillingClientTheme {
            return if (theme == THEME_DARK) BillingClientTheme.Dark else BillingClientTheme.Light
        }
    }

    fun provideRuStoreBillingClient(): RuStoreBillingClient = ruStoreBillingClient

}
