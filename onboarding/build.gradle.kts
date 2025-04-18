plugins {
  id("voice.library")
  id("voice.compose")
  alias(libs.plugins.anvil)
}

anvil {
  generateDaggerFactories.set(true)
}

android {
  buildFeatures {
    androidResources = true
  }
}

dependencies {
  implementation(projects.common)
  implementation(projects.strings)
  implementation(projects.data)
  implementation(projects.datastore)
  implementation(projects.folderPicker)
  implementation(projects.pref)

  implementation(libs.datastore)
  implementation(libs.coil)
  implementation(libs.androidxCore)

  implementation(libs.dagger.core)
}
