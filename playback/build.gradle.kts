plugins {
  id("voice.library")
  alias(libs.plugins.anvil)
  alias(libs.plugins.kotlin.serialization)
}

android {
  buildFeatures {
    androidResources = true
  }
}

anvil {
  generateDaggerFactories.set(true)
}

dependencies {
  implementation(projects.common)
  implementation(projects.strings)
  implementation(projects.data)

  implementation(libs.androidxCore)
  implementation(libs.prefs.core)
  implementation(libs.datastore)
  implementation(libs.coroutines.guava)
  implementation(libs.serialization.json)
  implementation(libs.coil)
  implementation(libs.dagger.core)

  implementation(libs.media3.exoplayer)
  implementation(libs.media3.session)

  testImplementation(libs.bundles.testing.jvm)
  testImplementation(libs.prefs.inMemory)
  testImplementation(libs.media3.testUtils.core)
  testImplementation(libs.media3.testUtils.robolectric)
  testImplementation(libs.prefs.inMemory)
  testImplementation(libs.media3.testUtils.core)
  testImplementation(libs.media3.testUtils.robolectric)
}
